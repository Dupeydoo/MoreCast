package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
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
        public TextView textView;
        public TodayView todayView;
        public ForecastView forecastView;

        public ViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }

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
            return new ViewHolder(forecastView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONArray day;
        double temp;
        if(holder.getItemViewType() == 0) {
            day = ResultParser.getForecastDay(weatherJson, 0);
            bindMainInformation(day, holder);
            bindAdditionalInformation(day, holder);
        }

        else {
            day = ResultParser.getForecastDay(weatherJson, 0); // pos doesnt mean anything atm.
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
        try {
            JSONObject closestTime = day.getJSONObject(0);
            double temp = closestTime.getJSONObject("main").getDouble("temp");
            String description = closestTime.getJSONArray("weather")
                    .getJSONObject(0).getString("description");
            holder.todayView.setMainInfo(temp, description);
        }

        catch(JSONException e) {
            System.out.println("Well this went wrong somehow.");
        }
    }

    private void bindAdditionalInformation(JSONArray day, @NonNull ViewHolder holder) {
        try {
            // Put in resultparser
            JSONObject closestTime = day.getJSONObject(0);
            double pressure = closestTime.getJSONObject("main").getDouble("pressure");
            int humididty = closestTime.getJSONObject("main").getInt("humidity");

            double windSpeed = closestTime.getJSONObject("wind").getDouble("speed");
            double windDirection = closestTime.getJSONObject("wind").getDouble("deg");

            String precipType = "Rain";
            double precipAmount = 2.0;

            holder.todayView.setAdditionalWeatherInfo(
                    pressure, humididty, windSpeed, windDirection, precipType, precipAmount);
        }

        catch(JSONException e) {
            System.out.println("Wrong!!");
        }
    }

    private void bindForecastInformation(JSONArray day, @NonNull ViewHolder holder) {
        try {
            JSONObject midDay = day.getJSONObject(4);
            long timestamp = midDay.getLong("dt");
            Date currentDate = new Date(timestamp * 1000);

            String currentDay = DateHandler.returnDayOfTheWeek(currentDate);
            double temp = midDay.getJSONObject("main").getDouble("temp");
            holder.forecastView.setForecast(currentDay, temp);
        }

        catch(JSONException e) {
            System.out.println("I really hate how many try catches you need for JSON parsing");
        }
    }
}
