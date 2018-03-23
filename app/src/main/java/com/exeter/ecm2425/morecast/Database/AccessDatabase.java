package com.exeter.ecm2425.morecast.Database;


import android.content.Context;

import com.exeter.ecm2425.morecast.DataProcessing.ForecastParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class AccessDatabase {

    public ArrayList<FiveDayForecast> save(String apiResult, Context context) {
        FiveDayForecast[] forecast;
        if(apiResult != null && !apiResult.isEmpty()) {
            try {
                JSONObject result = new JSONObject(apiResult);
                ForecastParser parser = new ForecastParser();
                parser.parseWeatherData(result);

                FiveDayForecastDao forecastDao = MorecastDatabase.getMorecastDatabase(context)
                        .getFiveDayForecastDao();

                forecast = parser.getFiveDayForecasts();
                forecastDao.destroyTable();
                forecastDao.insertFiveDayForecast(forecast);
            } catch(JSONException e) {
                forecast = new FiveDayForecast[] { };
            }
        }

        else {
            forecast = new FiveDayForecast[] { };
        }
        return new ArrayList<>(Arrays.asList(forecast));
    }


}
