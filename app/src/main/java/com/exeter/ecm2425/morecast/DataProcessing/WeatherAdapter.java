package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Views.ForecastView;
import com.exeter.ecm2425.morecast.Views.TodayView;

import java.util.ArrayList;

/**
 * The WeatherAdapter is the central binding utility to the ReycyclerView that
 * displays forecast data in the MainActivity.
 *
 * @author 64001970
 * @version 2.4.0
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    // The data to be bound to the Views.
    private ArrayList<FiveDayForecast> fiveDayForecasts;

    // A collection of each day's forecasts that is updated when
    // The RecyclerView first binds data.
    private ArrayList<ArrayList<FiveDayForecast>> currentForecasts;

    // The number of days to show forecast data for.
    private final static int FORECAST_DAYS = 5;

    // A constant to represent a signal to bind a TodayView.
    private final static int BIND_TODAY = 0;

    // A constant to represent a signal to bind a ForecastView.
    private final static int BIND_FORECAST = 1;

    // A counter to count first binds before the RecyclerView should
    // start rebinding data from currentForecasts.
    private int binderCounter = 0;

    /**
     * The ViewHolder class used in the RecyclerView to hold the
     * individual list items.
     *
     * @author 640010970
     * @version 1.1.0
     */
    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TodayView todayView;
        public ForecastView forecastView;

        /**
         * Constructor to use a TodayView list item.
         * @param todayView The TodayView to bind to.
         */
        public ViewHolder(TodayView todayView) {
            super(todayView);
            this.todayView = todayView;
        }

        /**
         * Constructor to use a ForecastView list item.
         * @param forecastView The ForecastView to bind to.
         */
        public ViewHolder(ForecastView forecastView) {
            super(forecastView);
            this.forecastView = forecastView;
        }
    }


    /**
     * WeatherAdapter constructor.
     * @param forecasts The forecast data to display.
     */
    public WeatherAdapter(ArrayList<FiveDayForecast> forecasts) {
        fiveDayForecasts = forecasts;
        currentForecasts = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * Creates the TodayView and ForecastView Holders to bind data to.
     * @return WeatherAdapter.ViewHolder The view holders to put into the RecyclerView.
     */
    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                        int viewType) {
        // Create the first item to display detailed forecast data.
        if (viewType == 0) {
            TodayView todayView = new TodayView(parent.getContext());

            // Set the views layout dynamically. This needs to be done with
            // RecyclerViews to prevent layout bugs.
            todayView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(todayView);
        } else {
            // Otherwise show weather information for the next few days with a different view.
            ForecastView forecastView = new ForecastView(parent.getContext());
            forecastView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(forecastView);
        }
    }

    /**
     * {@inheritDoc}
     * Binds data to the viewholders in the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<FiveDayForecast> day = new ArrayList<>();

        // If its still the first data binding.
        if(binderCounter < getItemCount()) {
            rebindViews(holder, day, position);
            binderCounter++;
        } else {
            // Otherwise use existing data to bind.
            rebindViews(holder, null, position);
        }
    }

    /**
     * {@inheritDoc}
     * Differentiates between view items.
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }

    /**
     * {@inheritDoc}
     * Called by the layout manager to establish how many
     * ViewHolders to create.
     */
    @Override
    public int getItemCount() {
        return FORECAST_DAYS;
    }


    /**
     * Helper method to retrieve the current forecast day and use it to
     * prepare information for the WeatherAdapter.
     * @param holder The ViewHolder to bind data to.
     * @param day The forecast day to set.
     * @param bindType The type of binding to perform, Today, or the next four
     *                 days.
     * @param position The position of the current RecyclerView item.
     */
    private void bindViews
            (ViewHolder holder, ArrayList<FiveDayForecast> day, int bindType, int position) {
        if(day.isEmpty()) {
            day = ResultParser.getForecastDay(fiveDayForecasts);
            currentForecasts.add(day);
        }

        BindWeatherAdapter binder = new BindWeatherAdapter(day, holder);

        // Bind the first list item with today's weather.
        if(bindType == BIND_TODAY) {
            binder.bindToday();
        } else {
            // Otherwise bind the other
            // list items with the relevant day's weather.
            binder.bindForecast(day);
        }
    }

    /**
     * Rebinds the data if a ViewHolder is detached while the user is scrolling.
     * @param holder The ViewHolder to bind data to.
     * @param day The forecast day to set.
     * @param position The position of the current RecyclerView item.
     */
    private void rebindViews(ViewHolder holder, ArrayList<FiveDayForecast> day, int position) {
        if(holder.getItemViewType() == 0) {
            if(day == null) {
                day = currentForecasts.get(0);
            }
            bindViews(holder, day, BIND_TODAY, position);
        } else {
            if(day == null) {
                day = currentForecasts.get(position);
            }
            bindViews(holder, day, BIND_FORECAST, position);
        }
    }
}