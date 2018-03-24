package com.exeter.ecm2425.morecast.Views;


import android.content.Context;
import android.widget.ImageView;

import com.exeter.ecm2425.morecast.R;

class ViewHelper {
    ViewHelper() { }

    void setWeatherImage(Context context, int weather, ImageView image, int time) {
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
}
