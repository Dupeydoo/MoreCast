package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class TodayView extends ConstraintLayout {
    private TextView bigTemperature;
    private TextView descriptor;

    private ImageView stampOne;
    private ImageView stampTwo;
    private ImageView stampThree;
    private ImageView stampFour;

    private TextView labelOne;
    private TextView labelTwo;
    private TextView labelThree;
    private TextView labelFour;

    private TextView pressureContent;
    private TextView humidityContent;
    private TextView windSpeed;
    private TextView windDegrees;
    private TextView precipitationType;
    private TextView precipitationAmount;

    public TodayView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.today_view, this);
        setViews();
    }

    public TodayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.today_view, this);
        setViews();
    }

    private void setViews() {
        this.bigTemperature = (TextView) findViewById(R.id.bigTemperature);
        this.descriptor = (TextView) findViewById(R.id.descriptor);

        this.stampOne = (ImageView) findViewById(R.id.stampOne);
        this.stampTwo = (ImageView) findViewById(R.id.stampTwo);
        this.stampThree = (ImageView) findViewById(R.id.stampThree);
        this.stampFour = (ImageView) findViewById(R.id.stampFour);

        this.labelOne = (TextView) findViewById(R.id.labelOne);
        this.labelTwo = (TextView) findViewById(R.id.labelTwo);
        this.labelThree = (TextView) findViewById(R.id.labelThree);
        this.labelFour = (TextView) findViewById(R.id.labelFour);

        this.pressureContent = (TextView) findViewById(R.id.pressureContent);
        this.humidityContent = (TextView) findViewById(R.id.humidityContent);
        this.windSpeed = (TextView) findViewById(R.id.windSpeed);
        this.windDegrees = (TextView) findViewById(R.id.windDegrees);
        this.precipitationType = (TextView) findViewById(R.id.precipitationType);
        this.precipitationAmount = (TextView) findViewById(R.id.precipitationAmount);
    }

    public void setMainInfo(double bigTemperature, String descriptor) {
        this.bigTemperature.setText(String.format(Locale.ENGLISH, "%.1f °C", bigTemperature));
        this.descriptor.setText(descriptor);
    }

    public void setImages(
            int firstCode, int secondCode, int thirdCode, int fourthCode,
            int firstTime, int secondTime, int thirdTime, int fourthTime,
            ArrayList<Double> temperatures) {
        ViewHelper weatherHelper = new ViewHelper();
        Context context = getContext();
        weatherHelper.setWeatherImage(context, firstCode, stampOne, firstTime, temperatures.get(0));
        weatherHelper.setWeatherImage(context, secondCode, stampTwo, secondTime, temperatures.get(1));
        weatherHelper.setWeatherImage(context, thirdCode, stampThree, thirdTime, temperatures.get(2));
        weatherHelper.setWeatherImage(context, fourthCode, stampFour, fourthTime, temperatures.get(3));
    }

    public void setLabels(
            String firstTime, String secondTime, String thirdTime, String fourthTime) {
        this.labelOne.setText(parseDateTime(firstTime));
        this.labelTwo.setText(parseDateTime(secondTime));
        this.labelThree.setText(parseDateTime(thirdTime));
        this.labelFour.setText(parseDateTime(fourthTime));
    }

    public void setAdditionalWeatherInfo(
            double pressure, int humidity, double speed, double windDirection,
            String precipType, double precipAmount) {
        this.pressureContent.setText(String.format(Locale.ENGLISH, "%.2f hPa", pressure));
        this.humidityContent.setText(String.format(Locale.ENGLISH, "%d%%", humidity));
        this.windSpeed.setText(String.format(Locale.ENGLISH, "%.2f m/s", speed));
        this.windDegrees.setText(String.format(Locale.ENGLISH, "%.2f degrees", windDirection));
        this.precipitationType.setText(precipType);
        this.precipitationAmount.setText(String.format(Locale.ENGLISH, "%.2f mm, (3h)", precipAmount));
    }

    private String parseDateTime(String dateTime) {
        return dateTime.split("\\s+")[1].substring(0, 5);
    }
}
