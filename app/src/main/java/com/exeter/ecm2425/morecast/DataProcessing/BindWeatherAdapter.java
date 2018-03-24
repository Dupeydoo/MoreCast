package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Utils.DateHandler;
import com.exeter.ecm2425.morecast.Views.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class BindWeatherAdapter {
    private ArrayList<FiveDayForecast> dayForecast;
    private WeatherAdapter.ViewHolder viewHolder;


    public BindWeatherAdapter
            (ArrayList<FiveDayForecast> forecasts, WeatherAdapter.ViewHolder holder) {
        this.dayForecast = forecasts;
        this.viewHolder = holder;
    }

    void bindToday() {
        bindMainInformation();
        bindAdditionalInformation();
        bindImageToday();
        bindLabels();
    }

    void bindForecast(ArrayList<FiveDayForecast> day) {
        bindForecastInformation();
        bindImageForecasts();
        bindDayForecast(day);
    }

    private void bindMainInformation() {
        FiveDayForecast closestTime = dayForecast.get(0);
        double temp = closestTime.getTemperature();
        String description = closestTime.getDescription();
        viewHolder.todayView.setMainInfo(temp, description);
    }

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

    private void bindForecastInformation() {
        FiveDayForecast midDay = dayForecast.get(4);
        long timestamp = midDay.getEpochTime();
        Date currentDate = new Date(timestamp * 1000);

        String currentDay = DateHandler.returnDayOfTheWeek(currentDate);
        double temp = midDay.getTemperature();
        viewHolder.forecastView.setForecast(currentDay, temp);
    }

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

        ArrayList<FiveDayForecast> times = new ArrayList<>();
        Collections.addAll(times, closestTime, secondTime, thirdTime, fourthTime);
        ArrayList<Double> temperatures = new ViewHelper().getTemperatures(times);

        viewHolder.todayView.setImages(firstCode, secondCode, thirdCode, fourthCode,
                firstHour, secondHour, thirdHour, fourthHour, temperatures);
    }

    private void bindImageForecasts() {
        FiveDayForecast midDay = dayForecast.get(4);
        int code = midDay.getWeatherCode();
        double temperature = midDay.getTemperature();
        viewHolder.forecastView.setForecastImage(code, 12, temperature);
    }

    private void bindLabels() {
        FiveDayForecast firstForecast = dayForecast.get(0);
        FiveDayForecast secondForecast = dayForecast.get(1);
        FiveDayForecast thirdForecast = dayForecast.get(2);
        FiveDayForecast fourthForecast = dayForecast.get(3);

        String firstTime = firstForecast.getDateTime();
        String secondTime = secondForecast.getDateTime();
        String thirdTime = thirdForecast.getDateTime();
        String fourthTime = fourthForecast.getDateTime();
        viewHolder.todayView.setLabels(firstTime, secondTime, thirdTime, fourthTime);
    }

    private void bindDayForecast(ArrayList<FiveDayForecast> dayForecasts) {
        viewHolder.forecastView.setDayForecasts(dayForecasts);
    }
}
