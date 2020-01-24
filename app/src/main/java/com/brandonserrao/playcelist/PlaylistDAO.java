package com.brandonserrao.playcelist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaylistDAO {

    @Insert
    public void insert(Playlist playlist);

    @Update
    public void update(Playlist playlist);

    @Delete
    public void delete(Playlist playlist);

    @Query("SELECT * FROM LISTS")
    public List<Playlist> getAllPlaylists();

    @Query ("SELECT * FROM LISTS WHERE ( NAME LIKE '%' || :search_term || '%' ) ")
    public List<Playlist> searchPlaylistsByName(String search_term);

    @Query("DELETE FROM LISTS WHERE (NAME LIKE '%' || :search_term || '%')")
    public void deleteSearchResults(String search_term);

}
