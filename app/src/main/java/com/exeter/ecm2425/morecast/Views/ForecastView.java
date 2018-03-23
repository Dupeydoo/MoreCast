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


public class ForecastView extends ConstraintLayout implements View.OnClickListener {
    private TextView forecastDay;
    private ImageView forecastImage;
    private TextView forecastTemp;
    private int recyclerId;
    private ArrayList<FiveDayForecast> dayForecasts;

    public ForecastView(Context context) {
        super(context);
        setOnClickListener(this);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.forecast_view, this);
        setViews();
    }

    private void setViews() {
        this.forecastDay = (TextView) findViewById(R.id.forecastDay);
        this.forecastImage = (ImageView) findViewById(R.id.forecastImage);
        this.forecastTemp = (TextView) findViewById(R.id.forecastTemp);
    }

    public void setForecast(String day, double temp) {
        this.forecastDay.setText(day);
        this.forecastTemp.setText(String.format(Locale.ENGLISH, "%.1f °C", temp));
    }

    public void setForecastImage(int code, int time) {
        ViewHelper weatherHelper = new ViewHelper();
        weatherHelper.setWeatherImage(this.getContext(), code, forecastImage, time);
    }

    public ArrayList<FiveDayForecast> getDayForecasts() {
        return dayForecasts;
    }

    public void setDayForecasts(ArrayList<FiveDayForecast> dayForecasts) {
        this.dayForecasts = dayForecasts;
    }

    @Override
    public void onClick(View view) {
        ForecastView forecastView = (ForecastView) view;
        Class dest = DetailedActivity.class;
        Intent intent = new Intent(forecastView.getContext(), dest);
        intent.putParcelableArrayListExtra("forecast-day", this.getDayForecasts());
        forecastView.getContext().startActivity(intent);
    }
}
