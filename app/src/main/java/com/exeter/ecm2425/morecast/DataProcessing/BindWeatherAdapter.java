package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Utils.DateHandler;
import com.exeter.ecm2425.morecast.Views.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Binds the data to a TodayView which is the first item of the MainActivity
 * RecyclerView. The binding process occurs on ViewHolder objects that are
 * contained in the RecyclerView.
 *
 * @author 640010970
 * @version 2.2.0
 */
public class BindWeatherAdapter {
    private ArrayList<FiveDayForecast> dayForecast;
    private WeatherAdapter.ViewHolder viewHolder;

    /**
     * Constructs a new binder to bind data to a WeatherAdapter's
     * ViewHolders.
     * @param forecasts The forecast data to bind to the RecyclerView.
     * @param holder The current holder to bind to.
     */
    public BindWeatherAdapter
            (ArrayList<FiveDayForecast> forecasts, WeatherAdapter.ViewHolder holder) {
        this.dayForecast = forecasts;
        this.viewHolder = holder;
    }

    /**
     * Entry method to binding today's forecast data.
     */
    void bindToday() {
        bindMainInformation();
        bindAdditionalInformation();
        bindImageToday();
        bindLabels();
    }

    /**
     * Entry method to bind the next four days of forecast
     * data to the RecyclerView.
     * @param day The day forecasts used to bind data.
     */
    void bindForecast(ArrayList<FiveDayForecast> day) {
        bindForecastInformation();
        bindImageForecasts();
        bindDayForecast(day);
    }

    /**
     * Binds the main information to the RecyclerView TodayView.
     */
    private void bindMainInformation() {
        FiveDayForecast closestTime = dayForecast.get(0);
        double temp = closestTime.getTemperature();
        String description = closestTime.getDescription();
        String timeZone = closestTime.getTimeZoneName();
        viewHolder.todayView.setMainInfo(temp, description, timeZone);
    }

    /**
     * Binds the additional information to the TodayView,
     * for instance, pressure.
     */
    private void bindAdditionalInformation() {
        FiveDayForecast closestTime = dayForecast.get(0);
        double pressure = closestTime.getPressure();
        int humidity = closestTime.getHumidity();
        double windSpeed = closestTime.getWindSpeed();
        double windDirection = closestTime.getWindDegree();
        String precipType = closestTime.getPrecipitationType();
        double precipAmount = closestTime.getPrecipitationAmount();

        viewHolder.todayView.setAdditionalWeatherInfo(
                pressure, humidity, windSpeed, windDirection, precipType, precipAmount
        );
    }

    /**
     * Binds the simple forecast data to the smaller ForecastViews
     * in the RecyclerView. 12pm UTC is taken as the time to display
     * weather data.
     */
    private void bindForecastInformation() {
        FiveDayForecast midDay = dayForecast.get(4);

        // Get the epoch time-stamp to decide which day
        // of the week the forecast is.
        long timestamp = midDay.getEpochTime();
        Date currentDate = new Date(timestamp);

        String currentDay = DateHandler.returnDayOfTheWeek(currentDate);
        double temp = midDay.getTemperature();
        viewHolder.forecastView.setForecast(currentDay, temp);
    }

    /**
     * Binds the weather icons to the TodayView based on
     * the weather codes supplied by the API.
     */
    private void bindImageToday() {
        FiveDayForecast closestTime = dayForecast.get(0);
        FiveDayForecast secondTime = dayForecast.get(1);
        FiveDayForecast thirdTime = dayForecast.get(2);
        FiveDayForecast fourthTime = dayForecast.get(3);

        int firstCode = closestTime.getWeatherCode();
        int secondCode = secondTime.getWeatherCode();
        int thirdCode = thirdTime.getWeatherCode();
        int fourthCode = fourthTime.getWeatherCode();

        int firstHour = DateHandler.getHour(closestTime.getDateTime());
        int secondHour = DateHandler.getHour(secondTime.getDateTime());
        int thirdHour = DateHandler.getHour(thirdTime.getDateTime());
        int fourthHour = DateHandler.getHour(fourthTime.getDateTime());

        // Retrieve the temperatures and times required to decide which icons
        // to display.
        ArrayList<FiveDayForecast> times = new ArrayList<>();
        Collections.addAll(times, closestTime, secondTime, thirdTime, fourthTime);
        ArrayList<Double> temperatures = new ViewHelper().getTemperatures(times);

        viewHolder.todayView.setImages(firstCode, secondCode, thirdCode, fourthCode,
                firstHour, secondHour, thirdHour, fourthHour, temperatures);
    }

    /**
     * Binds the weather icons required for the upcoming forecasts in the
     * MainActivity RecyclerView.
     */
    private void bindImageForecasts() {
        FiveDayForecast midDay = dayForecast.get(4);
        int code = midDay.getWeatherCode();
        double temperature = midDay.getTemperature();
        viewHolder.forecastView.setForecastImage(code, 12, temperature);
    }

    /**
     * Binds the corresponding labels for the weather icons in the TodayView
     * that is displayed as the first item in the RecyclerView.
     */
    private void bindLabels() {
        FiveDayForecast firstForecast = dayForecast.get(0);
        FiveDayForecast secondForecast = dayForecast.get(1);
        FiveDayForecast thirdForecast = dayForecast.get(2);
        FiveDayForecast fourthForecast = dayForecast.get(3);

        String firstTime = firstForecast.getDateTime();
        String secondTime = secondForecast.getDateTime();
        String thirdTime = thirdForecast.getDateTime();
        String fourthTime = fourthForecast.getDateTime();

        ArrayList<FiveDayForecast> times = new ArrayList<>();
        Collections.addAll(times, firstForecast, secondForecast, thirdForecast, fourthForecast);
        ArrayList<Double> temperatures = new ViewHelper().getTemperatures(times);

        viewHolder.todayView.setLabels(firstTime, secondTime, thirdTime, fourthTime, temperatures);
    }

    /**
     * Binds the current day's worth of forecast data to the ForecastView
     * that is currently being bound so that it may be passed to the DetailedActivity
     * when a list item is clicked.
     * @param dayForecasts The forecast data for a day.
     */
    private void bindDayForecast(ArrayList<FiveDayForecast> dayForecasts) {
        viewHolder.forecastView.setDayForecasts(dayForecasts);
    }
}
