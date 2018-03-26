package com.exeter.ecm2425.morecast.Views;


import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.exeter.ecm2425.morecast.Activities.DetailedActivity;
import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;
import com.exeter.ecm2425.morecast.Utils.DateHandler;

import java.util.ArrayList;

/**
 * Separates conditionals with a lot of choices from the actual View classes.
 * These methods respond to weather codes to either set the icons to use in the forecast
 * screens or to change the background.
 *
 * @author 640010970
 * @version 1.1.0
 */
public class ViewHelper {

    /**
     * Default Constructor.
     */
    public ViewHelper() { }

    /**
     * Sets the ImageView specified to an icon representing the weather at the given time-stamp.
     * @param context The context where the resources can be retrieved.
     * @param weather The weather code which decides which icon to display.
     * @param image The ImageView that is to be changed.
     * @param time The time to check if a nighttime icon should be shown instead of a daytime icon.
     * @param temperature The temperature at a given time-stamp. If it is hot or cold enough special
     *                    icons are used.
     */
    void setWeatherImage(Context context, int weather, ImageView image, int time, double temperature) {
        // Freezing temperatures.
        if(temperature < 0) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cold));
            return;
        }

        // Boiling temperatures.
        else if(temperature > 30) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hot));
            return;
        }

        // The weather is stormy.
        if(weather >= 200 && weather < 300) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightstorm));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_storm));
            }
        }

        // The weather is drizzle.
        else if(weather >= 300 && weather < 400) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightrain));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_drizzle));
            }
        }

        // The weather is light rain.
        else if(weather >= 500 && weather < 502) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightrain));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lightrain));
            }
        }

        // The weather is heavy rain.
        else if(weather >= 502 && weather < 600) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heavyrain));
        }

        // The weather is snowy.
        else if(weather >= 600 && weather < 700) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_snow));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightsnow));
            }
        }

        // The weather is misty.
        else if(weather >= 700 && weather < 800) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightmist));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mist));
            }
        }

        // The weather is clear.
        else if(weather == 800) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_moon));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sun));
            }
        }

        // The weather is cloudy.
        else if(weather == 801 || weather == 802) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightcloud));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lightcloud));
            }
        }

        // The weather is very cloudy.
        else if(weather == 803 || weather == 804) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heavycloud));
        }

        // There is a hurricane.
        else if(weather >= 900 && weather < 903) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hurricane));
        }

        // The weather is extremely cold.
        else if(weather == 903) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cold));
        }

        // The weather is extremely cold.
        else if(weather == 904) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hot));
        }

        // It is windy.
        else {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightwind));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wind));
            }
        }
    }

    /**
     * Sets the background of the Main and Detailed Activites based on the
     * weather at the time-stamp closest to the current time.
     * @param forecast The forecast object to get the weather code and temperature from.
     * @param image The ImageView to set.
     * @param resources The resources object to retrieve vector drawables with.
     */
    public static void setBackground
            (FiveDayForecast forecast, ImageView image, Resources resources) {
        int code = forecast.getWeatherCode();
        double temperature = forecast.getTemperature();
        int time = DateHandler.getHour(forecast.getDateTime());

        if(time > 19 || time < 6) {
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_nightback));
            return;
        }

        if(temperature < -5) {
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_coldbackground));
            return;
        }

        else if(temperature > 30) {
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_hotback));
            return;
        }

        if(code >= 300 && code < 502) {
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_rainyback));
        }

        else if(code > 800 && code < 805) {
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_cloudbackground));
        }

        else if(code == 800) {
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_sunnyback));
        }

        else if(code >= 600 && code < 700) {
            image.setImageDrawable(resources.getDrawable(R.drawable.ic_snowback));
        }
    }

    /**
     * Gets the temperatures for a number of FiveDayForecast objects and returns a
     * collection of them.
     * @param fiveDayForecasts The forecasts.
     * @return ArrayList<double> The temperatures that have been extracted.
     */
    public ArrayList<Double> getTemperatures(ArrayList<FiveDayForecast> fiveDayForecasts) {
        ArrayList<Double> temperatures = new ArrayList<>();
        for(int i = 0; i < fiveDayForecasts.size(); i++) {
            temperatures.add(fiveDayForecasts.get(i).getTemperature());
        }
        return temperatures;
    }
}
