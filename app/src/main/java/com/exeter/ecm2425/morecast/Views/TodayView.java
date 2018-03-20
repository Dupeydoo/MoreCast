package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;

public class TodayView extends RelativeLayout {
    private TextView bigTemperature;

    public TodayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.today_view, this);
        setViews();
    }

    private void setViews() {
        bigTemperature = (TextView) findViewById(R.id.bigTemperature);
    }
}
