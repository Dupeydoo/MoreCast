package com.exeter.ecm2425.morecast.API;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.exeter.ecm2425.morecast.Activities.LocationActivity;
import com.exeter.ecm2425.morecast.Activities.MainActivity;
import com.exeter.ecm2425.morecast.Views.ErrorDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * The APILocation class handles retrieving the users location via the
 * Google Fused Location Provider Client.
 *
 * @author 640010970
 * @version 2.0.0
 */
public class APILocation {

    private FusedLocationProviderClient locClient;
    private MainActivity activity;
    private Intent intent;

    // Constant to signal success of requesting permissions.
    public static final int PERMISSIONS_SUCCESS = 0;

    /**
     * Creates an APILocation object, when instantiated the location
     * is retrieved instantly.
     * @param activity The MainActivity requesting a location.
     * @param intent An intent for the APIService.
     */
    public APILocation(MainActivity activity, Intent intent) {
        this.locClient = LocationServices.getFusedLocationProviderClient(activity);
        this.activity = activity;
        this.intent = intent;
        getLocation();
    }

    /**
     * Retrieves the user's location using the Google location provider
     * client.
     */
    public void getLocation() {
        try {
            // Listener for when the location fetch is completed.
            locClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                boolean isGpsButton = intent.getBooleanExtra("is-gps-button", false);
                                // Call the APIService with the current location.
                                activity.startApiService(intent, location, isGpsButton);
                            }
                        }
                    }
            ).addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Default to London if the current location can't be obtained.
                            activity.startApiService(APILocation.this.intent, "London");
                        }
                    }
            );
        } catch(SecurityException securityException) {
            // Backup if somehow the permissions are not handled by
            // the usual mechanisms.
            activity.startApiService(this.intent, "London");
        }
    }

    /**
     * Checks for fine grain location permissions.
     * @param activity Activity where the permissions is checked.
     * @return boolean False if permissions are not set, true otherwise.
     */
    public static boolean checkLocationPermission(Activity activity) {
        if(ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
