package com.brandonserrao.playcelist;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SONGS")
public class Song {

    @PrimaryKey//(autoGenerate = true)
    @NonNull
    private String UID;
    private String SONG_ID;
    private String LAT;
    private String LNG;
    private String NAME;

    public String getUID() {
        return UID;
    }
    public void setUID(@NonNull String UID) {
        this.UID = UID;
    }

    public String getSONG_ID() {
        return SONG_ID;
    }

    public void setSONG_ID(String SONG_ID) {
        this.SONG_ID = SONG_ID;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLNG() {
        return LNG;
    }

    public void setLNG(String LNG) {
        this.LNG = LNG;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

}
