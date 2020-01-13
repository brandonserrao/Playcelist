package com.brandonserrao.playcelist;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SONGS")
public class Song {
    @PrimaryKey
    @NonNull
    private int UID;
    private String NAME;
    private String SONG_ID;
    private double LAT;
    private double LNG;

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSONG_ID() {
        return SONG_ID;
    }

    public void setSONG_ID(String SONG_ID) {
        this.SONG_ID = SONG_ID;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public double getLNG() {
        return LNG;
    }

    public void setLNG(double LNG) {
        this.LNG = LNG;
    }
}
