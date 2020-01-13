package com.brandonserrao.playcelist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SongDAO {
    @Insert
    public void insert(Song song);

    @Update
    public void update(Song song);

    @Delete
    public void delete(Song song);

    @Query("SELECT * FROM SONGS")
    public List<Song> getAllSongs();
}
