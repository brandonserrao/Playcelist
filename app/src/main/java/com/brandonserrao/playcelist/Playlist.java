package com.brandonserrao.playcelist;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "LISTS"
/*      //example foreignkey room notation in java
, foreignKeys = @ForeignKey(entity = inPlaylist.class,
        parentColumns = "LIST_UID",
        childColumns = "UID",
        onDelete = ForeignKey.CASCADE)*/
)

public class Playlist {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer UID;
    String LIST_ID;
    String NAME;

    @NonNull
    public Integer getUID() {
        return UID;
    }

    public void setUID(@NonNull Integer UID) {
        this.UID = UID;
    }

    public String getLIST_ID() {
        return LIST_ID;
    }

    public void setLIST_ID(String LIST_ID) {
        this.LIST_ID = LIST_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
