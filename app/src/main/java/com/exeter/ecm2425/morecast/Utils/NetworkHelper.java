package com.exeter.ecm2425.morecast.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
