package com.exeter.ecm2425.morecast.DataProcessing;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;

import java.util.ArrayList;

/**
 * Adapter to bind data to the default cities list in the LocationActivity.
 *
 * @author 640010970
 * @version 1.0.0
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<String> capitalCities;

    /**
     * ViewHolder class to hold TextViews which display each capital
     * city in turn.
     *
     * @author 640010970
     * @version 1.0.0
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        /**
         * Constructor to instantiate a TextView holder.
         * @param textView The TextView to bind data to.
         */
        public ViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }

    /**
     * LocationAdapter constructor to create the Adapter data-set.
     * @param capitalCities
     */
    public LocationAdapter(ArrayList<String> capitalCities) {
        this.capitalCities = capitalCities;
    }

    /**
     * {@inheritDoc}
     * @return LocationAdapter.ViewHolder Holds the TextViews for the RecyclerView.
     */
    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_text_view, parent, false);
        return new ViewHolder(textView);
    }

    /**
     * {@inheritDoc}
     * Sets the TextViews contained in the ViewHolders.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(capitalCities.get(position));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return capitalCities.size();
    }
}
