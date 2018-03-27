package com.exeter.ecm2425.morecast.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.exeter.ecm2425.morecast.API.APIException;
import com.exeter.ecm2425.morecast.API.APIResultReceiver;
import com.exeter.ecm2425.morecast.DataProcessing.ForecastParser;
import com.exeter.ecm2425.morecast.Database.AccessDatabase;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Utils.NetworkHelper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * The APIService serves as the main worker for delivering data
 * to the Activities. When invoked it starts up a new thread completely separate
 * from the UI thread and makes API calls based on the parameters it receives.
 *
 * Both location requests for forecasts data and database insertion is done by
 * the APIService. The many other classes it uses also make a TimeZone API call
 * and parse the data that comes from the APIs.
 *
 * Due to its separation from the MainActivity it cannot be interrupted by configuration
 * changes or other tasks that make an AsyncTask unsuitable for longer running processes.
 *
 * The IntentService -> ResultReceiver pattern is used to deliver data back to the
 * Activities that requested API calls.
 *
 * @author 640010970
 * @version 3.0.0
 */
public class APIService extends IntentService {

    // The OpenWeatherMap API Key.
    private final String API_KEY = "79ccd6c60ce7d9f6fa8bb7b4f561a924";

    // The base address to make request to the OpenWeatherMap API.
    private String apiAddress =
            "https://api.openweathermap.org/data/2.5/forecast?units=metric&";

    public static final int API_RUNNING = 1;
    public static final int API_FINISHED = 0;
    public static final int API_ERROR = -1;

    /**
     * Default Constructor, required by the ResultReceiever
     * but not explicitly invoked in Morecast code.
     */
    public APIService() { super("Default"); }

    /**
     * Named Constructor, once again required but not
     * explicitly invoked.
     * @param name The name of the APIService.
     */
    public APIService(String name) {
        super(name);
    }

    /**
     * Called when an intent to start the APIService is received from the Activity.
     * The APIService handles the intents differently based on whether a named location
     * or a lat and long coordinate set is specified.
     * @param intent The intent sent to the APIService.
     */
    protected void onHandleIntent(Intent intent) {
        // Get the receiver the Activity has provided to send data to and
        // obtain the command requested of the service.
        final ResultReceiver receiver = intent.getParcelableExtra("api-receiver");
        String command = intent.getStringExtra("command");
        Bundle apiBundle = new Bundle();

        if(command.equals("forecast")) {
            namedLocationHandler(intent, receiver, apiBundle);
        }

        else if(command.equals("forecast-location")) {
            latLongLocationHandler(intent, receiver, apiBundle);
        }
    }

    /**
     * Makes an API call to the OpenWeatherMap API with the supplied suffix.
     * A suffix refers to a Query string which contains either lat and long
     * or location parameters.
     * @param apiSuffix The query string to append to the url.
     * @return String The result of the API call.
     * @throws IOException If the API cannot be reached.
     */
    private String makeApiCall(String apiSuffix) throws IOException, JSONException {
        StringBuilder apiResult = new StringBuilder();
        URL url = new URL(apiAddress + apiSuffix + "&APPID=" + API_KEY);

        // Make a secure HTTPS connection with OpenWeatherMap.
        String apiString = NetworkHelper.makeApiConnection(url, apiResult);
        return apiString;
    }

    /**
     * Handles the intent when a named location is specified by the caller. Only occurs
     * when a default capital city is chosen from the RecyclerView in the LocationActivity.
     * @param intent The intent containing a named location, for instance, Berlin.
     * @param receiver The receiever to send data via to the calling Activity.
     * @param apiBundle The Bundle which the result of the API call are put into.
     */
    private void namedLocationHandler(Intent intent, ResultReceiver receiver, Bundle apiBundle) {
        String namedLocation = intent.getStringExtra("named-location");

        // Build the query string OpenWeatherMap needs.
        String apiSuffix = String.format(Locale.US, "q=%s", namedLocation);

        // Notify the Activity the service is running.
        receiver.send(API_RUNNING, Bundle.EMPTY);

        try {
            String apiResult = makeApiCall(apiSuffix);
            apiBundle.putString("result", apiResult);

            JSONObject resultData = new JSONObject(apiResult);
            ForecastParser parser = new ForecastParser();

            parser.parseWeatherData(resultData);
            ArrayList<FiveDayForecast> fiveDayForecasts = new ArrayList<>
                    (Arrays.asList(parser.getFiveDayForecasts()));

            // Send the parsed result back to the Activity.
            sendFiveDayForecasts(apiBundle, fiveDayForecasts, receiver);
        } catch(Exception exception) {
            // Send an error if an exception is caught.
            apiBundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(API_ERROR, apiBundle);
        }
    }

    /**
     * Handles the intent when a lat and long location is specified by the caller. Called
     * when the user requests either their current location or a location picked from the
     * Google AutoComplete Fragment.
     * @param intent The intent containing a lat and long.
     * @param receiver The receiever to send data via to the calling Activity.
     * @param apiBundle The Bundle which the result of the API call are put into.
     */
    private void latLongLocationHandler(Intent intent, ResultReceiver receiver, Bundle apiBundle) {
        double longitude, latitude;
        Location location = intent.getParcelableExtra("location");
        LatLng latLng = intent.getParcelableExtra("lat-lng");

        // Check if the request came from the AutoComplete Fragment.
        if(latLng != null) {
            longitude = latLng.longitude;
            latitude = latLng.latitude;
        } else {
            // This is a current location call, get lat
            // and long from the Location object.
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        String apiSuffix = String.format(Locale.US, "lat=%f&lon=%f", latitude, longitude);
        receiver.send(API_RUNNING, Bundle.EMPTY);

        try {
            String apiResult = makeApiCall(apiSuffix);
            apiBundle.putString("result", apiResult);
            ArrayList<FiveDayForecast> fiveDayForecasts = insertApiResponseDatabase(apiResult);
            sendFiveDayForecasts(apiBundle, fiveDayForecasts, receiver);
        } catch(Exception exception) {
            apiBundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(API_ERROR, apiBundle);
        }
    }

    /**
     * Insert the weather response from the API into the database for when the user
     * accesses the app without internet. The database is updated every time the user
     * makes a new API call.
     * @param apiResult The String result from the API.
     * @return ArrayList<FiveDayForecast> The list of forecast objects to use for the RecyclerView.
     * @throws APIException
     * @throws IOException
     */
    private ArrayList<FiveDayForecast> insertApiResponseDatabase(String apiResult)
            throws APIException, IOException {
        AccessDatabase database = new AccessDatabase();
        return database.save(apiResult, getApplicationContext());
    }

    /**
     * Handles the sending of a finished result with an ArrayList of forecast data.
     * @param bundle The bundle to send to the Activity.
     * @param fiveDayForecasts The forecasts to send to the Activity.
     * @param receiver The receiver which delivers data to the Activity.
     */
    private void sendFiveDayForecasts
            (Bundle bundle, ArrayList<FiveDayForecast> fiveDayForecasts, ResultReceiver receiver) {
        bundle.putParcelableArrayList("forecast", fiveDayForecasts);
        receiver.send(API_FINISHED, bundle);
    }
}
