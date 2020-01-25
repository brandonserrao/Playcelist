package com.brandonserrao.playcelist;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Record.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{
    public abstract RecordDAO getRecordDAO();
}
