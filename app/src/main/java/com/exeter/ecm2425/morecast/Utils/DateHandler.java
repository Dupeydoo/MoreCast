package com.exeter.ecm2425.morecast.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


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

    public static int getHour(String timeStamp) {
        String hour = timeStamp.split("\\s+")[1].substring(0, 2);
        return Integer.parseInt(hour);
    }

    public static Long getDeviceEpochTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String getDateStringFromEpoch(Long epoch) {
        Date adjustedDate = new Date(epoch);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(adjustedDate);
    }
}
