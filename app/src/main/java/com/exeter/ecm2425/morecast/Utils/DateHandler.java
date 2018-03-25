package com.exeter.ecm2425.morecast.Utils;

import java.util.Calendar;
import java.util.Date;


public class DateHandler {
    public DateHandler() {}

    public static String returnDayOfTheWeek(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek;

        switch(day) {
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;

            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;

            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;

            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;

            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;

            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;

            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;

            default:
                dayOfWeek = "Monday";

        }
        return dayOfWeek;
    }

    public static int getLocaleHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getHour(String timeStamp) {
        String hour = timeStamp.split("\\s+")[1].substring(0, 2);
        return Integer.parseInt(hour);
    }

    public static Long getDeviceEpochTime() {
        return System.currentTimeMillis() / 1000;
    }
}
