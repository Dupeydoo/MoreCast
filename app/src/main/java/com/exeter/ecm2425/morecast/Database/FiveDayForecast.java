package com.exeter.ecm2425.morecast.Database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Room entity class representing a single time-stamp for a FiveDatForecast
 * from the OpenWeatherMap API. Each object holds the most useful information
 * needed to display to the user. Parcelable is implemented for high speed
 * exchange of the object between activities and bundles.
 *
 * Each attribute of this class corresponds to a column in the database that
 * Room manages.
 *
 * @author 640010970
 * @version 2.0.0
 */

@Entity
public class FiveDayForecast implements Parcelable {

    /**
     * Default constructor.
     */
    public FiveDayForecast() { }

    /**
     * Parcelable constructor invoked when
     * parcelling.
     * @param parcel The parcel to to read from.
     */
    public FiveDayForecast(Parcel parcel) {
        epochTime = parcel.readLong();
        temperature = parcel.readDouble();
        description = parcel.readString();
        pressure = parcel.readDouble();
        humidity = parcel.readInt();
        windSpeed = parcel.readDouble();
        windDegree = parcel.readDouble();
        precipitationType = parcel.readString();
        precipitationAmount = parcel.readDouble();
        dateTime = parcel.readString();
        utcDateTime = parcel.readString();
        timeZoneName = parcel.readString();
        weatherCode = parcel.readInt();
    }

    /**
     * Primary Key epoch time. Adjusted for
     * the current timezone.
     */
    @PrimaryKey
    @ColumnInfo(name = "epoch-time")
    private Long epochTime;

    /**
     * The current temperature at the time.
     */
    @ColumnInfo(name = "temperature")
    private double temperature;

    /**
     * A description of the weather conditions.
     */
    @ColumnInfo(name = "description")
    private String description;

    /**
     * The name of the current time zone.
     */
    @ColumnInfo(name = "time-zone")
    private String timeZoneName;

    /**
     * The pressure at the current time.
     */
    @ColumnInfo(name = "pressure")
    private double pressure;

    /**
     * The humidity at the current time.
     */
    @ColumnInfo(name = "humidity")
    private int humidity;

    /**
     * The wind speed at the current time.
     */
    @ColumnInfo(name = "wind-speed")
    private double windSpeed;

    /**
     * The wind direction in degrees at
     * the current time.
     */
    @ColumnInfo(name = "wind-degree")
    private double windDegree;

    /**
     * The type of precipitation that has
     * occurred in the last 3 hours.
     * Snow takes precedence over rain.
     */
    @ColumnInfo(name = "precipitation-type")
    private String precipitationType;

    /**
     * The amount of precipitation that has
     * fallen in the last 3 hours.
     */
    @ColumnInfo(name = "precipitation-amount")
    private double precipitationAmount;

    /**
     * A string representing the current
     * time of the forecast in the form:
     * "yyyy-MM-dd HH:mm:ss".
     */
    @ColumnInfo(name = "date-time")
    private String dateTime;

    /**
     * The above dateTime but in UTC time.
     * Allows days to be split easily as well
     * as dealing with unusual locations like
     * Antarctica.
     */
    @ColumnInfo(name = "utc-date-time")
    private String utcDateTime;

    /**
     * An integer code from the API representing
     * a type of weather. See here for more information:
     * https://openweathermap.org/weather-conditions.
     */
    @ColumnInfo(name = "weather-code")
    private int weatherCode;


    public Long getEpochTime() { return epochTime; }

    public double getTemperature() { return temperature; }

    public String getDescription() { return description; }

    public double getPressure() { return pressure; }

    public int getHumidity() { return humidity; }

    public double getWindSpeed() { return windSpeed; }

    public double getWindDegree() { return windDegree; }

    public String getPrecipitationType() { return precipitationType; }

    public double getPrecipitationAmount() { return precipitationAmount; }

    public String getDateTime() { return dateTime; }

    public String getUtcDateTime() { return utcDateTime; }

    public String getTimeZoneName() { return timeZoneName; }

    public int getWeatherCode() { return weatherCode; }

    public void setEpochTime(Long epochTime) { this.epochTime = epochTime; }

    public void setDescription(String description) { this.description = description; }

    public void setTemperature(double temperature) { this.temperature = temperature; }

    public void setPressure(double pressure) { this.pressure = pressure; }

    public void setHumidity(int humidity) { this.humidity = humidity; }

    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public void setWindDegree(double windDegree) { this.windDegree = windDegree; }

    public void setPrecipitationType(String precipitationType) {
        this.precipitationType = precipitationType;
    }

    public void setPrecipitationAmount(double precipitationAmount) {
        this.precipitationAmount = precipitationAmount;
    }

    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public void setUtcDateTime(String utcDateTime) { this.utcDateTime = utcDateTime; }

    public void setTimeZoneName(String timeZoneName) { this.timeZoneName = timeZoneName; }

    public void setWeatherCode(int weatherCode) { this.weatherCode = weatherCode; }

    /**
     * {@inheritDoc}
     * Writes a FiveDayForecast to a parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
         parcel.writeLong(epochTime);
         parcel.writeDouble(temperature);
         parcel.writeString(description);
         parcel.writeDouble(pressure);
         parcel.writeInt(humidity);
         parcel.writeDouble(windSpeed);
         parcel.writeDouble(windDegree);
         parcel.writeString(precipitationType);
         parcel.writeDouble(precipitationAmount);
         parcel.writeString(dateTime);
         parcel.writeString(utcDateTime);
         parcel.writeString(timeZoneName);
         parcel.writeInt(weatherCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * Implementation of the Creator interface, required to parcel an object
     * or ArrayList of that object.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        /**
         * Create a forecast object from a parcel.
         * @param parcel The parcel to read from.
         * @return FiveDayForecast A forecast object.
         */
        public FiveDayForecast createFromParcel(Parcel parcel) {
            return new FiveDayForecast(parcel);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FiveDayForecast[] newArray(int size) {
            return new FiveDayForecast[size];
        }
    };
}
