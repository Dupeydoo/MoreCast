package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.Activities.DetailedActivity;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * ForecastView is the smaller custom view that displays forecast information
 * for the upcoming days in the MainActivity RecyclerView. The view is implemented
 * as a ConstraintLayout to be as lightweight as possible for the RecyclerView.
 * The view is designed for re-usability and layout of several child views.
 *
 * Data for these smaller views is displayed at 12pm UTC, a reasonable choice
 * when considering different timezones.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class ForecastView extends ConstraintLayout implements View.OnClickListener {
    private TextView forecastDay;
    private ImageView forecastImage;
    private TextView forecastTemp;
    private ArrayList<FiveDayForecast> dayForecasts;

    /**
     * Constructor for the ForecastView, inflates the view
     * so the RecyclerView does not have to.
     * @param context The context where the view will be inflated.
     */
    public ForecastView(Context context) {
        super(context);
        setOnClickListener(this);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.forecast_view, this);
        setViews();
    }

    /**
     * Finds and initialises the child views of the ForecastView for
     * binding.
     */
    private void setViews() {
        this.forecastDay = (TextView) findViewById(R.id.forecastDay);
        this.forecastImage = (ImageView) findViewById(R.id.forecastImage);
        this.forecastTemp = (TextView) findViewById(R.id.forecastTemp);
    }

    /**
     * Sets the forecast information for the day to be displayed.
     * @param day The day of the week that is to be displayed.
     * @param temp The temperature to display.
     */
    public void setForecast(String day, double temp) {
        this.forecastDay.setText(day);
        this.forecastTemp.setText(String.format(Locale.ENGLISH, "%.1f °C", temp));
    }

    /**
     * Sets the weather icon based on the current time, temperature and weather code.
     * @param code The weather code at 12pm UTC.
     * @param time The time of day.
     * @param temperature The temperature at the given forecast.
     */
    public void setForecastImage(int code, int time, double temperature) {
        ViewHelper weatherHelper = new ViewHelper();
        weatherHelper.setWeatherImage(this.getContext(), code, forecastImage, time, temperature);
    }

    /**
     * Retrieves the forecasts for the day that is bound to this ForecastView.
     * @return The forecasts for the day.
     */
    public ArrayList<FiveDayForecast> getDayForecasts() {
        return dayForecasts;
    }

    /**
     * Sets the forecasts for a day to this ForecastView instance.
     * @param dayForecasts The forecasts to set.
     */
    public void setDayForecasts(ArrayList<FiveDayForecast> dayForecasts) {
        this.dayForecasts = dayForecasts;
    }

    /**
     * {@inheritDoc}
     * Onclick handler for when a forecast view is clicked.
     * When clicked the current day's data is passed to the
     * DetailedActivity so a detailed forecast can be viewed.
     */
    @Override
    public void onClick(View view) {
        ForecastView forecastView = (ForecastView) view;

        // Create an intent for the DetailedActivity.
        Class dest = DetailedActivity.class;
        Intent intent = new Intent(forecastView.getContext(), dest);

        // Pass the forecast data to the DetailedActivity.
        intent.putParcelableArrayListExtra("forecast-day", this.getDayForecasts());
        forecastView.getContext().startActivity(intent);
    }
}
