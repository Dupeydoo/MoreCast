package com.exeter.ecm2425.morecast.Database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FiveDayForecast {
    @PrimaryKey
    @ColumnInfo(name = "epoch-time")
    private Long epochTime;

    @ColumnInfo(name = "temperature")
    private double temperature;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "pressure")
    private double pressure;

    @ColumnInfo(name = "humidity")
    private int humidity;

    @ColumnInfo(name = "wind-speed")
    private double windSpeed;

    @ColumnInfo(name = "wind-degree")
    private double windDegree;

    @ColumnInfo(name = "precipitation-type")
    private String precipitationType;

    @ColumnInfo(name = "precipitation-amount")
    private double precipitationAmount;

    @ColumnInfo(name = "date-time")
    private String dateTime;

    @ColumnInfo(name = "weather-code")
    private int weatherCode;

    public Long getEpochTime() {
        return epochTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public double getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public String getPrecipitationType() {
        return precipitationType;
    }

    public double getPrecipitationAmount() {
        return precipitationAmount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setEpochTime(Long epochTime) {
        this.epochTime = epochTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }

    public void setPrecipitationType(String precipitationType) {
        this.precipitationType = precipitationType;
    }

    public void setPrecipitationAmount(double precipitationAmount) {
        this.precipitationAmount = precipitationAmount;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }
}
