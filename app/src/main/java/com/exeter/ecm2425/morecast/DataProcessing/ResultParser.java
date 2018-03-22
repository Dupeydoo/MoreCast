package com.exeter.ecm2425.morecast.DataProcessing;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

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

    public static JSONArray getForecastDay(JSONObject weatherJson) {
        JSONArray day = getDay(weatherJson);
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

    private static JSONArray getDay(JSONObject weatherJson) {
        JSONArray dayForecast = new JSONArray();

        try {
            JSONArray data = weatherJson.getJSONArray("list");
            dayForecast.put(data.getJSONObject(parseIndex));
            parseIndex++;

            for(int i = parseIndex; i < data.length(); i++) {
                JSONObject currentForecastObj = data.getJSONObject(i);
                if(currentForecastObj.getString("dt_txt").contains("00:00:00")) {
                    break;
                }
                dayForecast.put(currentForecastObj);
                parseIndex++;
            }
        }

        catch(JSONException e) {
            return null;
        }
        return dayForecast;
    }
}
