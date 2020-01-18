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

    @Query ("SELECT * FROM SONGS WHERE ( NAME LIKE '%' || :search_term || '%' ) ")
    public List<Song> searchSongsByName(String search_term);

    @Query("DELETE FROM SONGS WHERE (NAME LIKE '%' || :search_term || '%')")
    public void deleteSearchResults(String search_term);

}
