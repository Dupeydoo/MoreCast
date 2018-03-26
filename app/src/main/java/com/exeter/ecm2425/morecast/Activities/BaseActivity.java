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

/**
 * Base abstract class of all Activities in Morecast. Morecast supports earlier
 * API levels from 15, 100% of current devices. To this end AppCompatActvitity
 * is the base class for this. Provides common behaviour to other Activities.
 *
 * @author 640010970
 * @version 2.1.0
 */
public abstract class BaseActivity extends AppCompatActivity {

    // ResultReceiver used with the IntentService, APIService.
    public APIResultReceiver apiReceiver;
    protected SharedPreferences sharedPreferences;
    protected final static String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * {@inheritDoc}
     * Sets the apiReceiver to null on pause.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(apiReceiver != null) {
            apiReceiver.setReceiver(null);
        }
    }

    /**
     * The event called to create an options menu.
     * @param menu The menu object to inflate.
     * @return boolean True when the menu is inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     * When an item is selected its Id is used to start another
     * Morecast Activity.
     * @param item The selected menu item.
     * @return boolean True when an activity is started.
     */
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

    /**
     * Writes a given location string to the global SharedPreferences.
     * @param location The location to write.
     */
    protected void writeLocToSharedPreferences(String location) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        // Enable editing of preferences.
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();

        editor.putString("location", location);
        // Apply is used asynchronously instead of commit to offload the UI thread.
        editor.apply();
    }

    /**
     * Sets the title of the Activity to one contained in SharedPreferences.
     */
    protected void setTitleFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, 0);
        // If it can't be set default to London.
        this.setTitle(preferences.getString("location", "London"));
    }

    /**
     * Sets the title of an Activity based on a JSON API Result.
     * @param result The JSONObject containing a title to set.
     */
    protected void setResultTitle(JSONObject result) {
        String city = result.optJSONObject("city").optString("name");
        this.setTitle(city);
        // Once it is set, write it to SharedPreferences.
        writeLocToSharedPreferences(city);
    }

    /**
     * Allows an Activity to request permissions for Accesing the Fine Location of
     * the user using GPS.
     */
    protected void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_SUCCESS);
    }

    /**
     * Sets the ImageView background of an Activity. The background to set
     * is determined by the forecast weather code.
     * @param forecast The FiveDayForecast which contains a weather code.
     */
    protected void setBackground(FiveDayForecast forecast) {
        ImageView background = (ImageView) findViewById(R.id.weatherBack);
        ViewHelper.setBackground(forecast, background, getResources());
    }

    /**
     * Uses the ErrorDialog class to show a dialog with a message and title.
     * @param messageId The string resource id of a message.
     * @param errorTitleId The string resource id of a title.
     */
    protected void createErrorDialog(int messageId, int errorTitleId) {
        Resources resources = getResources();
        String error = resources.getString(messageId);
        String title = resources.getString(errorTitleId);
        ErrorDialog errorDialog = new ErrorDialog(error, title);
        errorDialog.showDialog(this);
    }

    /**
     * Starts an Activity in Morecast. More specific alternatives to this
     * method are provided in subclasses of BaseActivity if required.
     * @param destination The destination activity to start.
     */
    protected void startMorecastActivity(Class destination) {
        Intent intent = new Intent(this, destination);
        startActivity(intent);
    }
}
