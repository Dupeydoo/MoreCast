package com.exeter.ecm2425.morecast.Views;


import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.exeter.ecm2425.morecast.Database.FiveDayForecast;
import com.exeter.ecm2425.morecast.R;

import java.util.ArrayList;

public class ViewHelper {
    public ViewHelper() { }

    void setWeatherImage(Context context, int weather, ImageView image, int time, double temperature) {
        if(temperature < 0) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cold));
            return;
        }

        else if(temperature > 30) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hot));
            return;
        }

        if(weather >= 200 && weather < 300) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightstorm));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_storm));
            }
        }

        else if(weather >= 300 && weather < 400) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightrain));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_drizzle));
            }
        }

        else if(weather >= 500 && weather < 502) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightrain));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lightrain));
            }
        }

        else if(weather >= 502 && weather < 600) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heavyrain));
        }

        else if(weather >= 600 && weather < 700) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_snow));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightsnow));
            }
        }

        else if(weather >= 700 && weather < 800) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightmist));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mist));
            }
        }

        else if(weather == 800) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_moon));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sun));
            }
        }

        else if(weather == 801 || weather == 802) {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightcloud));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lightcloud));
            }
        }

        else if(weather == 803 || weather == 804) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heavycloud));
        }

        else if(weather >= 900 && weather < 903) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hurricane));
        }

        else if(weather == 903) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cold));
        }

        else if(weather == 904) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_hot));
        }

        else {
            if(time > 19 || time < 6) {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nightwind));
            } else {
                image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wind));
            }
        }
    }

    public void setBackground(int code, ImageView image, Resources resources, double temperature) {
        if(temperature < 0) {
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
    }

    public ArrayList<Double> getTemperatures(ArrayList<FiveDayForecast> fiveDayForecasts) {
        ArrayList<Double> temperatures = new ArrayList<>();
        for(int i = 0; i < fiveDayForecasts.size(); i++) {
            temperatures.add(fiveDayForecasts.get(i).getTemperature());
        }
        return temperatures;
    }
}
