package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Utils.DateHandler;
import com.exeter.ecm2425.morecast.Views.TodayView;
import com.exeter.ecm2425.morecast.Views.ViewHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Binds the data to a TodayView which is housed in the DetailedActivity.
 * The binding process is slightly different to the binding to a WeatherAdapter
 * in that the binding happens directly to the views and not to ViewHolders.
 *
 * @author 640010970
 * @version 2.0.0
 */
public class BindDetailedView {

    // The detailed forecast and view for the binding process.
    private ArrayList<FiveDayForecast> detailedForecast;
    private TodayView detailedView;

    /**
     * Constructor to create a detailed forecast binder for
     * the DetailedActivity.
     * @param selectedForecast The forecast that was clicked by the user.
     * @param view The TodayView to bind data to.
     */
    public BindDetailedView(ArrayList<FiveDayForecast> selectedForecast, TodayView view) {
        this.detailedForecast = selectedForecast;
        this.detailedView = view;
    }

    /**
     * Entry method to binding detailed data.
     */
    public void bindDetailedData() {
        bindMainInformation();
        bindAdditionalInformation();
        bindImageToday();
        bindImageLabels();
    }

    /**
     * Binds the main information to the detailed forecast.
     */
    private void bindMainInformation() {
        // Get the 12pm UTC timestamp.
        FiveDayForecast midDay = detailedForecast.get(4);

        double temp = midDay.getTemperature();
        String description = midDay.getDescription() + " - Data at 12pm UTC";
        String timeZone = midDay.getTimeZoneName();
        detailedView.setMainInfo(temp, description, timeZone);
    }

    /**
     * Binds the additional information to the detailed forecast,
     * for instance, pressure.
     */
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

    /**
     * Binds the weather icons to the detailed forecast based on
     * the weather codes supplied by the API.
     */
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

        // Gather the times and temperatures needed to decide which
        // icons to display.
        ArrayList<FiveDayForecast> times = new ArrayList<>();
        Collections.addAll(times, closestTime, secondTime, thirdTime, fourthTime);
        ArrayList<Double> temperatures = new ViewHelper().getTemperatures(times);

        detailedView.setImages(firstCode, secondCode, thirdCode, fourthCode,
                firstHour, secondHour, thirdHour, fourthHour, temperatures);
    }

    /**
     * Binds the correct labels for the icons in the detailed forecast. The
     * labels are time-stamps for different times of the day.
     */
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
