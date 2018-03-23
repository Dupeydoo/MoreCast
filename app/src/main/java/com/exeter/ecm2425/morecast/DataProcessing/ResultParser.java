package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.Database.FiveDayForecast;

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

    public JSONObject parseResult() {
        JSONObject jsonResult;
        try {
             jsonResult = new JSONObject(preParsed);
        }

        catch(JSONException e) {
            jsonResult = new JSONObject();
        }
        return jsonResult;
    }

    public static ArrayList<FiveDayForecast> getForecastDay(ArrayList<FiveDayForecast> forecast) {
        ArrayList<FiveDayForecast> day = getDay(forecast);
        return day;
    }

    public static JSONObject getTimestamp(JSONArray day, int index) {
        try {
            return day.getJSONObject(index);
        } catch(JSONException e) {
            return null;
        }
    }

    public static double getDoubleFromJson
            (JSONObject jsonObject, String innerObject, String jsonDouble) {
        try {
            return jsonObject.getJSONObject(innerObject).getDouble(jsonDouble);
        } catch(JSONException e) {
            return 0.0;
        }
    }

    public static int getIntFromJson(JSONObject jsonObject, String innerObject, String jsonInt) {
        try {
            return jsonObject.getJSONObject(innerObject).getInt(jsonInt);
        } catch(JSONException e) {
            return 0;
        }
    }

    public static String checkPrecipitationType(JSONObject jsonObject) {
        try {
            JSONObject snow = jsonObject.getJSONObject("snow");
            return "Snow";
        } catch(JSONException e) {
            return "Rain";
        }
    }

    public static double getPrecipitationAmount(JSONObject jsonObject, String precipType) {
        String preType = precipType.toLowerCase();
        try {
            return jsonObject.getJSONObject(preType).getDouble("3h");
        } catch(JSONException e) {
            return 0.0;
        }
    }

    public static String getWeatherDescription(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        } catch(JSONException e) {
            return "Description Unavailable";
        }
    }

    public static Long getWeatherEpoch(JSONObject jsonObject) {
        try {
            return jsonObject.getLong("dt");
        } catch(JSONException e) {
            return 0L;
        }
    }

    public static String getDateTime(JSONObject jsonObject) {
        return jsonObject.optString("dt_txt");
    }

    public static String getWeatherDateTime(JSONObject jsonObject) {
        String dateTime = getDateTime(jsonObject);
        String simpleTime = dateTime.split("\\s+")[1];
        return simpleTime.substring(0, 5);
    }

    public static int getWeatherId(JSONObject time) {
        try {
            return time.getJSONArray("weather").getJSONObject(0).getInt("id");
        } catch(JSONException e) {
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
