package com.exeter.ecm2425.morecast.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.exeter.ecm2425.morecast.DataProcessing.LocationAdapter;
import com.exeter.ecm2425.morecast.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter viewAdapter;
    private RecyclerView.LayoutManager viewManager;
    private ArrayList<String> capitalCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        capitalCities = readLocationsText(this);
        recyclerView = (RecyclerView) findViewById(R.id.locationList);
        recyclerView.setHasFixedSize(true);
        viewManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(viewManager);
        viewAdapter = new LocationAdapter(capitalCities);
        recyclerView.setAdapter(viewAdapter);
    }

    public ArrayList<String> readLocationsText(Context context) {
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
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        return locationsText;
    }
}
