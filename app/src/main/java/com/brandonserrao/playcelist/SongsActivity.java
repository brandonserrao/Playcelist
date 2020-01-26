package com.brandonserrao.playcelist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
//import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/*import static com.brandonserrao.playcelist.MainActivity.db_name;
import static com.brandonserrao.playcelist.MainActivity.song_list;
import static com.brandonserrao.playcelist.MainActivity.recorddao;*/

public class SongsActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "fdcc6fcc754e42e3bc7f45f2524816f3"; //use from MAC
    //public static final String CLIENT_ID = "cff5c927f91e4e9582f97c827f8632dd"; //- use from PC;
    private static final String REDIRECT_URI = "com.brandonserrao.playcelist://callback";
    public SpotifyAppRemote mSpotifyAppRemote;
    private boolean isAppLoggedIn = false;

    public String db_name = "playcelist_db_v8.sqlite";
    //public String db_name = "sqlstudio_db2_v5.sqlite";
    RecordDAO recorddao;
    List<Record> song_list;

    SongsAdapter songsAdapter;
    RecyclerView recyclerView;
    private BottomNavigationView.OnNavigationItemSelectedListener myNavigationItemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        //initialize the bottomNavBar
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = this.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener);
        bottomNavigationView.setSelectedItemId(R.id.btn_toSongs);
        bottomNavigationView.findViewById(R.id.btn_toSongs).setClickable(false);
        bottomNavigationView.findViewById(R.id.btn_toSongs).setActivated(true);
        //Todo have the active state be represented in the style too

        //check if this is the first creation after initial spotify log in
        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        boolean isFirstTimeSongs;
        isFirstTimeSongs = pref.getBoolean("isFirstTimeSongs", true);
        if (isFirstTimeSongs) {
            //Todo edit welcome dialog
            new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog)
                    .setTitle("Welcome!")
                    .setMessage(getString(R.string.welcomeSongs))
                    .setPositiveButton("got it!", null)
                    .show();

            editor.putBoolean("isFirstTimeSongs", false);
            editor.apply();
        }

        isAppLoggedIn=pref.getBoolean("isAppLoggedIn",false);
        if (isAppLoggedIn) Log.e("SHARED", "isAppLoggedIn1");
        else Log.e("SHARED", "isAppLoggedIn0");

        //create db instance for this activity
        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class, db_name)
                        .allowMainThreadQueries()
                        .createFromAsset(db_name)
                        .build();
        //setup recycler view and contents
        recorddao = database.getRecordDAO();
        song_list = recorddao.getAllSongs();
        //reversing so most recent at top
        Collections.reverse(song_list);
        List<Record> list_values = song_list;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsAdapter = new SongsAdapter(list_values);
        recyclerView.setAdapter(songsAdapter);


        if(isAppLoggedIn) {
            SpotifyAppRemote.connect(
                    getApplication(),
                    new ConnectionParams.Builder(CLIENT_ID)
                            .setRedirectUri(REDIRECT_URI)
                            .showAuthView(true)
                            .build(),
                    new Connector.ConnectionListener() {
                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.e("SPOTIFY", " APP connected in  playslist list ");

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("SPOTIFY", " APP conn fail in  playlist llis");
                            Log.e("MyActivity", throwable.getMessage(), throwable);
                        }
                    });

        }

        else { redirectToLauncher();}

           // Toast.makeText(this, R.string.SAccountName, Toast.LENGTH_LONG).show();}


            // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.sv_songs);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svSearchSongsByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void svSearchSongsByName(String query) {
        List<Record> search_results = recorddao.searchSongsByName(query);
        songsAdapter = new SongsAdapter(search_results);
        recyclerView.setAdapter(songsAdapter);
    }

    //Todo-----obsolete db init function; from tutorials
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
    public void onClickStartSongsActivity(MenuItem item) {
        //Intent intent = new Intent(this, SongsActivity.class);
        //startActivity(intent);
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
        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1);
        String uid = (String) uidTv.getText();
        double lat = recorddao.getLatByUid(uid);
        double lng = recorddao.getLngByUid(uid);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("methodName", "ZoomToLatLng");
        intent.putExtra("Lat", lat);
        intent.putExtra("Lng", lng);
        intent.putExtra("ZoomLevel", 12.);
        startActivity(intent);
    }

    //retrieves and sends the songID to spotify to play
    public void API_playThisSong(View view) {
        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1);
        String uid = (String) uidTv.getText();
        String songID = recorddao.getSidByUid(uid);

        View contextView = itemView.findViewById(R.id.btn_playSong);
        Snackbar.make(contextView, songID, Snackbar.LENGTH_SHORT)
                .show();


        mSpotifyAppRemote.getPlayerApi().play(songID);

    }

    //opens a dialog to confirm deleting the Song
    //deletes song from the DB
    public void onClickOpenSongDeleteDialog(View view) {
        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1); //now this gets tv5 of the first item in the list...
        String uid = (String) uidTv.getText();

        new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog)
                .setMessage("Do you want to unplayce this song?")
                .setNeutralButton("cancel", null)
                .setNegativeButton("delete", (dialog, which) -> deleteRecord(uid))
                .show();
    }

    private void deleteRecord(String uid) {
        recorddao.deleteRecordByUID(uid);
        List<Record> songs = recorddao.getAllSongs();
        songsAdapter = new SongsAdapter(songs);
        recyclerView.setAdapter(songsAdapter);
    }
        public void redirectToLauncher() {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
        }
}

//Todo add albumart from Spotify?