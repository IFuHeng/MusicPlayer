package com.example.musicplayer.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.musicplayer.entity.MusicBean;

import java.util.List;

@Dao
public interface MusicDao {
    @Insert
    void insert(MusicBean... beans);

    @Update
    void update(MusicBean... beans);

    @Delete
    void delete(MusicBean... beans);

    @Query("DELETE FROM MusicBean")
    void clean();

    @Query("SELECT * FROM MusicBean ORDER BY ID DESC")
    List<MusicBean> getAll();
}
