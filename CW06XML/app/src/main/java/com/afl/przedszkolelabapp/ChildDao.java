package com.afl.przedszkolelabapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Jakub Pamuła on 01/05/2018.
 */
@Dao
public interface ChildDao {
    @Insert
    void insert(Child child);

    @Query("SELECT * FROM child_table ORDER BY surname")
    LiveData<List<Child>> getAllChildren();

    @Query("DELETE FROM child_table")
    void clearDatabase();
}
