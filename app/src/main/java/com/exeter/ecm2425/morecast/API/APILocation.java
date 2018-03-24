package com.exeter.ecm2425.morecast.API;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.exeter.ecm2425.morecast.Activities.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class APILocation {

    private FusedLocationProviderClient locClient;
    private MainActivity activity;
    private Intent intent;

    public static final int PERMISSIONS_SUCCESS = 0;

    public APILocation(MainActivity activity, Intent intent) {
        this.locClient = LocationServices.getFusedLocationProviderClient(activity);
        this.activity = activity;
        this.intent = intent;
        getLocation();
    }

    public void getLocation() {
        try {
            locClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                activity.startApiService(intent, location);
                            }
                        }
                    }
            ).addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // deal with no internet here.
                        }
                    }
            );
        } catch(SecurityException secEx) {
            System.out.println(secEx.getMessage());
        }
    }

    public static boolean checkLocationPermission(Activity activity) {
        if(ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
