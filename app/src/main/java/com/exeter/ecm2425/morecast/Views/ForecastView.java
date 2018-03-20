package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;

import java.util.Locale;


public class ForecastView extends ConstraintLayout {
    private TextView forecastDay;
    private ImageView forecastImage;
    private TextView forecastTemp;

    public ForecastView(Context context) {
        super(context);
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
}
