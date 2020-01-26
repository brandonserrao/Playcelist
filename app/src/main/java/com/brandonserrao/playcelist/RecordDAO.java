package com.brandonserrao.playcelist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface RecordDAO {

    @Insert
    public void insert(Record record);

    @Update
    public void update(Record record);

    @Delete
    public void delete(Record record);

    @Query("SELECT * FROM RECORDS")
    public List<Record> getAllRecords();

    @Query("SELECT * FROM RECORDS WHERE ( isLIST = 0 ) ")
    public List<Record> getAllSongs();

    @Query("SELECT * FROM RECORDS WHERE ( isLIST = 1) ")
    public List<Record> getAllLists();

    @Query ("SELECT * FROM RECORDS WHERE ( NAME LIKE '%' || :search_term || '%' ) ")
    public List<Record> searchRecordsByName(String search_term);

    @Query ("SELECT * FROM RECORDS WHERE ( ( NAME LIKE '%' || :search_term || '%' ) AND (isLIST = 0) ) ")
    public List<Record> searchSongsByName(String search_term);

    @Query ("SELECT * FROM RECORDS WHERE ( ( NAME LIKE '%' || :search_term || '%' ) AND (isLIST = 1) ) ")
    public List<Record> searchListsByName(String search_term);


    @Query("DELETE FROM RECORDS WHERE (NAME LIKE '%' || :search_term || '%') AND (isLIST = 0)")
    public void deleteSongSearchResults(String search_term);

    @Query("DELETE FROM RECORDS WHERE (NAME LIKE '%' || :search_term || '%') AND (isLIST = 1)")
    public void deleteListSearchResults(String search_term);


    @Query("DELETE FROM RECORDS WHERE (UID = :uid )")
    public void deleteRecordByUID(String uid);


    @Query("SELECT LAT FROM RECORDS WHERE (UID = :uid)")
    public float getLatByUid(String uid);

    @Query("SELECT LNG FROM RECORDS WHERE (UID = :uid)")
    public float getLngByUid(String uid);

    @Query("SELECT S_ID FROM RECORDS WHERE (UID = :uid)")
    public String getSidByUid(String uid);

    @Query("SELECT ARTIST FROM RECORDS WHERE (UID = :uid)")
    //Todo change to ... SELECT ZOOM FROM ... once double ZOOM is in db
    public String getZoomLevelByUid(String uid);

}