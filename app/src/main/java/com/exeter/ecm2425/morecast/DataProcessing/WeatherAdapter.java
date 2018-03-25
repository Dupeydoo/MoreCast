package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Views.ForecastView;
import com.exeter.ecm2425.morecast.Views.TodayView;

import java.util.ArrayList;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private ArrayList<FiveDayForecast> fiveDayForecasts;
    private final static int FORECAST_DAYS = 5;
    private final static int BIND_TODAY = 0;
    private final static int BIND_FORECAST = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder  {
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

    // get next day, if it cant bind the next four times, get n-1 position from next day.
    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                        int viewType) {
        if (viewType == 0) {
            TodayView todayView = new TodayView(parent.getContext());
            todayView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(todayView);
        } else {
            ForecastView forecastView = new ForecastView(parent.getContext());
            forecastView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(forecastView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<FiveDayForecast> day = new ArrayList<>();
        if (holder.getItemViewType() == 0) {
            bindViews(holder, day, BIND_TODAY);
        } else {
            bindViews(holder, day, BIND_FORECAST);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }


    @Override
    public int getItemCount() {
        return FORECAST_DAYS;
    }

    private void bindViews(ViewHolder holder, ArrayList<FiveDayForecast> day, int bindType) {
        day = ResultParser.getForecastDay(fiveDayForecasts);
        BindWeatherAdapter binder = new BindWeatherAdapter(day, holder);
        if(bindType == BIND_TODAY) {
            binder.bindToday();
        } else {
            binder.bindForecast(day);
        }
    }
}