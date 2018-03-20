package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;

import java.util.Locale;

public class TodayView extends RelativeLayout {
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

    public TodayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.today_view, this);
        setViews();
    }

    private void setViews() {
        bigTemperature = (TextView) findViewById(R.id.bigTemperature);
        descriptor = (TextView) findViewById(R.id.descriptor);

        stampOne = (ImageView) findViewById(R.id.stampOne);
        stampTwo = (ImageView) findViewById(R.id.stampTwo);
        stampThree = (ImageView) findViewById(R.id.stampThree);
        stampFour = (ImageView) findViewById(R.id.stampFour);

        labelOne = (TextView) findViewById(R.id.labelOne);
        labelTwo = (TextView) findViewById(R.id.labelTwo);
        labelThree = (TextView) findViewById(R.id.labelThree);
        labelFour = (TextView) findViewById(R.id.labelFour);

        pressureContent = (TextView) findViewById(R.id.pressureContent);
        humidityContent = (TextView) findViewById(R.id.humidityContent);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDegrees = (TextView) findViewById(R.id.windDegrees);
    }

    public void setMainInfo(double bigTemperature, String descriptor) {
        this.bigTemperature.setText(String.format(Locale.ENGLISH, "%.1f °C", bigTemperature));
        this.descriptor.setText(descriptor);
    }

    public void setImages(
            int firstCode, int secondCode, int thirdCode, int fourthCode) {
        setWeatherImage(firstCode, stampOne);
        setWeatherImage(secondCode, stampTwo);
        setWeatherImage(thirdCode, stampThree);
        setWeatherImage(fourthCode, stampFour);
    }

    public void setLabels(
            String firstTime, String secondTime, String thirdTime, String fourthTime) {
        this.labelOne.setText(firstTime);
        this.labelTwo.setText(secondTime);
        this.labelThree.setText(thirdTime);
        this.labelFour.setText(fourthTime);
    }

    public void setAdditionalWeatherInfo(
            double pressure, int humidity, double speed, double windDirection) {
        this.pressureContent.setText(String.format(Locale.ENGLISH, "%.2f hPa", pressure));
        this.humidityContent.setText(String.format(Locale.ENGLISH, "%d%%", humidity));
        this.windSpeed.setText(String.format(Locale.ENGLISH, "%.2f m/s", speed));
        this.windDegrees.setText(String.format(Locale.ENGLISH, "%.2f degrees", windDirection));
    }

    private void setWeatherImage(int weather, ImageView image) {
        if(weather >= 200 && weather < 300) {
            // thunderstorm;
        }

        else if(weather >= 300 && weather < 400) {
            // drizzle
        }

        else if(weather >= 500 && weather < 502) {
            // light rain
        }

        else if(weather >= 502 && weather < 600) {
            // heavy rain
        }

        else if(weather >= 600 && weather < 700) {
            // snow
        }

        else if(weather >= 700 && weather < 800) {
            // atmosphere - mist
        }

        else if(weather == 800) {
            // sun
        }

        else if(weather == 801 || weather == 802) {
            // light cloud
        }

        else if(weather == 803 || weather == 804) {
            // heavy cloud
        }

        else if(weather >= 900 && weather < 903) {
            // hurrican, tornado, trop storm
        }

        else if(weather == 903) {
            // cold
        }

        else if(weather == 904) {
            // hot
        }

        else {
            // some winds and shit
        }
    }
}
