package com.exeter.ecm2425.morecast.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {FiveDayForecast.class}, version = 1)
public abstract class MorecastDatabase extends RoomDatabase {
    public abstract FiveDayForecastDao getFiveDayForecastDao();
}
