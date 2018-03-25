package com.exeter.ecm2425.morecast.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FiveDayForecast.class}, version = 1)
public abstract class MorecastDatabase extends RoomDatabase {
    private static MorecastDatabase singleton;
    public abstract FiveDayForecastDao getFiveDayForecastDao();

    public static MorecastDatabase getMorecastDatabase(Context context) {
        if (singleton == null) {
            singleton = Room.databaseBuilder(context.getApplicationContext(),
                    MorecastDatabase.class, "MorecastDatabase").build();
        }
        return singleton;
    }

    public static void destroyInstance() {
        singleton = null;
    }
}
