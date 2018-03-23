package com.exeter.ecm2425.morecast.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FiveDayForecastDao {
    @Query("SELECT * from FiveDayForecast")
    List<FiveDayForecast> getFiveDayForecast();

    @Insert
    void insertFiveDayForecast(FiveDayForecast... fiveDayForecasts);

    @Delete
    void delete(FiveDayForecast... fiveDayForecasts);
}
