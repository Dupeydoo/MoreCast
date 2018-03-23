package com.exeter.ecm2425.morecast.Database;


import android.arch.persistence.room.Room;
import android.content.Context;

import com.exeter.ecm2425.morecast.DataProcessing.ResultParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AccessDatabase {
    private MorecastDatabase database;

    public void setDatabase(Context context) {
        this.database = Room.databaseBuilder(context,
                MorecastDatabase.class, "MorecastDatabase").build();
    }

    public void closeDatabase() {
        this.database.close();
    }

    public MorecastDatabase getDatabase() {
        return this.database;
    }

    public void save(String apiResult) {
        if(apiResult != null && !apiResult.isEmpty()) {
            try {
                JSONObject result = new JSONObject(apiResult);
                FiveDayForecast[] forecast = parseWeatherData(result);
                FiveDayForecastDao forecastDao = database.getFiveDayForecastDao();
                forecastDao.insertFiveDayForecast(forecast);
            } catch(JSONException e) {
                System.out.println("Do something");
            }
        }

        else {
            // cant save to databae
        }
    }

    private FiveDayForecast[] parseWeatherData(JSONObject result) {
        JSONArray forecasts = result.optJSONArray("list");
        FiveDayForecast[] fiveDayForecast = new FiveDayForecast[forecasts.length()];
        for(int i = 0; i < forecasts.length(); i++) {
            JSONObject currentForecastData = forecasts.optJSONObject(i);
            FiveDayForecast forecast = populateFiveDayForecast(new FiveDayForecast(),
                    currentForecastData);
            fiveDayForecast[i] = forecast;
        }
        return fiveDayForecast;
    }

    private FiveDayForecast populateFiveDayForecast
            (FiveDayForecast forecast, JSONObject currentForecastData) {
        String precipitationType = ResultParser.checkPrecipitationType(currentForecastData);
        forecast.setEpochTime(ResultParser.getWeatherEpoch(currentForecastData));
        forecast.setDescription(ResultParser.getWeatherDescription(currentForecastData));
        forecast.setPressure(ResultParser.getDoubleFromJson(currentForecastData, "main", "pressure"));
        forecast.setTemperature(ResultParser.getDoubleFromJson(currentForecastData, "main", "temp"));
        forecast.setHumidity(ResultParser.getIntFromJson(currentForecastData, "main", "humidity"));
        forecast.setWindSpeed(ResultParser.getDoubleFromJson(currentForecastData, "wind", "speed"));
        forecast.setWindDegree(ResultParser.getDoubleFromJson(currentForecastData, "wind", "deg"));
        forecast.setPrecipitationType(precipitationType);
        forecast.setPrecipitationAmount(ResultParser.getPrecipitationAmount(currentForecastData,
                precipitationType));
        forecast.setDateTime(ResultParser.getWeatherDateTime(currentForecastData));
        forecast.setWeatherCode(ResultParser.getWeatherId(currentForecastData));
        return forecast;
    }
}
