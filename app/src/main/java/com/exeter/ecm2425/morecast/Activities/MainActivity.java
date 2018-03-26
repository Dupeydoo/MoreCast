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

import org.json.JSONObject;

import java.util.ArrayList;

import static com.exeter.ecm2425.morecast.API.APILocation.PERMISSIONS_SUCCESS;


public class MainActivity extends BaseActivity implements APIResultReceiver.Receiver {

    private Bundle resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle apiData = null;

        if(savedInstanceState != null) {
            apiData = savedInstanceState.getBundle("api-data");
        }

        if(apiData == null && NetworkHelper.checkForInternet(this)) {
            if (APILocation.checkLocationPermission(this)) {
                startIntentReceiver();
            } else {
                requestPermissions();
            }
        } else {
            try {
                apiData = savedInstanceState.getBundle("api-data");
                postProcessResults(apiData);
            } catch(NullPointerException nullException) {
                performDatabaseTask();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("api-data", resultData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void startApiService(Intent intent, String namedLocation) {
        intent.putExtra("named-location", namedLocation);
        intent.putExtra("api-receiver", apiReceiver);
        intent.putExtra("command", "forecast");
        startService(intent);
    }

    public void startApiService(Intent intent, Location location) {
        intent.putExtra("location", location);
        intent.putExtra("lat-lng", getIntent().getParcelableExtra("lat-lng"));
        intent.putExtra("api-receiver", apiReceiver);
        intent.putExtra("command", "forecast-location");
        startService(intent);
    }

    public void onReceiveResult(int resultCode, Bundle resultData) {
        ProgressBar apiBar = (ProgressBar) findViewById(R.id.apiBar);
        switch (resultCode) {
            case APIService.API_RUNNING:
                apiBar.setVisibility(View.VISIBLE);
                break;

            case APIService.API_FINISHED:
                postProcessResults(resultData);
                apiBar.setVisibility(View.INVISIBLE);
                break;

            case APIService.API_ERROR:
                performDatabaseTask();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull  String permissions[], @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSIONS_SUCCESS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.recreate();
                } else {
                    Intent apiIntent = setReceiverAndIntent();
                    startApiService(apiIntent, "London");
                }
        }
    }

    private void postProcessResults(Bundle resultData) {
        this.resultData = resultData;
        ResultParser parser = new ResultParser(resultData.getString("result"));
        try {
            JSONObject result = parser.parseResult();
            ArrayList<FiveDayForecast> forecast = resultData.getParcelableArrayList("forecast");
            setResultTitle(result);
            setBackground(forecast.get(0));
            setUpRecyclerView(forecast);
        } catch(APIException apiException) {
            Resources resources = getResources();
            ErrorDialog errorDialog = new ErrorDialog(resources.getString(R.string.emptyAPIError),
                    resources.getString(R.string.errorTitle));
            errorDialog.showDialog(this);
        }
    }

    private void postProcessDatabaseResults(ArrayList<FiveDayForecast> forecastData) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        this.setTitle(sharedPreferences.getString("location", "London"));
        ResultParser parser = new ResultParser();
        setBackground(forecastData.get(0));
        setUpRecyclerView(forecastData);
    }

    private void setUpRecyclerView(ArrayList<FiveDayForecast> forecastData) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.weatherList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager viewManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(viewManager);
        WeatherAdapter viewAdapter = new WeatherAdapter(forecastData);
        recyclerView.setAdapter(viewAdapter);
    }

    private void startIntentReceiver() {
        Intent apiIntent = setReceiverAndIntent();
        String namedLocation = getIntent().getStringExtra("named-location");

        if(namedLocation != null) {
            startApiService(apiIntent, namedLocation);
        } else {
            APILocation apiLocater = new APILocation(this, apiIntent);
        }
    }

    protected Intent setReceiverAndIntent() {
        apiReceiver = new APIResultReceiver(new Handler());
        apiReceiver.setReceiver(this);
        return new Intent(Intent.ACTION_SYNC, null, this, APIService.class);
    }

    private void performDatabaseTask() {
        AsyncTask<Context, Void, ArrayList<FiveDayForecast>> readDb =
                new DatabaseReadTask().execute(this);
    }

    private class DatabaseReadTask extends AsyncTask<Context, Void, ArrayList<FiveDayForecast>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar progressBar = findViewById(R.id.apiBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<FiveDayForecast> doInBackground(Context... context) {
            Context current = context[0];
            AccessDatabase accessDatabase = new AccessDatabase();
            ArrayList<FiveDayForecast> fiveDayForecasts = accessDatabase.read(current);
            return fiveDayForecasts;
        }

        @Override
        protected void onPostExecute(ArrayList<FiveDayForecast> forecasts) {
            super.onPostExecute(forecasts);
            if(forecasts.size() > 0) {
                postProcessDatabaseResults(forecasts);
            } else {
                TextView alertView = (TextView) findViewById(R.id.alertView);
                alertView.setVisibility(View.VISIBLE);
            }
            ProgressBar progressBar = findViewById(R.id.apiBar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
