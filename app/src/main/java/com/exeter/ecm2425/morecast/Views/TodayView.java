package com.exeter.ecm2425.morecast.Views;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Utils.DateHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

/**
 * TodayView is the the detailed screen for a weather forecast.
 * A custom view implementation is used so that child Views can
 * be organised in a desired layout. This view is used both for
 * the first forecast in the MainActivity and the forecast displayed
 * in the DetailedActivity.
 *
 * @author 640010970
 * @version 1.0.0
 */
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

    /**
     * Create a TodayView with a given parent context and
     * inflate it so the RecyclerView does not have to.
     * @param context The housing context of the view, for
     *                example, the MainActivity.
     */
    public TodayView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.today_view, this);
        setViews();
    }

    /**
     * Create a TodayView with a given parent context and
     * attributes.
     * @param context The housing context of the view, for
     *                example, the MainActivity.
     * @param attrs A collection of attributes associated
     *              with the TodayView tag in xml.
     */
    public TodayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.today_view, this);
        setViews();
    }

    /**
     * Finds and initialises references to all the child views to which
     * data will be bound.
     */
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

    /**
     * Sets the main information at the top of the TodayView.
     * @param bigTemperature The current temperature.
     * @param descriptor A description of the current weather.
     * @param timeZoneName The name of the timezone for the selected
     *                     location.
     */
    public void setMainInfo(double bigTemperature, String descriptor, String timeZoneName) {
        Resources res = getResources();
        this.bigTemperature.setText(String.format(Locale.ENGLISH,
                res.getString(R.string.temperatureUnits), bigTemperature));
        String totalDescription = descriptor + "\n" + timeZoneName;
        this.descriptor.setText(totalDescription);
    }

    /**
     * Sets the weather icons at different times of the day shown by the TodayView.
     * @param firstCode The first weather code.
     * @param secondCode The second weather code.
     * @param thirdCode The third weather code.
     * @param fourthCode The fourth weather code.
     * @param firstTime The first time of day.
     * @param secondTime The second time of day.
     * @param thirdTime The third time of day.
     * @param fourthTime The fourth time of day.
     * @param temperatures The temperatures to help decide
     *                     if to show a hot or cold icon.
     */
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

    /**
     * Sets the labels associated with the weather icons with correct times
     * and temperatures of the day.
     * @param firstTime The first time of the day.
     * @param secondTime The second time of the day.
     * @param thirdTime The third time of the day.
     * @param fourthTime The fourth time of the day.
     */
    public void setLabels(String firstTime, String secondTime, String thirdTime,
                          String fourthTime, ArrayList<Double> temperatures) {
        Resources res = getResources();
        this.labelOne.setText(DateHandler.parseDateTime(firstTime));
        this.labelTwo.setText(DateHandler.parseDateTime(secondTime));
        this.labelThree.setText(DateHandler.parseDateTime(thirdTime));
        this.labelFour.setText(DateHandler.parseDateTime(fourthTime));

        this.labelOne.append("\n" + String.format(Locale.ENGLISH,
                res.getString(R.string.temperatureUnits), temperatures.get(0)));
        this.labelTwo.append("\n" + String.format(Locale.ENGLISH,
                res.getString(R.string.temperatureUnits), temperatures.get(1)));
        this.labelThree.append("\n" + String.format(Locale.ENGLISH,
                res.getString(R.string.temperatureUnits), temperatures.get(2)));
        this.labelFour.append("\n" + String.format(Locale.ENGLISH,
                res.getString(R.string.temperatureUnits), temperatures.get(3)));
    }

    /**
     * Sets the additional weather information needed for a detailed weather
     * forecast.
     * @param pressure The current pressure, measured in hPa.
     * @param humidity The current humidity as a percentage.
     * @param speed The current wind speed.
     * @param windDirection The current wind direction.
     * @param precipType The type of precipitation.
     * @param precipAmount The amount of precipitation in mm.
     */
    public void setAdditionalWeatherInfo(
            double pressure, int humidity, double speed, double windDirection,
            String precipType, double precipAmount) {
        Resources res = getResources();
        this.pressureContent.setText
                (String.format(Locale.ENGLISH, res.getString(R.string.pressureUnits), pressure));
        this.humidityContent.setText
                (String.format(Locale.ENGLISH, res.getString(R.string.humidityUnits), humidity));
        this.windSpeed.setText
                (String.format(Locale.ENGLISH, res.getString(R.string.windSpeedUnits), speed));
        this.windDegrees.setText
                (String.format(Locale.ENGLISH, res.getString(R.string.windDirectionUnits), windDirection));
        this.precipitationAmount.setText
                (String.format(Locale.ENGLISH, res.getString(R.string.precipAmountUnits), precipAmount));
        this.precipitationType.setText(precipType);
    }
}
