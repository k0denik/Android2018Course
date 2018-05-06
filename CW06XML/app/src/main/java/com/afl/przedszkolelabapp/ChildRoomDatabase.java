package com.afl.przedszkolelabapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Jakub Pamuła on 01/05/2018.
 */

@Database(entities = {Child.class}, version = 1, exportSchema = false)
public abstract class ChildRoomDatabase extends RoomDatabase {
    private static ChildRoomDatabase INSTANCE;

    public abstract ChildDao childDao();

    public static ChildRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (ChildRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, ChildRoomDatabase.class, "child_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
