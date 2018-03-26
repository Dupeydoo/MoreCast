package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Utils.DateHandler;
import com.exeter.ecm2425.morecast.Views.TodayView;
import com.exeter.ecm2425.morecast.Views.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;

public class BindDetailedView {

    private ArrayList<FiveDayForecast> detailedForecast;
    private TodayView detailedView;

    public BindDetailedView(ArrayList<FiveDayForecast> selectedForecast, TodayView view) {
        this.detailedForecast = selectedForecast;
        this.detailedView = view;
    }

    public void bindDetailedData() {
        bindMainInformation();
        bindAdditionalInformation();
        bindImageToday();
        bindImageLabels();
    }

    private void bindMainInformation() {
        FiveDayForecast midDay = detailedForecast.get(4);
        double temp = midDay.getTemperature();
        String description = midDay.getDescription() + " - Data at 12pm UTC";
        String timeZone = midDay.getTimeZoneName();
        detailedView.setMainInfo(temp, description, timeZone);
    }

    private void bindAdditionalInformation() {
        FiveDayForecast midDay = detailedForecast.get(4);
        double pressure = midDay.getPressure();
        int humidity = midDay.getHumidity();
        double windSpeed = midDay.getWindSpeed();
        double windDirection = midDay.getWindDegree();
        String precipType = midDay.getPrecipitationType();
        double precipAmount = midDay.getPrecipitationAmount();
        detailedView.setAdditionalWeatherInfo(pressure, humidity,
                windSpeed, windDirection, precipType, precipAmount);
    }

    private void bindImageToday() {
        FiveDayForecast closestTime = detailedForecast.get(2);
        FiveDayForecast secondTime = detailedForecast.get(3);
        FiveDayForecast thirdTime = detailedForecast.get(4);
        FiveDayForecast fourthTime = detailedForecast.get(5);

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

        detailedView.setImages(firstCode, secondCode, thirdCode, fourthCode,
                firstHour, secondHour, thirdHour, fourthHour, temperatures);
    }

    private void bindImageLabels() {
        FiveDayForecast firstForecast = detailedForecast.get(2);
        FiveDayForecast secondForecast = detailedForecast.get(3);
        FiveDayForecast thirdForecast = detailedForecast.get(4);
        FiveDayForecast fourthForecast = detailedForecast.get(5);

        String firstTime = firstForecast.getDateTime();
        String secondTime = secondForecast.getDateTime();
        String thirdTime = thirdForecast.getDateTime();
        String fourthTime = fourthForecast.getDateTime();
        detailedView.setLabels(firstTime, secondTime, thirdTime, fourthTime);
    }
}
