package com.brandonserrao.playcelist;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "RECORDS"
/*      //example foreignkey room notation in java
, foreignKeys = @ForeignKey(entity = inPlaylist.class,
        parentColumns = "SONG_UID",
        childColumns = "UID",
        onDelete = ForeignKey.CASCADE)*/
)

public class Record {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer UID;
    String S_ID;

    float LAT;
    float LNG;

    String ARTIST; //Todo zoomlevels for lists are to be saved here

    String NAME;

    String LIST_ITEMS;
    Boolean isLIST;

    @NonNull
    public Integer getUID() {
        return UID;
    }

    public void setUID(@NonNull Integer UID) {
        this.UID = UID;
    }

    public String getS_ID() {
        return S_ID;
    }

    public void setS_ID(String s_ID) {
        S_ID = s_ID;
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

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getLIST_ITEMS() {
        return LIST_ITEMS;
    }

    public void setLIST_ITEMS(String LIST_ITEMS) {
        this.LIST_ITEMS = LIST_ITEMS;
    }

    public Boolean getIsLIST() {
        return isLIST;
    }

    public void setIsLIST(Boolean isLIST) {
        this.isLIST = isLIST;
    }
}
