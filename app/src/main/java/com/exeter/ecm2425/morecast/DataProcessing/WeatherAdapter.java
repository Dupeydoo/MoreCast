package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;

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

    public WeatherAdapter(JSONObject json) {
        weatherJson = json;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        TextView textView;
        if(viewType == 0) {
            textView = (TextView) LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.today_view, parent, false);
        }

        else {
            textView = (TextView) LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.forecast_view, parent, false);
        }
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double temp = ResultParser.getTemp(weatherJson);
        holder.textView.setText(String.format(Locale.ENGLISH, "%f", temp));
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return weatherJson.length();
    }
}
