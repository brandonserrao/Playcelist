package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    String db_name = "sqlstudio_db2_v4.sqlite";
    SongDAO songdao;
    List<Song> song_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //testing
        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class,db_name)
                        .allowMainThreadQueries()
                        .build();

        songdao = database.getSongDAO();
        song_list = songdao.getAllSongs();
        List<Song> list_values = song_list;
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter listAdapter = new ListAdapter(list_values);
        recyclerView.setAdapter(listAdapter);
    }


    public void onClickStartMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    private void  copyDatabaseFile(String destinationPath) throws IOException {
        InputStream assetsDB = this.getAssets().open(db_name);
        OutputStream dbOut = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = assetsDB.read(buffer)) > 0) {
            dbOut.write(buffer, 0, length);
        }
        dbOut.flush();
        dbOut.close();
    }



}
