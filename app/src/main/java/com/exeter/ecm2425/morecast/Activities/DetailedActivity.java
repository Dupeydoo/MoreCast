package com.exeter.ecm2425.morecast.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.exeter.ecm2425.morecast.DataProcessing.BindDetailedView;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Views.TodayView;
import com.exeter.ecm2425.morecast.Views.ViewHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailedActivity extends BaseActivity {

    private ArrayList<FiveDayForecast> forecastData;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void bindData() {
        TodayView detailedView = (TodayView) findViewById(R.id.detailedView);
        BindDetailedView binder = new BindDetailedView(forecastData, detailedView);
        binder.bindDetailedData();
        setSharedPreferences();
    }
}
