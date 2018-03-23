package com.exeter.ecm2425.morecast.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailedActivity extends AppCompatActivity {

    private ArrayList<FiveDayForecast> forecastData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        forecastData = getIntent().getParcelableArrayListExtra("forecast-day");
    }
}
