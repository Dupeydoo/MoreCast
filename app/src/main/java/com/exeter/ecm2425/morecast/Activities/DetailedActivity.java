package com.exeter.ecm2425.morecast.Activities;

import android.os.Bundle;
import android.view.Menu;

import com.exeter.ecm2425.morecast.DataProcessing.BindDetailedView;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Views.TodayView;

import java.util.ArrayList;


/**
 * The Activity displayed when a user selects a forecasts from the MainActivity
 * RecyclerView. DetailedActivity knows only how to bind a single days forecast
 * data to the TodayView object, and to preserve that data on configuration change.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class DetailedActivity extends BaseActivity {
    // A reference to the forecast data for the currently selected day.
    private ArrayList<FiveDayForecast> forecastData;

    /**
     * {@inheritDoc}
     * Retrieves the forecast data from either an intent supplied by the MainActivity
     * or a bundle created due to a configuration change.
     */
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
        // Midday UTC is selected as a standard for displaying future forecasts.
        setBackground(forecastData.get(4));
    }

    /**
     * {@inheritDoc}
     * Adds the forecast to bundle before the Activity is pausing/stopping.
     */
    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putParcelableArrayList("forecast-day", forecastData);
        super.onSaveInstanceState(outBundle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Binds the forecast data to the detailed view with a title.
     */
    private void bindData() {
        TodayView detailedView = (TodayView) findViewById(R.id.detailedView);
        BindDetailedView binder = new BindDetailedView(forecastData, detailedView);

        // Bind the forecast data for a detailed forecast.
        binder.bindDetailedData();
        setTitleFromSharedPreferences();
    }
}
