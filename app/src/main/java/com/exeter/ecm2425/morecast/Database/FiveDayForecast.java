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
}
