package com.exeter.ecm2425.morecast.Database;


import android.content.Context;

import com.exeter.ecm2425.morecast.API.APIException;
import com.exeter.ecm2425.morecast.DataProcessing.ForecastParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class AccessDatabase {

    public ArrayList<FiveDayForecast> save(String apiResult, Context context)
            throws APIException, IOException {
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
            } catch(JSONException jsonException) {
                throw new APIException(jsonException.getMessage());
            }
        }

        else {
            throw new APIException("API Data could not be parsed!");
        }
        return new ArrayList<>(Arrays.asList(forecast));
    }

    public ArrayList<FiveDayForecast> read(Context context) {
        FiveDayForecastDao forecastDao = MorecastDatabase.getMorecastDatabase(context)
                .getFiveDayForecastDao();
        return new ArrayList<>(forecastDao.getFiveDayForecast());
    }

}
