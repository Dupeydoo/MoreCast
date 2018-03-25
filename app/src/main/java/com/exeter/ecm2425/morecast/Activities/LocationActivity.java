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

public class LocationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager viewManager;
    private ArrayList<String> capitalCities;
    protected GeoDataClient geoDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Choose Location");
        setContentView(R.layout.activity_location);

        if(NetworkHelper.checkForInternet(this)) {
            AsyncTask<Context, Void, Boolean> capitals =
                    new ReadCapitalCities().execute(this);
            geoDataClient = Places.getGeoDataClient(this);
            setUpAutoCompleteFragment();
        } else {
            TextView alertView = (TextView) findViewById(R.id.alertView);
            alertView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.locationList);
        recyclerView.setHasFixedSize(true);
        viewManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(viewManager);
        RecyclerView.Adapter viewAdapter = new LocationAdapter(capitalCities);
        recyclerView.setAdapter(viewAdapter);
        styleRecyclerView();
    }

    private void styleRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), viewManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void itemClick(View view) {
        TextView textView = (TextView) view;
        Intent intent = createForecastIntent();
        intent.putExtra("named-location", textView.getText());
        startActivity(intent);
    }

    private void setUpAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autoCompleteFragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Intent intent = createForecastIntent();
                intent.putExtra("lat-lng", place.getLatLng());
                startActivity(intent);
            }

            @Override
            public void onError(Status status) {
                createLocationsErrorDialog(R.string.googleSearchError, R.string.errorTitle);
            }
        });
    }

    private Intent createForecastIntent() {
        Class destination = MainActivity.class;
        return new Intent(this, destination);
    }

    private class ReadCapitalCities extends AsyncTask<Context, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar fileBar = findViewById(R.id.fileBar);
            fileBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Context... context) {
            capitalCities = readLocationsText(context[0]);
            if(capitalCities.isEmpty() || capitalCities == null) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean finished) {
            super.onPostExecute(finished);
            if(finished) {
                ProgressBar fileBar = findViewById(R.id.fileBar);
                fileBar.setVisibility(View.INVISIBLE);
                setUpRecyclerView();
            } else {
                createLocationsErrorDialog(R.string.locationsTextError, R.string.errorTitle);
            }
        }
    }

    private ArrayList<String> readLocationsText(Context context) {
        ArrayList<String> locationsText = new ArrayList<String>();
        BufferedReader reader;

        try{
            final InputStream locFile = context.getResources().openRawResource(R.raw.citieslist);
            reader = new BufferedReader(new InputStreamReader(locFile));
            String line = reader.readLine();
            locationsText.add(line);
            while(line != null){
                line = reader.readLine();
                locationsText.add(line);
            }
        } catch(IOException ioException){
            createLocationsErrorDialog(R.string.locationsTextError, R.string.errorTitle);
        }
        return locationsText;
    }

    private void createLocationsErrorDialog(int messageId, int errorTitleId) {
        Resources resources = getResources();
        String error = resources.getString(messageId);
        String title = resources.getString(errorTitleId);
        ErrorDialog errorDialog = new ErrorDialog(error, title);
        errorDialog.showDialog(this);
    }
}
