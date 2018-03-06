package com.exeter.ecm2425.morecast.DataProcessing;


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
            return weatherJson.getJSONObject("main").getDouble("temp");
        }

        catch(JSONException e) {
            // log
            return 0;
        }
    }
}
