package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Views.TodayView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private JSONObject weatherJson;
    private final static int FORECAST_DAYS = 5;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TodayView todayView;
        public ViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }

        public ViewHolder(TodayView todayView) {
            super(todayView);
            this.todayView = todayView;
        }
    }


    public WeatherAdapter(JSONObject json) {
        weatherJson = json;
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
            TextView textView = (TextView) LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.forecast_view, parent, false);
            return new ViewHolder(textView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONArray day;
        double temp;
        if(holder.getItemViewType() == 0) {
            day = ResultParser.getForecastDay(weatherJson, 0);
        }

        else {
            day = ResultParser.getForecastDay(weatherJson, 7);
            try {
                temp = day.getJSONObject(0).getJSONObject("main").getDouble("temp");
            } catch(JSONException e) {
                temp = 0;
                System.out.println("oops");
            }
            holder.textView.setText(String.format(Locale.ENGLISH, "%f", temp));
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
