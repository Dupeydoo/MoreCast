package com.exeter.ecm2425.morecast.Activities;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.API.APIException;
import com.exeter.ecm2425.morecast.API.APILocation;
import com.exeter.ecm2425.morecast.DataProcessing.ResultParser;
import com.exeter.ecm2425.morecast.DataProcessing.WeatherAdapter;
import com.exeter.ecm2425.morecast.Database.AccessDatabase;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.API.APIResultReceiver;
import com.exeter.ecm2425.morecast.Services.APIService;
import com.exeter.ecm2425.morecast.Utils.NetworkHelper;
import com.exeter.ecm2425.morecast.Views.ErrorDialog;
import com.exeter.ecm2425.morecast.Views.ViewHelper;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.exeter.ecm2425.morecast.API.APILocation.PERMISSIONS_SUCCESS;

/**
 * The MainActivity is the home screen of Morecast. Here the results of API calls
 * are bound to a RecyclerView. A detailed forecast for the current day is displayed
 * with four future days in less detail. Actual long running processes like API calls
 * are handled by the APIService. The UI thread is always doing the minimum amount of work.
 *
 * @author 640010970
 * @version 4.0.0
 */
public class MainActivity extends BaseActivity implements APIResultReceiver.Receiver {
    // Reference to the API data used to pass from IntentServices to Activites alike.
    private Bundle resultData;

    /**
     * {@inheritDoc}
     * Performs checks for configuration changes, lack of network
     * connectivity, and lack of API data. Also checks if the user
     * has given the application permissions to their current location.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle apiData = null;

        // If the screen was rotated then fetch the API data.
        if(savedInstanceState != null) {
            apiData = savedInstanceState.getBundle("api-data");
        }

        // Check if the apiData is null, and if the user has internet.
        if(apiData == null && NetworkHelper.checkForInternet(this)) {

            // Check for location permissions.
            if (APILocation.checkLocationPermission(this)) {

                // Make an API call!
                startIntentReceiver(false);
            } else {
                requestPermissions();
            }
        } else {
            try {
                apiData = savedInstanceState.getBundle("api-data");
                postProcessResults(apiData);
            } catch(NullPointerException nullException) {
                // If we can't get data due to lack of connectivity
                // then read the most recent database five day forecast.
                performDatabaseTask();
            }
        }
    }

    /**
     * {@inheritDoc}
     * Put the API data into a bundle when the Activity is getting
     * ready to pause or stop.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("api-data", resultData);
        super.onSaveInstanceState(outState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.actionLocation:
                String nullLocation = null;
                getIntent().putExtra("named-location", nullLocation);
                startIntentReceiver(true);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates an intent to the APIService to signal its time to
     * make an API call.
     * @param intent The intent to supply to the APIService.
     * @param namedLocation A named location chosen from the default
     *                      capital city locations.
     */
    public void startApiService(Intent intent, String namedLocation) {
        intent.putExtra("named-location", namedLocation);
        intent.putExtra("api-receiver", apiReceiver);
        intent.putExtra("command", "forecast");
        startService(intent);
    }

    /**
     * Overload of startAPIService, to make an API call with a Longitude
     * and Latitude.
     * @see "MainActivity.startApiService(Intent intent, String namedLocation)"
     */
    public void startApiService(Intent intent, Location location, boolean isGpsButton) {
        intent.putExtra("location", location);

        if(!isGpsButton) {
            intent.putExtra("lat-lng", getIntent().getParcelableExtra("lat-lng"));
        }

        intent.putExtra("api-receiver", apiReceiver);
        intent.putExtra("command", "forecast-location");
        startService(intent);
    }

