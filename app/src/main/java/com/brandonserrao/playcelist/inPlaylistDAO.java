package com.brandonserrao.playcelist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

    @Dao
    public interface inPlaylistDAO {

        @Insert
        public void insert(inPlaylist inplaylist);

        @Update
        public void update(inPlaylist inplaylist);

        @Delete
        public void delete(inPlaylist inplaylist);

        @Query("SELECT * FROM LISTS")
        public List<inPlaylist> getAllinPlaylists();

    }
