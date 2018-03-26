package com.exeter.ecm2425.morecast.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Singleton abstract class providing access to the database itself.
 * Exposes an instance of the database to the application
 * and implements retrieval of data access methods.
 *
 * @author 640010970
 * @version 1.0.0
 */
@Database(entities = {FiveDayForecast.class}, version = 1)
public abstract class MorecastDatabase extends RoomDatabase {

    // The single instance of the database.
    private static MorecastDatabase singleton;

    // Retrieves the DAO object to make calls on.
    public abstract FiveDayForecastDao getFiveDayForecastDao();

    /**
     * Gets the instance of the Morecast database.
     * @param context The context requesting the database.
     * @return MorecastDatabase The database object.
     */
    public static MorecastDatabase getMorecastDatabase(Context context) {
        if (singleton == null) {
            // Construct the database if it does not already exist.
            singleton = Room.databaseBuilder(context.getApplicationContext(),
                    MorecastDatabase.class, "MorecastDatabase").build();
        }
        return singleton;
    }

    /**
     * Destroys the single instance if invoked.
     */
    public static void destroyInstance() {
        singleton = null;
    }
}
