package com.example.musicplayer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.musicplayer.entity.MusicBean;

@Database(entities = {FilterBean.class, MusicBean.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase sInstance;

    public static final MyDatabase getInstance(Context context) {
        synchronized (MyDatabase.class) {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context, MyDatabase.class, "my_database").allowMainThreadQueries().build();
            }
            return sInstance;
        }
    }

    public abstract FilterDao getFilterDao();

    public abstract MusicDao getMusicDao();
}