    /**
     * Implements the APIResultReceiver interface. This method is called
     * when the APIService sends updates, a result, or an error from a
     * long running operation performed on a separate thread.
     * @param resultCode The code of the result: success, running, or failure.
     * @param resultData The data sent from the APIService to the MainActivity.
     */
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ProgressBar apiBar = (ProgressBar) findViewById(R.id.apiBar);
        switch (resultCode) {
            case APIService.API_RUNNING:
                // Show the loader while the user waits.
                apiBar.setVisibility(View.VISIBLE);
                break;

            case APIService.API_FINISHED:
                // Use the API data to bind to the MainActivity.
                postProcessResults(resultData);
                apiBar.setVisibility(View.INVISIBLE);
                break;

            case APIService.API_ERROR:
                // If an API call fails, the last resort is to fetch
                // forecast data from the database.
                performDatabaseTask();
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull  String permissions[], @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSIONS_SUCCESS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The user has agreed, lets restart the Activity.
                    this.recreate();
                } else {
                    // Otherwise default to the major capital city, London.
                    Intent apiIntent = setReceiverAndIntent();
                    startApiService(apiIntent, "London");
                }
        }
    }

    /**
     * Processes the results from an API call.
     * @param resultData A bundle which contains the String result data.
     */
    private void postProcessResults(Bundle resultData) {
        this.resultData = resultData;

        // Extract the string from the Bundle.
        ResultParser parser = new ResultParser(resultData.getString("result"));

        try {
            // Parse the string and retrieve the forecast data.
            JSONObject result = parser.parseResult();
            ArrayList<FiveDayForecast> forecast = resultData.getParcelableArrayList("forecast");

            // Set UI aesthetics based on weather code.
            setResultTitle(result);
            setBackground(forecast.get(0));

            // Bind the data to the RecyclerView.
            setUpRecyclerView(forecast);
        } catch(APIException apiException) {
            // Signal to the user that data could not be retrieved. This should never happen due
            // to error handling but is a backup.
            Resources resources = getResources();
            ErrorDialog errorDialog = new ErrorDialog(resources.getString(R.string.emptyAPIError),
                    resources.getString(R.string.errorTitle));
            errorDialog.showDialog(this);
        }
    }

    /**
     * Processes the results from a database read.
     * @param forecastData A list of forecast data for the five day period.
     */
    private void postProcessDatabaseResults(ArrayList<FiveDayForecast> forecastData) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        this.setTitle(sharedPreferences.getString("location", "London"));
        ResultParser parser = new ResultParser();
        setBackground(forecastData.get(0));
        setUpRecyclerView(forecastData);
    }

    /**
     * Sets up the RecyclerView with a layout manager and WeatherAdapter to bind
     * data.
     * @param forecastData The data sent to the WeatherAdapter to bind.
     */
    private void setUpRecyclerView(ArrayList<FiveDayForecast> forecastData) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.weatherList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager viewManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(viewManager);
        WeatherAdapter viewAdapter = new WeatherAdapter(forecastData);
        recyclerView.setAdapter(viewAdapter);
    }

    /**
     * Beings the IntentService and ResultReceiver pattern. If a named location
     * is provided then pass that to the APIService.
     */
    private void startIntentReceiver(boolean isGpsButton) {
        Intent apiIntent;
        if(!isGpsButton) {
            apiIntent = setReceiverAndIntent();
            apiIntent.putExtra("lat-lng-call", true);
        } else {
            apiIntent = setReceiverAndIntent();
            apiIntent.putExtra("is-gps-button", true);
            apiIntent.putExtra("lat-lng-call", true);
        }
        String namedLocation = getIntent().getStringExtra("named-location");

        if(namedLocation != null) {
            startApiService(apiIntent, namedLocation);
        } else {
            // Otherwise use the current location, or LatLng from Google Fragment.
            APILocation apiLocater = new APILocation(this, apiIntent);
        }
    }

    /**
     * Creates an APIResultReceiver to allow the MainActivity and APIService
     * to communicate.
     * @return Intent An intent to the APIService for data synchronisation.
     */
    protected Intent setReceiverAndIntent() {
        apiReceiver = new APIResultReceiver(new Handler());
        apiReceiver.setReceiver(this);
        return new Intent(Intent.ACTION_SYNC, null, this, APIService.class);
    }

    /**
     * Begin the database read AsyncTask.
     */
    private void performDatabaseTask() {
        // Stored under a object to prevent a memory leak.
        AsyncTask<Context, Void, ArrayList<FiveDayForecast>> readDb =
                new DatabaseReadTask().execute(this);
    }

    /**
     * A private nested AsyncTask to read data from the database if there is no internet
     * connection. Nested as it is tightly bound to the hosting activity. When a database read
     * is required it is performed on a separate thread.
     *
     * @author 640010970
     * @version 1.0.0
     */
    private class DatabaseReadTask extends AsyncTask<Context, Void, ArrayList<FiveDayForecast>> {
        /**
         * {@inheritDoc}
         * Shows a spinner when the database is being read.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = findViewById(R.id.apiBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * {@inheritDoc}
         * Uses the AccessDatabase class to read data from the database.
         * @param context The array of contexts length 1 which contains the hosting
         *                context.
         * @return ArrayList<FiveDayForecast> The forecast data from the database.
         */
        @Override
        protected ArrayList<FiveDayForecast> doInBackground(Context... context) {
            Context current = context[0];
            AccessDatabase accessDatabase = new AccessDatabase();
            ArrayList<FiveDayForecast> fiveDayForecasts = accessDatabase.read(current);
            return fiveDayForecasts;
        }

        /**
         * {@inheritDoc}
         * Checks the database results were accessed, and post-processes them.
         * @param forecasts The forecast data returned by doInBackground.
         */
        @Override
        protected void onPostExecute(ArrayList<FiveDayForecast> forecasts) {
            super.onPostExecute(forecasts);
            if(forecasts.size() > 0) {
                postProcessDatabaseResults(forecasts);
            } else {
                // If there is no database data, or it cannot be accessed
                // then notify the user.
                TextView alertView = (TextView) findViewById(R.id.alertView);
                alertView.setVisibility(View.VISIBLE);
                alertView.bringToFront();
            }
            ProgressBar progressBar = findViewById(R.id.apiBar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
