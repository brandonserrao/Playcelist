package com.brandonserrao.playcelist;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SONGS")
public class Song {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer UID;
    String SONG_ID;
    float LAT;
    float LNG;
    String NAME;
    //Todo add field for Artistname
    //String ARTIST;


    public String getSONG_ID() {
        return SONG_ID;
    }

    public void setSONG_ID(String SONG_ID) {
        this.SONG_ID = SONG_ID;
    }


    public void setUID(@NonNull Integer UID) {
        this.UID = UID;
    }

    @NonNull
    public Integer getUID() {
        return UID;
    }


    public float getLAT() {
        return LAT;
    }

    public void setLAT(float LAT) {
        this.LAT = LAT;
    }


    public float getLNG() {
        return LNG;
    }

    public void setLNG(float LNG) {
        this.LNG = LNG;
    }


    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    /*
    Todo activate after field has been added:
     public void setARTIST(String ARTIST) { this.ARTIST = ARTIST; }
    */
}
