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

/**
 * Builds FiveDayForecast objects to be inserted into the database or bound
 * to RecyclerViews. ResultParser is used to parse the JSON that is returned from
 * the API. Timezone API calls and parsing is also handled here. All of the work
 * this class does happens on the separate thread that the APIService runs on.
 *
 * @author 640010970
 * @version 1.2.0
 */
public class ForecastParser {
    // An array of forecasts to be put into the database when populated.
    private FiveDayForecast[] fiveDayForecasts;

    // The API-key required to access the Google Timezone API.
    private final static String TIMEZONE_KEY = "AIzaSyAr3TGv-W69DKMTsSSSHp38cZsAttsFZaw";

    /**
     * Constructor for ForecastParser.
     */
    public ForecastParser() {
        this.fiveDayForecasts = new FiveDayForecast[] { };
    }

    /**
     * Retrieve the FiveDayForecasts.
     * @return FiveDayForecast[] The array of FiveDayForecasts.
     */
    public FiveDayForecast[] getFiveDayForecasts() {
        return this.fiveDayForecasts;
    }

    /**
     * Parses the weather data returned by the API from JSON into the required array
     * of FiveDayForecasts.
     * @param result JSONObject containing api result data.
     * @throws IOException Passed up from a failed TimeZone API call.
     * @throws JSONException If JSON parsing fails then pass up to the APIService.
     */
    public void parseWeatherData(JSONObject result) throws IOException, JSONException {

        // Get the coordinates of the current lcoation. Need for the TimeZone call.
        JSONObject coords = result.optJSONObject("city").optJSONObject("coord");
        JSONObject timeZoneResult;

        // Retrieve the forecast time-stamps from the JSON.
        JSONArray forecasts = result.optJSONArray("list");

        // Retrieve an epoch in UTC time from OpenWeatherMap for adjustment.
        Long utcEpoch = ResultParser.getWeatherEpoch(forecasts.optJSONObject(0));

        if(coords.length() != 0) {
            // Retrieve the timezone based on longitude and latitude.
            timeZoneResult = calculateTimeZoneOffset(coords, utcEpoch);
        } else {
            // Otherwise use UTC.
            timeZoneResult = createDefaultTimeZone();
        }

        // Populate the FiveDayForecast objects one by one.
        FiveDayForecast[] fiveDayForecast = new FiveDayForecast[forecasts.length()];
        for(int i = 0; i < forecasts.length(); i++) {
            JSONObject currentForecastData = forecasts.optJSONObject(i);
            FiveDayForecast forecast = populateFiveDayForecast(new FiveDayForecast(),
                    currentForecastData, timeZoneResult);
            fiveDayForecast[i] = forecast;
        }
        this.fiveDayForecasts = fiveDayForecast;
    }

