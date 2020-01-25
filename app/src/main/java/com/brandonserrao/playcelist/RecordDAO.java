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

    @Query("DELETE FROM RECORDS WHERE (NAME LIKE '%' || :search_term || '%')")
    public void deleteSearchResults(String search_term);

}