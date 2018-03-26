package com.exeter.ecm2425.morecast.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Class that provides helper methods for network based functionality.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class NetworkHelper {

    /**
     * Checks to see if the user's device is currently connected to the internet.
     * @param context The context where the check is performed.
     * @return boolean True if connected, false otherwise.
     */
    public static boolean checkForInternet(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Helper method to make an API call and read a String of API data. Called by
     * both the ForecastParser to get the timezone and the APIService to get the
     * forecast data. Only ever called on a separate worker thread.
     * @param url The URL to make the API call to.
     * @param apiResult The StringBuilder object to fill with data.
     * @return String The String of data read from the API.
     * @throws IOException If the reading process should fail, caught by the APIService.
     * @throws JSONException If the data can not be parsed to JSON.
     */
    public static String makeApiConnection
            (URL url, StringBuilder apiResult) throws IOException, JSONException {
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
        return apiResult.toString();
    }
}
