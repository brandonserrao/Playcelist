package com.brandonserrao.playcelist;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "inLIST")
public class inPlaylist {

    @PrimaryKey
    @NonNull
    Integer UID;
    Integer UID_SONG;
    Integer UID_LIST;

    @NonNull
    public Integer getUID() {
        return UID;
    }

    public void setUID(@NonNull Integer UID) {
        this.UID = UID;
    }

    public Integer getUID_SONG() {
        return UID_SONG;
    }

    public void setUID_SONG(Integer UID_SONG) {
        this.UID_SONG = UID_SONG;
    }

    public Integer getUID_LIST() {
        return UID_LIST;
    }

    public void setUID_LIST(Integer UID_LIST) {
        this.UID_LIST = UID_LIST;
    }
}
