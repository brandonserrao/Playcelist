package com.brandonserrao.playcelist;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "SONGS"
/*      //example foreignkey room notation in java
, foreignKeys = @ForeignKey(entity = inPlaylist.class,
        parentColumns = "SONG_UID",
        childColumns = "UID",
        onDelete = ForeignKey.CASCADE)*/
)

public class Song {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer UID;
    String SONG_ID;
    float LAT;
    float LNG;
    String NAME;


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
}
