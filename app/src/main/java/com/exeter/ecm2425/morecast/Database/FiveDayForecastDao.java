package com.exeter.ecm2425.morecast.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;


/**
 * The data access interface that provides query methods
 * to a Room database. Called by AccessDatabase to invoke
 * queries on the database.
 *
 * @author 640010970
 * @version 1.0.0
 */
@Dao
public interface FiveDayForecastDao {

    /**
     * Select everything from the database into a List.
     * @return List<FiveDayForecast> The forecast data
     *                               in the database.
     */
    @Query("SELECT * from FiveDayForecast")
    List<FiveDayForecast> getFiveDayForecast();

    /**
     * Inserts a provided array of FiveDayForecast objects
     * into the database.
     * @param fiveDayForecasts The array of objects that are
     *                         inserted.
     */
    @Insert
    void insertFiveDayForecast(FiveDayForecast... fiveDayForecasts);

    /**
     * Deletes the contents of the database. Used to clear
     * the database when a fresh and up to date API call is
     * made.
     */
    @Query("DELETE from FiveDayForecast")
    void destroyTable();
}
