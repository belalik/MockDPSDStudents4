package com.example.android.mockdpsdstudents4.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DPSDStudentEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DPSDStudentDAO dpsdStudentDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
