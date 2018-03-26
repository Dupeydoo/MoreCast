package com.exeter.ecm2425.morecast.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Provides static method for common date operations, for instance,
 * returning the day of the week based on a date.
 *
 * @author 640010970
 * @version 1.1.0
 */
public class DateHandler {

    /**
     * Default Constructor.
     */
    public DateHandler() {}

    /**
     * Returns the current day of the week based on the supplied
     * Date object.
     * @param currentDate The Date to find the dotw for.
     * @return String The day of the week.
     */
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

    /**
     * Retrieves the 24 hour clock hour from a OpenWeatherMap
     * time-stamp. Called when deciding whether to display either
     * day or night icons.
     * @param timeStamp The string time-stamp to get the hour from.
     * @return The hour parsed from the string.
     */
    public static int getHour(String timeStamp) {
        // Take the stamp, split it at a space character and take
        // the substring from after the space.
        String hour = timeStamp.split("\\s+")[1].substring(0, 2);
        return Integer.parseInt(hour);
    }

    /**
     * Returns the current epoch time of the user's device. Used
     * only as a backup option when an epoch cannot ne retrieved normally.
     * @return Long The epoch time of the device.
     */
    public static Long getDeviceEpochTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * Converts an epoch time to a date string in the format that
     * can be read by the View classes.
     * @param epoch The epoch time to convert.
     * @return String The converted date string.
     */
    public static String getDateStringFromEpoch(Long epoch) {
        Date adjustedDate = new Date(epoch);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Prevents default SimpleDateFormat behaviour.
        // TimeZone adjustment is all handled by the ForecastParser.
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(adjustedDate);
    }

    /**
     * Returns the hours and minutes section of a time-stamp.
     * @param dateTime The date string to parse.
     * @return String The time in HH:mm.
     */
    public static String parseDateTime(String dateTime) {
        return dateTime.split("\\s+")[1].substring(0, 5);
    }
}
