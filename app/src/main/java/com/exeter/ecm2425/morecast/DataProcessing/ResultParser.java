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
            // log
            jsonResult = new JSONObject();
        }
        return jsonResult;
    }

    public static double getTemp(JSONObject weatherJson, int position) {
        try {
            JSONArray day = getDay(weatherJson);
            JSONObject forecast = day.getJSONObject(0);
            return forecast.getJSONObject("main").getDouble("temp");
        }

        catch(JSONException e) {
            return 0;
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
            System.out.println(e.getMessage() + "\n\n");
            for(int i = 0; i < e.getStackTrace().length; i++) {
                System.out.println(e.getStackTrace()[i]);
            }
            return null;
        }
        return dayForecast;
    }
}
