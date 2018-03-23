package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Utils.DateHandler;
import com.exeter.ecm2425.morecast.Views.ForecastView;
import com.exeter.ecm2425.morecast.Views.TodayView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private JSONObject weatherJson;
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


    public WeatherAdapter(JSONObject json) {
        weatherJson = json;
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
        JSONArray day;
        if(holder.getItemViewType() == 0) {
            day = ResultParser.getForecastDay(weatherJson);
            bindMainInformation(day, holder);
            bindAdditionalInformation(day, holder);
            bindImageForecasts(day, holder);
        }

        else {
            day = ResultParser.getForecastDay(weatherJson);
            bindForecastInformation(day, holder);
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


    private void bindMainInformation(JSONArray day, @NonNull ViewHolder holder) {
        JSONObject closestTime = ResultParser.getTimestamp(day, 0);
        double temp = ResultParser.getDoubleFromJson(closestTime, "main", "temp");

        String description = ResultParser.getWeatherDescription(closestTime);
        holder.todayView.setMainInfo(temp, description);
    }

    private void bindAdditionalInformation(JSONArray day, @NonNull ViewHolder holder) {
        JSONObject closestTime = ResultParser.getTimestamp(day, 0);
        double pressure = ResultParser.getDoubleFromJson(closestTime, "main", "pressure");
        int humididty = ResultParser.getIntFromJson(closestTime, "main", "humidity");

        double windSpeed = ResultParser.getDoubleFromJson(closestTime, "wind", "speed");
        double windDirection = ResultParser.getDoubleFromJson(closestTime, "wind", "deg");
        String precipType = ResultParser.checkPrecipitationType(closestTime);
        double precipAmount = ResultParser.getPrecipitationAmount(closestTime, precipType);

        holder.todayView.setAdditionalWeatherInfo(
                pressure, humididty, windSpeed, windDirection, precipType, precipAmount
        );
    }

    private void bindForecastInformation(JSONArray day, @NonNull ViewHolder holder) {
        JSONObject midDay = ResultParser.getTimestamp(day, 4);
        long timestamp = ResultParser.getWeatherEpoch(midDay);
        Date currentDate = new Date(timestamp * 1000);

        String currentDay = DateHandler.returnDayOfTheWeek(currentDate);
        double temp = ResultParser.getDoubleFromJson(midDay, "main", "temp");
        holder.forecastView.setForecast(currentDay, temp);
    }

    private void bindImageForecasts(JSONArray day, @NonNull ViewHolder holder) {
        JSONObject closestTime = ResultParser.getTimestamp(day, 0);
        JSONObject secondTime = ResultParser.getTimestamp(day, 1);
        JSONObject thirdTime = ResultParser.getTimestamp(day, 2);
        JSONObject fourthTime = ResultParser.getTimestamp(day, 3);

        int firstCode = ResultParser.getWeatherId(closestTime);
        int secondCode = ResultParser.getWeatherId(secondTime);
        int thirdCode = ResultParser.getWeatherId(thirdTime);
        int fourthCode = ResultParser.getWeatherId(fourthTime);
        int currentHour = DateHandler.getLocaleHour();
        holder.todayView.setImages(firstCode, secondCode, thirdCode, fourthCode, currentHour);
    }
}
