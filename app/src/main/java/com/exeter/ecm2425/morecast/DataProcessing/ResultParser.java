package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.API.APIException;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Utils.DateHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultParser {

    private String preParsed;
    private static int parseIndex;

    public ResultParser(String preParsed) {
        this.preParsed = preParsed;
        parseIndex = 0;
    }

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

    public static ArrayList<FiveDayForecast> getForecastDay(ArrayList<FiveDayForecast> forecast) {
        ArrayList<FiveDayForecast> day = getDay(forecast);
        return day;
    }

    public static double getDoubleFromJson
            (JSONObject jsonObject, String innerObject, String jsonDouble) {
        try {
            return jsonObject.getJSONObject(innerObject).getDouble(jsonDouble);
        } catch(JSONException jsonException) {
            return 0.0;
        }
    }

    public static int getIntFromJson(JSONObject jsonObject, String innerObject, String jsonInt) {
        try {
            return jsonObject.getJSONObject(innerObject).getInt(jsonInt);
        } catch(JSONException jsonException) {
            return 0;
        }
    }

    public static String checkPrecipitationType(JSONObject jsonObject) {
        try {
            JSONObject snow = jsonObject.getJSONObject("snow");
            return "Snow";
        } catch(JSONException jsonException) {
            return "Rain";
        }
    }

    public static double getPrecipitationAmount(JSONObject jsonObject, String precipType) {
        String preType = precipType.toLowerCase();
        try {
            return jsonObject.getJSONObject(preType).getDouble("3h");
        } catch(JSONException jsonException) {
            return 0.0;
        }
    }

    public static String getWeatherDescription(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        } catch(JSONException jsonException) {
            return "Description Unavailable";
        }
    }

    public static Long getWeatherEpoch(JSONObject jsonObject) {
        try {
            return jsonObject.getLong("dt");
        } catch(JSONException jsonException) {
            return DateHandler.getDeviceEpochTime();
        }
    }

    public static String getDateTime(JSONObject jsonObject) {
        return jsonObject.optString("dt_txt");
    }

    public static int getWeatherId(JSONObject time) {
        try {
            return time.getJSONArray("weather").getJSONObject(0).getInt("id");
        } catch(JSONException jsonException) {
            return 800;
        }
    }

    private static ArrayList<FiveDayForecast> getDay(ArrayList<FiveDayForecast> forecast) {
        ArrayList<FiveDayForecast> dayForecast = new ArrayList<>();
        dayForecast.add(forecast.get(parseIndex));
        parseIndex++;

        for(int i = parseIndex; i < forecast.size(); i++) {
            FiveDayForecast currentForecastObj = forecast.get(i);
            if(currentForecastObj.getDateTime().contains("00:00:00")) {
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
