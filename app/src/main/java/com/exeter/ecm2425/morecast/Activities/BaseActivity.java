package com.exeter.ecm2425.morecast.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.exeter.ecm2425.morecast.API.APIResultReceiver;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Views.ErrorDialog;
import com.exeter.ecm2425.morecast.Views.ViewHelper;

import org.json.JSONObject;

import static com.exeter.ecm2425.morecast.API.APILocation.PERMISSIONS_SUCCESS;


public abstract class BaseActivity extends AppCompatActivity {

    public APIResultReceiver apiReceiver;
    protected SharedPreferences sharedPreferences;
    protected final static String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    protected final static int TODAY_FORECAST = 0;
    protected final static int FUTURE_FORECAST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(apiReceiver != null) {
            apiReceiver.setReceiver(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.chooseLocation:
                startMorecastActivity(LocationActivity.class);
                return true;

            case R.id.goHome:
                startMorecastActivity(MainActivity.class);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void writeLocToSharedPreferences(String location) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();

        editor.putString("location", location);
        // offload UI thread.
        editor.apply();
    }

    protected void setSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        this.setTitle(preferences.getString("location", "London"));
    }

    protected void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_SUCCESS);
    }

    protected void setBackground(FiveDayForecast forecast) {
        ImageView background = (ImageView) findViewById(R.id.weatherBack);
        ViewHelper.setBackground(forecast, background, getResources());
    }

    protected void setResultTitle(JSONObject result) {
        String city = result.optJSONObject("city").optString("name");
        this.setTitle(city);
        writeLocToSharedPreferences(city);
    }

    protected void createErrorDialog(int messageId, int errorTitleId) {
        Resources resources = getResources();
        String error = resources.getString(messageId);
        String title = resources.getString(errorTitleId);
        ErrorDialog errorDialog = new ErrorDialog(error, title);
        errorDialog.showDialog(this);
    }

    protected void startMorecastActivity(Class destination) {
        Intent intent = new Intent(this, destination);
        startActivity(intent);
    }
}
