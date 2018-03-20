package com.exeter.ecm2425.morecast.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


public class APIService extends IntentService {

    private final String API_KEY = "b706495b99e6ccf49c87124d8c2aa271";
    private String apiAddress = "https://api.openweathermap.org/data/2.5/forecast?units=metric&";
    public static final int API_RUNNING = 1;  // Enums not recommended in Android - reason.
    public static final int API_FINISHED = 0;
    public static final int API_ERROR = -1;

    public APIService() { super("Default"); }

    public APIService(String name) {
        super(name);
    }

    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("api-receiver");
        String command = intent.getStringExtra("command");
        Bundle apiBundle = new Bundle();

        if(command.equals("forecast")) {
            String namedLocation = intent.getStringExtra("named-location");
            String apiSuffix = String.format(Locale.ENGLISH, "q=%s", namedLocation);
            receiver.send(API_RUNNING, Bundle.EMPTY);

            try {
                String apiResult = makeApiCall(apiSuffix);
                apiBundle.putString("result", apiResult);
                receiver.send(API_FINISHED, apiBundle);
            }

            catch(Exception e) {
                apiBundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(API_ERROR, apiBundle);
            }
        }

        else if(command.equals("forecast-location")) {
            Location location = intent.getParcelableExtra("location");
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String apiSuffix = String.format(Locale.ENGLISH, "lat=%f&lon=%f", latitude, longitude);
            receiver.send(API_RUNNING, Bundle.EMPTY);

            try {
                String apiResult = makeApiCall(apiSuffix);
                apiBundle.putString("result", apiResult);
                receiver.send(API_FINISHED, apiBundle);
            }

            catch(Exception e) {
                apiBundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(API_ERROR, apiBundle);
            }
        }
    }

    private String makeApiCall(String apiSuffix) {
        StringBuilder apiResult = new StringBuilder();
        try {
            URL url = new URL(apiAddress + apiSuffix + "&APPID=" + API_KEY);
            HttpsURLConnection apiConnection = (HttpsURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
            String line;

            while((line = reader.readLine()) != null) {
                apiResult.append(line).append("\n");
            }
            reader.close();
            apiConnection.disconnect();
        }

        catch(MalformedURLException e) {

        }

        catch(IOException e) {

        }
        return apiResult.toString();
    }
}
