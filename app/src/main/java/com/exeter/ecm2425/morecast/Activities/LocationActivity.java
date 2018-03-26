package com.exeter.ecm2425.morecast.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.DataProcessing.LocationAdapter;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Utils.NetworkHelper;
import com.exeter.ecm2425.morecast.Views.ErrorDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The LocationActivity is where the user may choose a location
 * to get the weather for. Both a Google AutoCompleteFragment search bar
 * is provided and a RecyclerView of default capital cities read from a
 * text file. The result is millions of possible location choices.
 *
 * @author 640010970
 * @version 3.1.2
 */
public class LocationActivity extends BaseActivity {

    // The RecyclerView which displays the default locations
    private RecyclerView recyclerView;

    // The layout manager that controls RecyclerView layout.
    private LinearLayoutManager viewManager;

    // The ArrayList the capital cities are read into to be
    // bound to the RecyclerView.
    private ArrayList<String> capitalCities;

    /**
     * {@inheritDoc}
     * If the device has an internet connection then the default
     * list is populated and search fragment initialised.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Choose Location");
        setContentView(R.layout.activity_location);

        // Checks if network devices are accessing the internet.
        if(NetworkHelper.checkForInternet(this)) {
            AsyncTask<Context, Void, Boolean> capitals =
                    new ReadCapitalCities().execute(this);
            geoDataClient = Places.getGeoDataClient(this);
            setUpAutoCompleteFragment();
        } else {
            // Otherwise signal to the user that they need internet to
            // make location API calls.
            TextView alertView = (TextView) findViewById(R.id.alertLView);
            alertView.setVisibility(View.VISIBLE);
            alertView.bringToFront();
        }
    }

    /**
     * {@inheritDoc}
     * Overrides the BaseActivity, with a locations specific menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.locationmenu, menu);
        return true;
    }

    /**
     * Sets up the RecyclerView for the capital cities.
     */
    private void setUpRecyclerView() {
        // Find the RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.locationList);
        recyclerView.setHasFixedSize(true);

        // Instantiate the LayoutManager, LocationAdapter and bind them.
        viewManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(viewManager);
        RecyclerView.Adapter viewAdapter = new LocationAdapter(capitalCities);
        recyclerView.setAdapter(viewAdapter);

        // Finally style the RecyclerView.
        styleRecyclerView();
    }

    /**
     * A method to apply runtime Styles to the RecyclerView. Currently
     * adds divider item decorations between locations.
     */
    private void styleRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), viewManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    /**
     * Handles the onclick of a RecyclerView item tap. A new intent with
     * the named location is sent to the MainActivity.
     * @param view The view that has been clicked.
     */
    public void itemClick(View view) {
        TextView textView = (TextView) view;
        Intent intent = createForecastIntent();

        // Use a named location, e.g. Edinburgh of the Seven Seas.
        intent.putExtra("named-location", textView.getText());
        startActivity(intent);
    }

    /**
     * Sets up the AutoComplete location search fragment.
     */
    private void setUpAutoCompleteFragment() {
        // Find the fragment from the xml.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autoCompleteFragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            /**
             * {@inheritDoc}
             * Creates an intent with the selected Longitude and Latitude to
             * send to the APIService for an API call.
             */
            @Override
            public void onPlaceSelected(Place place) {
                Intent intent = createForecastIntent();
                intent.putExtra("lat-lng", place.getLatLng());
                startActivity(intent);
            }

            /**
             * {@inheritDoc}
             * Shows an error dialog if the Places API fails to respond correctly.
             */
            @Override
            public void onError(Status status) {
                createErrorDialog(R.string.googleSearchError, R.string.errorTitle);
            }
        });
    }

    /**
     * Creates an intent to the MainActivity which will
     * contain data to be sent to the APIService.
     * @return Intent The intent that is used to start the MainActivity.
     */
    private Intent createForecastIntent() {
        Class destination = MainActivity.class;
        return new Intent(this, destination);
    }

    /**
     * This nested private AsyncTask offloads the process of loading locations from
     * a textfile from the UI thread. It is nested as done commonly in Android due
     * to its reliance on the hosting Activity.
     *
     * @author 640010970
     * @version 1.0.0
     */
    private class ReadCapitalCities extends AsyncTask<Context, Void, Boolean> {
        /**
         * {@inheritDoc}
         * Show a progress spinner as the task sets up.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar fileBar = findViewById(R.id.fileBar);
            fileBar.setVisibility(View.VISIBLE);
        }

        /**
         * {@inheritDoc}
         * @param context An array of contexts, only ever 1 long.
         * @return boolean False if the results of reading are nothing,
         *         true if read successfully.
         */
        @Override
        protected Boolean doInBackground(Context... context) {
            // Read the citieslist.txt file.
            capitalCities = readLocationsText(context[0]);
            if(capitalCities.isEmpty() || capitalCities == null) {
                return false;
            }
            return true;
        }

        /**
         * {@inheritDoc}
         * Executed when the task has finished, hides the spinner
         * and sets up the RecyclerView.
         * @param finished True if doInBackground returns true.
         */
        @Override
        protected void onPostExecute(Boolean finished) {
            super.onPostExecute(finished);
            if(finished) {
                ProgressBar fileBar = findViewById(R.id.fileBar);
                fileBar.setVisibility(View.INVISIBLE);
                setUpRecyclerView();
            } else {
                // On failure signal that the file could not be read to the user.
                createErrorDialog(R.string.locationsTextError,
                        R.string.errorTitle);
            }
        }

        /**
         * Called only in the ReadCapitalCities AsyncTask to read the capital cities
         * text file.
         * @param context The current context to get resources from.
         * @return ArrayList<String> A List of all the Capital cities.
         */
        private ArrayList<String> readLocationsText(Context context) {
            ArrayList<String> locationsText = new ArrayList<String>();
            BufferedReader reader;

            try{
                // Open the txt file.
                final InputStream locFile = context.getResources().openRawResource(R.raw.citieslist);
                reader = new BufferedReader(new InputStreamReader(locFile));

                // Incrementally read lines, one city per line.
                String line = reader.readLine();
                locationsText.add(line);
                while(line != null){
                    line = reader.readLine();
                    locationsText.add(line);
                }
            } catch(IOException ioException){
                // Signal to the user an error reading the file occured.
                createErrorDialog(R.string.locationsTextError, R.string.errorTitle);
            }
            return locationsText;
        }
    }


}
