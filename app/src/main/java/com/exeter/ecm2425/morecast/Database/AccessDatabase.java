package com.exeter.ecm2425.morecast.Database;


import android.content.Context;

import com.exeter.ecm2425.morecast.DataProcessing.ResultParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AccessDatabase {
    public void save(String apiResult, Context context) {
        if(apiResult != null && !apiResult.isEmpty()) {
            try {
                JSONObject result = new JSONObject(apiResult);
                FiveDayForecast[] forecast = parseWeatherData(result);
                FiveDayForecastDao forecastDao = MorecastDatabase.getMorecastDatabase(context)
                        .getFiveDayForecastDao();
                forecastDao.destroyTable();
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
        forecast.setDateTime(ResultParser.getDateTime(currentForecastData));
        forecast.setWeatherCode(ResultParser.getWeatherId(currentForecastData));
        return forecast;
    }
}