    /**
     * Populates the FiveDayForecast objects by accessing their mutator methods.
     *
     * @param forecast The forecast object to set the attributes of.
     * @param currentForecastData The current JSON forecast.
     * @param timeZoneResult The JSONObject which contains the the result from the
     *                       time zone API.
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private FiveDayForecast populateFiveDayForecast
            (FiveDayForecast forecast, JSONObject currentForecastData, JSONObject timeZoneResult)
            throws IOException, JSONException {

        String precipitationType =
                ResultParser.checkPrecipitationType(currentForecastData);
        forecast.setDescription
                (ResultParser.getWeatherDescription(currentForecastData));
        forecast.setPressure
                (ResultParser.getDoubleFromJson(currentForecastData, "main", "pressure"));
        forecast.setTemperature
                (ResultParser.getDoubleFromJson(currentForecastData, "main", "temp"));
        forecast.setHumidity
                (ResultParser.getIntFromJson(currentForecastData, "main", "humidity"));
        forecast.setWindSpeed
                (ResultParser.getDoubleFromJson(currentForecastData, "wind", "speed"));
        forecast.setWindDegree
                (ResultParser.getDoubleFromJson(currentForecastData, "wind", "deg"));
        forecast.setPrecipitationAmount(ResultParser.getPrecipitationAmount(currentForecastData,
                precipitationType));
        forecast.setPrecipitationType(precipitationType);
        forecast.setWeatherCode(ResultParser.getWeatherId(currentForecastData));

        Long utcEpoch = ResultParser.getWeatherEpoch(currentForecastData);

        // Add up all the milliseconds of the offsets retrieved from the API.
        Long adjustedEpoch = calculateMilliSecondEpoch(timeZoneResult.optLong("dstOffset"),
                timeZoneResult.optLong("rawOffset"), utcEpoch);

        forecast.setEpochTime(adjustedEpoch);

        // Get the date and UTC date. Multiply by 1000 to convert to milliseconds
        // as OpenWeatherMap returns an epoch stamp in seconds.
        String dateTime = DateHandler.getDateStringFromEpoch(adjustedEpoch);
        String utcDateTime = DateHandler.getDateStringFromEpoch(utcEpoch * 1000);

        forecast.setDateTime(dateTime);
        forecast.setUtcDateTime(utcDateTime);
        forecast.setTimeZoneName(timeZoneResult.optString("timeZoneName"));
        return forecast;
    }

    /**
     * Calculate the timezone offset including adjustment for Daylight Savings Time
     * by calling the Google TimeZone API.
     * @param coords The lat and long where the timezone is to be calculated.
     * @param utcEpoch The epoch time in UTC.
     * @return JSONObject The return object from calling callTimeZoneApi.
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject calculateTimeZoneOffset(JSONObject coords, Long utcEpoch)
            throws IOException, JSONException {
        double lat = coords.optDouble("lat");
        double lon = coords.optDouble("lon");
        return callTimeZoneApi(lat, lon, utcEpoch);
    }

    /**
     * Calculates the epoch time with time zone offsets in milliseconds.
     * @param dstOffset The offset created by Daylight Savings Time.
     * @param rawOffset The offset created geographically.
     * @param utcEpoch The epoch in UTC time.
     * @return Long The epoch time in milliseconds.
     */
    private Long calculateMilliSecondEpoch(Long dstOffset, Long rawOffset, Long utcEpoch) {
        return 1000 * (dstOffset + rawOffset + utcEpoch);
    }

    /**
     * Makes a call to the Google TimeZone API to retrieve the time zone offsets
     * at the supplied latitude and longitude.
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     * @param timeStamp The epoch timestamp to request for in local time.
     * @return JSONObject The object containing the offset results from the API.
     * @throws IOException If the API cannot be streamed from.
     * @throws JSONException Thrown when a JSONObject cannot be constructed from
     *                       an API result.
     */
    private JSONObject callTimeZoneApi(double lat, double lon, long timeStamp)
            throws IOException, JSONException {
        StringBuilder apiResult = new StringBuilder();
        URL url = new URL("https://maps.googleapis.com/maps/api/timezone/json?location="
                + lat + "," + lon + "&timestamp=" + timeStamp + "&key=" + TIMEZONE_KEY);
        HttpsURLConnection apiConnection = (HttpsURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(apiConnection.getInputStream()));

        String line;

        // Read the data line by line.
        while((line = reader.readLine()) != null) {
            apiResult.append(line).append("\n");
        }
        reader.close();
        apiConnection.disconnect();
        return new JSONObject(apiResult.toString());
    }

    /**
     * Creates a default UTC time zone if a timezone cannot be retrieved.
     * This tends to happen for ocean locations or maybe the Arctic or the
     * Antarctic.
     * @return The JSONObject containing a default time zone.
     * @throws JSONException Thrown if a JSONObject cannot be created.
     */
    private JSONObject createDefaultTimeZone() throws JSONException {
        JSONObject timeZone = new JSONObject();
        timeZone.put("dstOffset", 0);
        timeZone.put("rawOffset", 0);
        timeZone.put("timeZoneName", "Coordinated Universal Time");
        return timeZone;
    }
}
