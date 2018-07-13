package com.tomasmichalkevic.seevilnius.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

//Influenced by: https://github.com/udacity/ud851-Exercises/blob/student/Lesson09b-ToDo-List-AAC/T09b.10-Exercise-AddViewModelToAddTaskActivity/app/src/main/java/com/example/android/todolist/database/AppDatabase.java
@Database(entities = {PlaceEntry.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class, BooleanConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "place";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME).fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PlaceDao placeDao();

}
