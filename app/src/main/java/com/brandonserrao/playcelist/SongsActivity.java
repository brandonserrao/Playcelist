package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/*import static com.brandonserrao.playcelist.MainActivity.db_name;
import static com.brandonserrao.playcelist.MainActivity.song_list;
import static com.brandonserrao.playcelist.MainActivity.recorddao;*/

public class SongsActivity extends AppCompatActivity {

    public String db_name = "playcelist_db_v8.sqlite";
    //public String db_name = "sqlstudio_db2_v5.sqlite";
    RecordDAO recorddao;
    List<Record> song_list;

    SongsAdapter songsAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        //create db instance for this activity
        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class, db_name)
                        .allowMainThreadQueries()
                        .createFromAsset(db_name)
                        .build();
        //setup recycler view and contents
        recorddao = database.getRecordDAO();
        song_list = recorddao.getAllSongs();
        List<Record> list_values = song_list;
        recyclerView=(RecyclerView)findViewById(R.id.recycler_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsAdapter = new SongsAdapter(list_values);
        recyclerView.setAdapter(songsAdapter);
    }


    //button functions
    public void searchSongsByName(View view) {
        EditText et = findViewById(R.id.edittext_searchbar);
        String search_term = et.getText().toString();
        List<Record> search_results = recorddao.searchSongsByName(search_term);
        songsAdapter = new SongsAdapter(search_results);
        recyclerView.setAdapter(songsAdapter);
    }

    public void onClickDeleteResults(View view) {
        EditText et = findViewById(R.id.edittext_searchbar);
        String search_term = et.getText().toString();
        recorddao.deleteSongSearchResults(search_term);
        et.setText("");
        List<Record> songs = recorddao.getAllSongs();
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
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    //to main activity via intent, center and zoom to song position on map
    public void viewSongOnMap(View view) {
        //Todo specifiy the item that was clicked on to get that specific item's lat+lng
        // atm the tv s of the first item on the list are being returned
        TextView lngtv = findViewById(R.id.tv3);
        String lng = (String) lngtv.getText();
        TextView lattv = findViewById(R.id.tv4);
        String lat = (String) lattv.getText();

        View contextView = findViewById(R.id.btn_showSongOnMap);
        Snackbar.make(contextView, lng + "   " + lat, Snackbar.LENGTH_SHORT)
                .show();
        /*
        Todo
         send latlng via intent to map to center and zoom
         Intent intent = new Intent(this, MainActivity.class);
         startActivity(intent);
        */
    }

    //retrieves and sends the songID to spotify to play
    public void API_playThisSong(View view) {
        //Todo specifiy the item that was clicked on to get that specific item's SongID
        TextView idtv = findViewById(R.id.tv5);
        String sID = (String) idtv.getText();
        View contextView = findViewById(R.id.btn_playSong);
        Snackbar.make(contextView, R.string.play_song + sID, Snackbar.LENGTH_SHORT)
                .show();
        /*
        Todo
         send intent(?) via API to play song
        */
    }

    //opens a dialog to confirm deleting the Song
    //deletes song from the DB
    public void onClickOpenSongDeleteDialog(View view) {
        //todo get the correct item specifications to delete
        TextView idTV = findViewById(R.id.tv5); //now this gets tv5 of the first item in the list...
        String delID = (String) idTV.getText();
        new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog)
                .setMessage("Do you want to unplayce this song?")
                .setNeutralButton("cancel", null)
                .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        songdao.deleteByID(delID);
                        List<Song> songs = songdao.getAllSongs();
                        songsAdapter = new SongsAdapter(songs);
                        recyclerView.setAdapter(songsAdapter);
                        //Todo doesn't delete anything yet...
                        Toast.makeText(SongsActivity.this, R.string.btnWillDeleteSong + delID, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}