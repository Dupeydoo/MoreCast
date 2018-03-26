package com.exeter.ecm2425.morecast.Database;


import android.content.Context;

import com.exeter.ecm2425.morecast.API.APIException;
import com.exeter.ecm2425.morecast.DataProcessing.ForecastParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Provides access to the data access objects needed to query the database
 * directly. All classes that need access to the DAO's should call to this
 * class first. The methods in this calls should only ever be called on a
 * separate thread from the UI thread.
 *
 * @author 640010970
 * @version 3.0.0
 */
public class AccessDatabase {

    /**
     * Saves data to the database and invokes the ForecastParser to prepare
     * the API data for binding and storing.
     * @param apiResult The String containing the API result.
     * @param context The current context invoking the save method.
     * @return ArrayList<FiveDayForecast> The FiveDayForecast data.
     * @throws APIException If parsing or processing should fail.
     * @throws IOException If the time zones API call fails.
     */
    public ArrayList<FiveDayForecast> save(String apiResult, Context context)
            throws APIException, IOException {
        FiveDayForecast[] forecast;

        // If the result is correct then being parsing.
        if(apiResult != null && !apiResult.isEmpty()) {
            try {
                JSONObject result = new JSONObject(apiResult);

                // Parse the data into the required representation.
                ForecastParser parser = new ForecastParser();
                parser.parseWeatherData(result);

                // Call the DAO's to insert data and clean up data that was stored
                // previously.
                FiveDayForecastDao forecastDao =
                        MorecastDatabase.getMorecastDatabase(context).getFiveDayForecastDao();

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

    /**
     * Calls the data access object to read the forecast data stored in the database.
     * @param context The context requesting the data.
     * @return ArrayList<FiveDayForecast> The forecast data stored in the database.
     */
    public ArrayList<FiveDayForecast> read(Context context) {
        FiveDayForecastDao forecastDao = MorecastDatabase.getMorecastDatabase(context)
                .getFiveDayForecastDao();
        return new ArrayList<>(forecastDao.getFiveDayForecast());
    }

}
