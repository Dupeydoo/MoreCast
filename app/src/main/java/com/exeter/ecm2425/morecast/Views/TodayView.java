package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;

import org.w3c.dom.Text;

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
            double pressure, int humidity, double speed, double windDirection,
            String precipType, double precipAmount) {
        this.pressureContent.setText(String.format(Locale.ENGLISH, "%.2f hPa", pressure));
        this.humidityContent.setText(String.format(Locale.ENGLISH, "%d%%", humidity));
        this.windSpeed.setText(String.format(Locale.ENGLISH, "%.2f m/s", speed));
        this.windDegrees.setText(String.format(Locale.ENGLISH, "%.2f degrees", windDirection));
        this.precipitationType.setText(precipType);
        this.precipitationAmount.setText(String.format(Locale.ENGLISH, "%.2f mm, (3h)", precipAmount));
    }

    private void setWeatherImage(int weather, ImageView image, String time) {
        if(weather >= 200 && weather < 300) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_storm));
        }

        else if(weather >= 300 && weather < 400) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_drizzle));
        }

        else if(weather >= 500 && weather < 502) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_lightrain));
        }

        else if(weather >= 502 && weather < 600) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_heavyrain));
        }

        else if(weather >= 600 && weather < 700) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_snow));
        }

        else if(weather >= 700 && weather < 800) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_mist));
        }

        else if(weather == 800) {
            int timeNum = Integer.parseInt(time.substring(0, 1));
            if(timeNum > 19 && timeNum < 6) {
                image.setImageDrawable(getResources().getDrawable(R.drawable.ic_moon));
            } else {
                image.setImageDrawable(getResources().getDrawable(R.drawable.ic_sun));
            }
        }

        else if(weather == 801 || weather == 802) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_lightcloud));
        }

        else if(weather == 803 || weather == 804) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_heavycloud));
        }

        else if(weather >= 900 && weather < 903) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_hurricane));
        }

        else if(weather == 903) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_cold));
        }

        else if(weather == 904) {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_hot));
        }

        else {
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_wind));
        }
    }
}
