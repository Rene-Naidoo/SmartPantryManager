package com.rene.smartpantrymanager.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * The main database class. Room generates the real SQLite code for it.
 * It lists every entity (table) and exposes every DAO.
 */
@Database(entities = {PantryItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Room implements this method for us
    public abstract PantryItemDao pantryItemDao();

    // Single shared instance (singleton)
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "smart_pantry.db")
                            .allowMainThreadQueries() // fine for a small assignment app
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}