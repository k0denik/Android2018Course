package com.afl.przedszkolelabapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Jakub Pamuła on 01/05/2018.
 */

@Entity(tableName = "child_table")
public class Child {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    String name;
    String surname;
    String textFilePath;
    String imagePath;

    public Child(@NonNull String name, @NonNull String surname, String textFilePath, String imagePath) {
        this.name = name;
        this.surname = surname;
        this.textFilePath = textFilePath;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTextFilePath() {
        return textFilePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
