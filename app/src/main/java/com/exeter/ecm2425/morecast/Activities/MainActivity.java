package com.exeter.ecm2425.morecast.Activities;


import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.API.APILocation;
import com.exeter.ecm2425.morecast.DataProcessing.ResultParser;
import com.exeter.ecm2425.morecast.DataProcessing.WeatherAdapter;
import com.exeter.ecm2425.morecast.Database.MorecastDatabase;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.API.APIResultReceiver;
import com.exeter.ecm2425.morecast.Services.APIService;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements APIResultReceiver.Receiver {

    public APIResultReceiver apiReceiver;

    private APILocation apiLocater;
    private Intent apiIntent;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter viewAdapter;
    private RecyclerView.LayoutManager viewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        apiReceiver = new APIResultReceiver(new Handler());
        apiReceiver.setReceiver(this);
        apiIntent = new Intent(Intent.ACTION_SYNC, null, this, APIService.class);
        String namedLocation = getIntent().getStringExtra("named-location");

        if(namedLocation != null) {
            startApiService(apiIntent, namedLocation);
        } else {
            apiLocater = new APILocation(this, apiIntent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        apiReceiver.setReceiver(null);
    }

    public void startApiService(Intent intent, String namedLocation) {
        intent.putExtra("named-location", namedLocation);
        intent.putExtra("api-receiver", apiReceiver);
        intent.putExtra("command", "forecast");
        startService(intent);
    }

    public void startApiService(Intent intent, Location location) {
        intent.putExtra("location", location);
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
                // Error - go to db instead
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case APILocation.PERMISSIONS_SUCCESS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    apiLocater = new APILocation(this, apiIntent);
                } else {
                    // permission denied
                }
        }
    }

    private void postProcessResults(Bundle resultData) {
        ResultParser parser = new ResultParser(resultData.getString("result"));
        JSONObject forecastResult = parser.parseResult();

        try {
            this.setTitle(forecastResult.getJSONObject("city").getString("name"));
            setUpRecyclerView(forecastResult);
        }

        catch(JSONException e) {
            // log
        }
    }

    private void setUpRecyclerView(JSONObject forecastData) {
        recyclerView = (RecyclerView) findViewById(R.id.weatherList);
        recyclerView.setHasFixedSize(true);
        viewManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(viewManager);
        viewAdapter = new WeatherAdapter(forecastData);
        recyclerView.setAdapter(viewAdapter);
    }
}
