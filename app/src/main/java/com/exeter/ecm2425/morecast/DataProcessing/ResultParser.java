package com.exeter.ecm2425.morecast.DataProcessing;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultParser {

    private String preParsed;

    public ResultParser(String preParsed) {
        this.preParsed = preParsed;
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

    public static double getTemp(JSONObject weatherJson) {
        try {
            JSONArray data = weatherJson.getJSONArray("list");
            JSONObject forecast = data.getJSONObject(0);
            return forecast.getJSONObject("main").getDouble("temp");
        }

        catch(JSONException e) {
            System.out.println(e.getMessage() + "\n\n");
            for(int i = 0; i < e.getStackTrace().length; i++) {
                System.out.println(e.getStackTrace()[i]);
            }
            return 0;
        }
    }
}
