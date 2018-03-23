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

    // get next day, if it cant bind the next four times, get n-1 position from next day.
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
            bindMainInformation(day, holder);
            bindAdditionalInformation(day, holder);
            bindImageToday(day, holder);
            bindLabels(day, holder);
        }

        else {
            day = ResultParser.getForecastDay(fiveDayForecasts);
            bindForecastInformation(day, holder);
            bindImageForecasts(day, holder);
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


    private void bindMainInformation(ArrayList<FiveDayForecast> day, @NonNull ViewHolder holder) {
        FiveDayForecast closestTime = day.get(0);
        double temp = closestTime.getTemperature();
        String description = closestTime.getDescription();
        holder.todayView.setMainInfo(temp, description);
    }

    private void bindAdditionalInformation(ArrayList<FiveDayForecast> day, @NonNull ViewHolder holder) {
        FiveDayForecast closestTime = day.get(0);
        double pressure = closestTime.getPressure();
        int humidity = closestTime.getHumidity();
        double windSpeed = closestTime.getWindSpeed();
        double windDirection = closestTime.getWindDegree();
        String precipType = closestTime.getPrecipitationType();
        double precipAmount = closestTime.getPrecipitationAmount();

        holder.todayView.setAdditionalWeatherInfo(
                pressure, humidity, windSpeed, windDirection, precipType, precipAmount
        );
    }

    private void bindForecastInformation(ArrayList<FiveDayForecast> day, @NonNull ViewHolder holder) {
        FiveDayForecast midDay = day.get(4);
        long timestamp = midDay.getEpochTime();
        Date currentDate = new Date(timestamp * 1000);

        String currentDay = DateHandler.returnDayOfTheWeek(currentDate);
        double temp = midDay.getTemperature();
        holder.forecastView.setForecast(currentDay, temp);
    }

    private void bindImageToday(ArrayList<FiveDayForecast> day, @NonNull ViewHolder holder) {
        FiveDayForecast closestTime = day.get(0);
        FiveDayForecast secondTime = day.get(1);
        FiveDayForecast thirdTime = day.get(2);
        FiveDayForecast fourthTime = day.get(3);

        int firstCode = closestTime.getWeatherCode();
        int secondCode = secondTime.getWeatherCode();
        int thirdCode = thirdTime.getWeatherCode();
        int fourthCode = fourthTime.getWeatherCode();
        int currentHour = DateHandler.getLocaleHour();
        holder.todayView.setImages(firstCode, secondCode, thirdCode, fourthCode, currentHour);
    }

    private void bindImageForecasts(ArrayList<FiveDayForecast> day, @NonNull ViewHolder holder) {
        FiveDayForecast midDay = day.get(4);
        int code = midDay.getWeatherCode();
        int currentHour = DateHandler.getLocaleHour();
        holder.forecastView.setForecastImage(code, currentHour);
    }

    private void bindLabels(ArrayList<FiveDayForecast> day, @NonNull ViewHolder holder) {
        FiveDayForecast firstForecast = day.get(0);
        FiveDayForecast secondForecast = day.get(1);
        FiveDayForecast thirdForecast = day.get(2);
        FiveDayForecast fourthForecast = day.get(3);

        String firstTime = firstForecast.getDateTime();
        String secondTime = secondForecast.getDateTime();
        String thirdTime = thirdForecast.getDateTime();
        String fourthTime = fourthForecast.getDateTime();
        holder.todayView.setLabels(firstTime, secondTime, thirdTime, fourthTime);
    }
}
