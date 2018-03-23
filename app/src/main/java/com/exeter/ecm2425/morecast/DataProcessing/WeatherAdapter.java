package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Utils.DateHandler;
import com.exeter.ecm2425.morecast.Views.ForecastView;
import com.exeter.ecm2425.morecast.Views.TodayView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private ArrayList<FiveDayForecast> fiveDayForecasts;
    private final static int FORECAST_DAYS = 5;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TodayView todayView;
        public ForecastView forecastView;

        public ViewHolder(TodayView todayView) {
            super(todayView);
            this.todayView = todayView;
        }

        public ViewHolder(ForecastView forecastView) {
            super(forecastView);
            this.forecastView = forecastView;
        }
    }


    public WeatherAdapter(ArrayList<FiveDayForecast> forecasts) {
        fiveDayForecasts = forecasts;
    }


    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        if(viewType == 0) {
            TodayView today = new TodayView(parent.getContext());
            return new ViewHolder(today);
        }

        else {
            ForecastView forecastView = new ForecastView(parent.getContext());
            forecastView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(forecastView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<FiveDayForecast> day;
        if(holder.getItemViewType() == 0) {
            day = ResultParser.getForecastDay(fiveDayForecasts);
            BindWeatherAdapter binder = new BindWeatherAdapter(day, holder);
            binder.bindToday();
        }

        else {
            day = ResultParser.getForecastDay(fiveDayForecasts);
            BindWeatherAdapter binder = new BindWeatherAdapter(day, holder);
            binder.bindForecast();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 0;
        return 1;
    }


    @Override
    public int getItemCount() {
        return FORECAST_DAYS;
    }
}
