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
            int firstCode, int secondCode, int thirdCode, int fourthCode, String time) {
        setWeatherImage(firstCode, stampOne, time);
        setWeatherImage(secondCode, stampTwo, time);
        setWeatherImage(thirdCode, stampThree, time);
        setWeatherImage(fourthCode, stampFour, time);
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

    private void setWeatherImage(int weather, ImageView image, String time) {
        if(weather >= 200 && weather < 300) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.storm));
        }

        else if(weather >= 300 && weather < 400) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.drizzle));
        }

        else if(weather >= 500 && weather < 502) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.lightrain));
        }

        else if(weather >= 502 && weather < 600) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.heavyrain));
        }

        else if(weather >= 600 && weather < 700) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.snow));
        }

        else if(weather >= 700 && weather < 800) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.mist));
        }

        else if(weather == 800) {
            int timeNum = Integer.parseInt(time.substring(0, 1));
            if(timeNum > 19 && timeNum < 6) {
                image.setImageDrawable(getResources().getDrawable(R.drawable.moon));
            } else {
                image.setImageDrawable(getResources().getDrawable(R.drawable.sun));
            }
        }

        else if(weather == 801 || weather == 802) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.lightcloud));
        }

        else if(weather == 803 || weather == 804) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.heavycloud));
        }

        else if(weather >= 900 && weather < 903) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.hurricane));
        }

        else if(weather == 903) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.cold));
        }

        else if(weather == 904) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.hot));
        }

        else {
            image.setImageDrawable(getResources().getDrawable(R.drawable.wind));
        }
    }
}
