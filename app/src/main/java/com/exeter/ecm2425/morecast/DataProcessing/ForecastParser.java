package com.exeter.ecm2425.morecast.DataProcessing;


import com.exeter.ecm2425.morecast.Database.FiveDayForecast;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForecastParser {
    private FiveDayForecast[] fiveDayForecasts;

    public ForecastParser() {
        this.fiveDayForecasts = new FiveDayForecast[] { };
    }

    public FiveDayForecast[] getFiveDayForecasts() {
        return this.fiveDayForecasts;
    }

    public void parseWeatherData(JSONObject result) {
        JSONArray forecasts = result.optJSONArray("list");
        FiveDayForecast[] fiveDayForecast = new FiveDayForecast[forecasts.length()];
        for(int i = 0; i < forecasts.length(); i++) {
            JSONObject currentForecastData = forecasts.optJSONObject(i);
            FiveDayForecast forecast = populateFiveDayForecast(new FiveDayForecast(),
                    currentForecastData);
            fiveDayForecast[i] = forecast;
        }
        this.fiveDayForecasts = fiveDayForecast;
    }

    private FiveDayForecast populateFiveDayForecast
            (FiveDayForecast forecast, JSONObject currentForecastData) {
        String precipitationType = ResultParser.checkPrecipitationType(currentForecastData);
        forecast.setEpochTime(ResultParser.getWeatherEpoch(currentForecastData));
        forecast.setDescription(ResultParser.getWeatherDescription(currentForecastData));
        forecast.setPressure(ResultParser.getDoubleFromJson(currentForecastData, "main", "pressure"));
        forecast.setTemperature(ResultParser.getDoubleFromJson(currentForecastData, "main", "temp"));
        forecast.setHumidity(ResultParser.getIntFromJson(currentForecastData, "main", "humidity"));
        forecast.setWindSpeed(ResultParser.getDoubleFromJson(currentForecastData, "wind", "speed"));
        forecast.setWindDegree(ResultParser.getDoubleFromJson(currentForecastData, "wind", "deg"));
        forecast.setPrecipitationType(precipitationType);
        forecast.setPrecipitationAmount(ResultParser.getPrecipitationAmount(currentForecastData,
                precipitationType));
        forecast.setDateTime(ResultParser.getDateTime(currentForecastData));
        forecast.setWeatherCode(ResultParser.getWeatherId(currentForecastData));
        return forecast;
    }
}
