package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Views.TodayView;

import org.json.JSONObject;

import java.util.Locale;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private JSONObject weatherJson;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }

    public static class TodayViewHolder extends RecyclerView.ViewHolder {
        public TodayView view;
        public TodayViewHolder(View view) {
            super(view);
            view = (TodayView) view;
        }
    }

    public WeatherAdapter(JSONObject json) {
        weatherJson = json;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        TextView textView;
        if(viewType == 0) {
            // Do something custom;
        }

        else {
            textView = (TextView) LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.forecast_view, parent, false);
            return new ViewHolder(textView);
        }
        return new ViewHolder(null);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double temp;
        if(holder.getItemViewType() == 0) {
            // temp = ResultParser.getTemp(weatherJson, position);
        }

        else {
            temp = ResultParser.getTemp(weatherJson, 7);
            holder.textView.setText(String.format(Locale.ENGLISH, "%f", temp));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 0;
        return 1;
    }

    // its using this to determine how many to display, im getting lucky basically lol.
    @Override
    public int getItemCount() {
        return weatherJson.length();
    }
}
