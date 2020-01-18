package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

    String db_name = "sqlstudio_db2_v5.sqlite";
    SongDAO songdao;
    List<Song> song_list;
    ListAdapter listAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

//----obsolete db loading method; from tutorial
/*        final File dbFile = this.getDatabasePath(db_name);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFile(dbFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class,db_name)
                        .allowMainThreadQueries()
                        .build();*/
//------

        //create db instance for this activity
        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class,db_name)
                        .allowMainThreadQueries()
                        .createFromAsset(db_name)
                        .build();
        //setup recycler view and contents
        songdao = database.getSongDAO();
        song_list = songdao.getAllSongs();
        List<Song> list_values = song_list;
        recyclerView=(RecyclerView)findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(list_values);
        recyclerView.setAdapter(listAdapter);
    }



    //button functions
    public void onClickStartMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void searchSongsByName(View view) {
        EditText et = findViewById(R.id.edittext_searchbar);
        String search_term = et.getText().toString();
        List<Song> search_results = songdao.searchSongsByName(search_term);
        listAdapter = new ListAdapter(search_results);
        recyclerView.setAdapter(listAdapter);
    }

    public void onClickDeleteResults(View view) {
        EditText et = findViewById(R.id.edittext_searchbar);
        String search_term = et.getText().toString();
        songdao.deleteSearchResults(search_term);
        et.setText("");

        List<Song> songs = songdao.getAllSongs();
        listAdapter = new ListAdapter(songs);
        recyclerView.setAdapter(listAdapter);
        //recreate();
    }

    //-----obsolete db init function; from tutorials
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
