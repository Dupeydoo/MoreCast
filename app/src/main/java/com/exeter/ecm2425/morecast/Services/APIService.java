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
import com.google.android.gms.maps.model.LatLng;

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


public class APIService extends IntentService {

    private final String API_KEY = "79ccd6c60ce7d9f6fa8bb7b4f561a924";
    private String apiAddress = "https://api.openweathermap.org/data/2.5/forecast?units=metric&";
    public static final int API_RUNNING = 1;  // Enums not recommended in Android - reason.
    public static final int API_FINISHED = 0;
    public static final int API_ERROR = -1;

    public APIService() { super("Default"); }

    public APIService(String name) {
        super(name);
    }

    protected void onHandleIntent(Intent intent) {
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

    private String makeApiCall(String apiSuffix) throws IOException {
        StringBuilder apiResult = new StringBuilder();
        URL url = new URL(apiAddress + apiSuffix + "&APPID=" + API_KEY);
        HttpsURLConnection apiConnection = (HttpsURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(apiConnection.getInputStream()));

        String line;
        while((line = reader.readLine()) != null) {
            apiResult.append(line).append("\n");
        }
        reader.close();
        apiConnection.disconnect();
        return apiResult.toString();
    }

    private void namedLocationHandler(Intent intent, ResultReceiver receiver, Bundle apiBundle) {
        String namedLocation = intent.getStringExtra("named-location");
        String apiSuffix = String.format(Locale.ENGLISH, "q=%s", namedLocation);
        receiver.send(API_RUNNING, Bundle.EMPTY);

        try {
            String apiResult = makeApiCall(apiSuffix);
            apiBundle.putString("result", apiResult);
            JSONObject resultData = new JSONObject(apiResult);
            ForecastParser parser = new ForecastParser();
            parser.parseWeatherData(resultData);
            ArrayList<FiveDayForecast> fiveDayForecasts = new ArrayList<>
                    (Arrays.asList(parser.getFiveDayForecasts()));
            apiBundle.putParcelableArrayList("forecast", fiveDayForecasts);
            receiver.send(API_FINISHED, apiBundle);
        } catch(Exception exception) {
            apiBundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(API_ERROR, apiBundle);
        }
    }

    private void latLongLocationHandler(Intent intent, ResultReceiver receiver, Bundle apiBundle) {
        double longitude, latitude;
        Location location = intent.getParcelableExtra("location");
        LatLng latLng = intent.getParcelableExtra("lat-lng");

        if(latLng != null) {
            longitude = latLng.longitude;
            latitude = latLng.latitude;
        } else {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        String apiSuffix = String.format(Locale.ENGLISH, "lat=%f&lon=%f", latitude, longitude);
        receiver.send(API_RUNNING, Bundle.EMPTY);

        try {
            String apiResult = makeApiCall(apiSuffix);
            apiBundle.putString("result", apiResult);
            ArrayList<FiveDayForecast> forecast = insertApiResponseDatabase(apiResult);
            apiBundle.putParcelableArrayList("forecast", forecast);
            receiver.send(API_FINISHED, apiBundle);
        } catch(Exception exception) {
            apiBundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(API_ERROR, apiBundle);
        }
    }

    private ArrayList<FiveDayForecast> insertApiResponseDatabase(String apiResult) throws APIException {
        AccessDatabase database = new AccessDatabase();
        return database.save(apiResult, getApplicationContext());
    }
}
