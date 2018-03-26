package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.Utils.DateHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ForecastParser {
    private FiveDayForecast[] fiveDayForecasts;
    // only ever invoked on a worker thread.
    private final static String TIMEZONE_KEY = "AIzaSyAr3TGv-W69DKMTsSSSHp38cZsAttsFZaw";
    public static String offsetEnd;

    public ForecastParser() {
        this.fiveDayForecasts = new FiveDayForecast[] { };
    }

    public FiveDayForecast[] getFiveDayForecasts() {
        return this.fiveDayForecasts;
    }

    public void parseWeatherData(JSONObject result) throws IOException, JSONException {
        JSONObject coords = result.optJSONObject("city").optJSONObject("coord");
        JSONArray forecasts = result.optJSONArray("list");
        FiveDayForecast[] fiveDayForecast = new FiveDayForecast[forecasts.length()];
        for(int i = 0; i < forecasts.length(); i++) {
            JSONObject currentForecastData = forecasts.optJSONObject(i);
            FiveDayForecast forecast = populateFiveDayForecast(new FiveDayForecast(),
                    currentForecastData, coords);
            fiveDayForecast[i] = forecast;
        }
        this.fiveDayForecasts = fiveDayForecast;
    }

    private FiveDayForecast populateFiveDayForecast
            (FiveDayForecast forecast, JSONObject currentForecastData, JSONObject coords)
            throws IOException, JSONException {
        String precipitationType = ResultParser.checkPrecipitationType(currentForecastData);
        forecast.setDescription(ResultParser.getWeatherDescription(currentForecastData));
        forecast.setPressure(ResultParser.getDoubleFromJson(currentForecastData, "main", "pressure"));
        forecast.setTemperature(ResultParser.getDoubleFromJson(currentForecastData, "main", "temp"));
        forecast.setHumidity(ResultParser.getIntFromJson(currentForecastData, "main", "humidity"));
        forecast.setWindSpeed(ResultParser.getDoubleFromJson(currentForecastData, "wind", "speed"));
        forecast.setWindDegree(ResultParser.getDoubleFromJson(currentForecastData, "wind", "deg"));
        forecast.setPrecipitationType(precipitationType);
        forecast.setPrecipitationAmount(ResultParser.getPrecipitationAmount(currentForecastData,
                precipitationType));
        forecast.setWeatherCode(ResultParser.getWeatherId(currentForecastData));

        Long utcEpoch = ResultParser.getWeatherEpoch(currentForecastData);
        JSONObject timeZoneResult = calculateTimeZoneOffset(coords, utcEpoch);

        Long adjustedEpoch = 1000 * (timeZoneResult.optLong("dstOffset")
                + timeZoneResult.optLong("rawOffset") + utcEpoch);
        forecast.setEpochTime(adjustedEpoch);

        String dateTime = DateHandler.getDateStringFromEpoch(adjustedEpoch);
        String utcDateTime = DateHandler.getDateStringFromEpoch(utcEpoch * 1000);
        forecast.setDateTime(dateTime);
        forecast.setUtcDateTime(utcDateTime);
        return forecast;
    }

    private JSONObject calculateTimeZoneOffset(JSONObject coords, Long utcEpoch)
            throws IOException, JSONException {
        double lat = coords.optDouble("lat");
        double lon = coords.optDouble("lon");
        return callTimeZoneApi(lat, lon, utcEpoch);
    }

    private JSONObject callTimeZoneApi(double lat, double lon, long timeStamp)
            throws IOException, JSONException {
        StringBuilder apiResult = new StringBuilder();
        URL url = new URL("https://maps.googleapis.com/maps/api/timezone/json?location="
                + lat + "," + lon + "&timestamp=" + timeStamp + "&key=" + TIMEZONE_KEY);
        HttpsURLConnection apiConnection = (HttpsURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(apiConnection.getInputStream()));

        String line;
        while((line = reader.readLine()) != null) {
            apiResult.append(line).append("\n");
        }
        reader.close();
        apiConnection.disconnect();
        return new JSONObject(apiResult.toString());
    }
}
