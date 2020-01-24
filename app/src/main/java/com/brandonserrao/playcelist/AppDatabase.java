package com.brandonserrao.playcelist;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class, Playlist.class, inPlaylist.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{
    public abstract SongDAO getSongDAO();
    public abstract PlaylistDAO getPlaylistDAO();
    public abstract inPlaylistDAO getinPlaylistDAO();

}
