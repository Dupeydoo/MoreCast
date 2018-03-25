package com.exeter.ecm2425.morecast.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.exeter.ecm2425.morecast.DataProcessing.BindDetailedView;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Views.TodayView;
import com.exeter.ecm2425.morecast.Views.ViewHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {

    private ArrayList<FiveDayForecast> forecastData;
    private final static String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        if(savedInstanceState == null) {
            forecastData = getIntent().getParcelableArrayListExtra("forecast-day");
        } else {
            forecastData = savedInstanceState.getParcelableArrayList("forecast-day");
        }
        bindData();
        // Midday
        setBackground(forecastData.get(4));
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putParcelableArrayList("forecast-day", forecastData);
        super.onSaveInstanceState(outBundle);
    }

    private void bindData() {
        TodayView detailedView = (TodayView) findViewById(R.id.detailedView);
        BindDetailedView binder = new BindDetailedView(forecastData, detailedView);
        binder.bindDetailedData();
        setSharedPreferences();
    }

    private void setSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        this.setTitle(preferences.getString("location", "London"));
    }

    private void setBackground(FiveDayForecast forecast) {
        ImageView background = (ImageView) findViewById(R.id.weatherBack);
        ViewHelper.setBackground(forecast, background, getResources());
    }
}
