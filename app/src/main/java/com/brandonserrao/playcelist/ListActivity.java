package com.brandonserrao.playcelist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
                Room.databaseBuilder(this, AppDatabase.class, db_name)
                        .allowMainThreadQueries()
                        .createFromAsset(db_name)
                        .build();
        //setup recycler view and contents
        songdao = database.getSongDAO();
        song_list = songdao.getAllSongs();
        List<Song> list_values = song_list;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(list_values);
        recyclerView.setAdapter(listAdapter);
    }


    //button functions
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
    private void copyDatabaseFile(String destinationPath) throws IOException {
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

    //navigates to songs activity
    public void onClickStartSongsActivity(MenuItem item) {
        Intent intent = new Intent(this, SongsActivity.class);
        startActivity(intent);
    }

    //navigates to main (map) activity
    public void onClickStartMainActivity(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //navigates to list activity
    public void onClickStartListsActivity(MenuItem item) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    //to main activity via intent, center and zoom to list on map
    public void viewListOnMap(View view) {
        View contextView = findViewById(R.id.btn_showListOnMap);
        Snackbar.make(contextView, R.string.showsListOnMap, Snackbar.LENGTH_SHORT)
                .show();
        //Intent intent = new Intent(this, MainActivity.class);
        // -
        // -
        //startActivity(intent);
        // -
        // -
        //send id for map to center on corresponding list circle
        // -
        // -
    }

    //sending the listID to spotify to play
    public void API_playThisList(View view) {
        View contextView = findViewById(R.id.iv);
        Snackbar.make(contextView, R.string.play_playcelist, Snackbar.LENGTH_SHORT)
                .show();
        //actual code:
        // get listID from db item
        // -
        // -
        // -
        // send intent(?) via API to play/shuffle list
        // -
        // -
        // -
    }

    public interface delListener{
        public void onClick();
    }

    //opens a dialog to confirm deleting the List
    public void onClickOpenListDeleteDialog(View view) {
        //to get the relative Layout for the item: view.getParent().getParent();
        new MaterialAlertDialogBuilder(this, R.style.DialogTheme)
                .setMessage("Do you want to delete this playcelist?")
                .setNeutralButton("cancel", null)
                .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ListActivity.this, R.string.btnWillDeleteList, Toast.LENGTH_SHORT).show();
                    }
                })
                // .setNegativeButtonIcon(getDrawable(R.drawable.delete))
                .show();
        //code to delete the list from DB & Spotify:
        // -
        // -
        // -
    }
}
