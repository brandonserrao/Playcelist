package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class SongsActivity extends AppCompatActivity {

    String db_name = "sqlstudio_db2_v5.sqlite";
    SongDAO songdao;
    List<Song> song_list;
    SongsAdapter songsAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

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
        recyclerView=(RecyclerView)findViewById(R.id.recycler_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsAdapter = new SongsAdapter(list_values);
        recyclerView.setAdapter(songsAdapter);
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
        songsAdapter = new SongsAdapter(search_results);
        recyclerView.setAdapter(songsAdapter);
    }

    public void onClickDeleteResults(View view) {
        EditText et = findViewById(R.id.edittext_searchbar);
        String search_term = et.getText().toString();
        songdao.deleteSearchResults(search_term);
        et.setText("");

        List<Song> songs = songdao.getAllSongs();
        songsAdapter = new SongsAdapter(songs);
        recyclerView.setAdapter(songsAdapter);
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


    //Button Click Handlers
    public void onClickStartSongsActivity(MenuItem item) {
        Intent intent = new Intent(this, SongsActivity.class);
        startActivity(intent);
    }
    public void onClickStartMainActivity(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void onClickStartListsActivity(MenuItem item) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public void viewSongOnMap(View view) {
        View contextView = findViewById(R.id.btn_showSongOnMap);
        Snackbar.make(contextView, R.string.btnWorking, Snackbar.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //send id for map to center on corresponding song bubble
    }

    //sending the songID to spotify to play
    //DOESN'T WORK ATM
    public void API_playThisSong(View view) {
        View contextView = findViewById(R.id.iv);
        Snackbar.make(contextView, R.string.btnWorking, Snackbar.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //enter API code here
    }
}
