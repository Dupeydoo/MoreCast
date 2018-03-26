package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.API.APIException;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Utils.DateHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ResultParser provides a number of static methods
 * to parse JSON that is returned from the API.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class ResultParser {

    private String preParsed;

    // Used in the day selection algorithm.
    protected static int parseIndex;

    /**
     * Default Constructor.
     */
    public ResultParser() {
        parseIndex = 0;
    }

    /**
     * Constructor to set a string of pre-parsed
     * API data.
     * @param preParsed A String of API response data.
     */
    public ResultParser(String preParsed) {
        this.preParsed = preParsed;
        parseIndex = 0;
    }

    /**
     * Parses the JSON string returned by the API.
     * @return JSONObject containing API weather data.
     * @throws APIException On parsing failure.
     */
    public JSONObject parseResult() throws APIException {
        JSONObject jsonResult;
        try {
             jsonResult = new JSONObject(preParsed);
        }

        catch(JSONException jsonException) {
            throw new APIException("API Result was empty!");
        }
        return jsonResult;
    }

    /**
     * Entry method to get the current forecast day.
     * @param forecast The five day forecast to split into
     *                 a day's worth of data.
     * @return ArrayList<FiveDayForecast> A single day of forecast data.
     */
    public static ArrayList<FiveDayForecast> getForecastDay
            (ArrayList<FiveDayForecast> forecast) {
        ArrayList<FiveDayForecast> day = getDay(forecast);
        return day;
    }

    /**
     * Retrieves a double from a specified object within a JSON object.
     * Used mostly to retrieve data like pressure or wind speed.
     * @param jsonObject The top level JSON object.
     * @param innerObject The inner object to inspect for a double.
     * @param jsonDouble The String denoting which value to return.
     * @return double The desired double, or 0.0 as a default.
     */
    public static double getDoubleFromJson
            (JSONObject jsonObject, String innerObject, String jsonDouble) {
        try {
            return jsonObject.getJSONObject(innerObject).getDouble(jsonDouble);
        } catch(JSONException jsonException) {
            return 0.0;
        }
    }

    /**
     * Retrieves an int from a specified object within a JSON object.
     * Used mostly to retrieve data like humidity.
     * @param jsonObject The top level JSON object.
     * @param innerObject The inner object to inspect for a double.
     * @param jsonInt The String denoting which value to return.
     * @return double The desired int, or 0 as a default.
     */
    public static int getIntFromJson(JSONObject jsonObject, String innerObject, String jsonInt) {
        try {
            return jsonObject.getJSONObject(innerObject).getInt(jsonInt);
        } catch(JSONException jsonException) {
            return 0;
        }
    }

    /**
     * Checks what precipitation type to display in the forecast. If
     * snow can be retrieved it takes precedence over rain.
     * @param jsonObject The JSONObject to retrieve a snow object from.
     * @return String "Snow" if it exists and "Rain" if not.
     */
    public static String checkPrecipitationType(JSONObject jsonObject) {
        try {
            JSONObject snow = jsonObject.getJSONObject("snow");
            return "Snow";
        } catch(JSONException jsonException) {
            return "Rain";
        }
    }

    /**
     * Retrieves the amount of precipitation that has fallen in the past 3 hours.
     * @param jsonObject The JSONObject to search,
     * @param precipType The type of precipitation to get an amount for. Precipitation
     *                   is measured in millimetres (mm).
     * @return
     */
    public static double getPrecipitationAmount(JSONObject jsonObject, String precipType) {
        String preType = precipType.toLowerCase();
        try {
            return jsonObject.getJSONObject(preType).getDouble("3h");
        } catch(JSONException jsonException) {
            return 0.0;
        }
    }

    /**
     * Retrieve the description of the weather at a time-stamp.
     * @param jsonObject JSONObject to parse.
     * @return String The description, or default text if unavailable.
     */
    public static String getWeatherDescription(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        } catch(JSONException jsonException) {
            return "Description Unavailable";
        }
    }

    /**
     * Returns the epoch time from a weather JSON object.
     * @param jsonObject The JSONObject to parse.
     * @return The time-stamp, or the device timestamp if
     *         not possible.
     */
    public static Long getWeatherEpoch(JSONObject jsonObject) {
        try {
            return jsonObject.getLong("dt");
        } catch(JSONException jsonException) {
            return DateHandler.getDeviceEpochTime();
        }
    }

    /**
     * Retrieves the weather code of the current weather.
     * @param time A time-stamp which contains a weather code.
     * @return The weather code, or 800 (clear skies) as a default.
     */
    public static int getWeatherId(JSONObject time) {
        try {
            return time.getJSONArray("weather").getJSONObject(0).getInt("id");
        } catch(JSONException jsonException) {
            return 800;
        }
    }

    /**
     * Helper method which returns the day using an algorithm that searches for
     * 00:00:00 UTC time.
     * @param forecast The forecast data to split into a day.
     * @return ArrayList<FiveDayForecast> Forecasts for a single day.
     */
    private static ArrayList<FiveDayForecast> getDay(ArrayList<FiveDayForecast> forecast) {
        ArrayList<FiveDayForecast> dayForecast = new ArrayList<>();

        // Add the first element. Prevents one time-stamp long days at night.
        dayForecast.add(forecast.get(parseIndex));
        parseIndex++;

        // Loop through the forecasts until the UTC date time is 00:00:00
        // the start of a new day.
        for(int i = parseIndex; i < forecast.size(); i++) {
            FiveDayForecast currentForecastObj = forecast.get(i);
            if(currentForecastObj.getUtcDateTime().contains("00:00:00")) {

                // Add a minimum of three additional for when displaying
                // the mini icon and label forecasts for the detailed forecast.
                dayForecast.add(forecast.get(i));
                dayForecast.add(forecast.get(i + 1));
                dayForecast.add(forecast.get(i + 2));
                break;
            }
            dayForecast.add(currentForecastObj);
            parseIndex++;
        }
        return dayForecast;
    }
}
