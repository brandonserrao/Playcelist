package com.brandonserrao.playcelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
/*
import static com.brandonserrao.playcelist.MainActivity.db_name;
import static com.brandonserrao.playcelist.MainActivity.song_list;
import static com.brandonserrao.playcelist.MainActivity.songdao;*/

public class ListsActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "fdcc6fcc754e42e3bc7f45f2524816f3"; //use from MAC
    //public static final String CLIENT_ID = "cff5c927f91e4e9582f97c827f8632dd"; //- use from PC;
    private static final String REDIRECT_URI = "com.brandonserrao.playcelist://callback";
    public SpotifyAppRemote mSpotifyAppRemote;




    public String db_name = "playcelist_db_v8.sqlite";
    //public String db_name = "sqlstudio_db2_v5.sqlite";
    RecordDAO recorddao;
    List<Record> list_list;

    ListsAdapter listsAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        //create db instance for this activity
        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class, db_name)
                        .allowMainThreadQueries()
                        .createFromAsset(db_name)
                        .build();

        //setup recycler view and contents
        recorddao = database.getRecordDAO();
        list_list = recorddao.getAllLists();
        List<Record> list_values = list_list;

        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listsAdapter = new ListsAdapter(list_values);
        recyclerView.setAdapter(listsAdapter);

        Log.e("SPOTIFY", "login attemt");
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


    //button functions
    public void searchListsByName(View view) {
        EditText et = findViewById(R.id.edittext_searchbar);
        String search_term = et.getText().toString();
        List<Record> search_results = recorddao.searchListsByName(search_term);
        listsAdapter = new ListsAdapter(search_results);
        recyclerView.setAdapter(listsAdapter);
    }

    public void onClickDeleteResults(View view) {
        EditText et = findViewById(R.id.edittext_searchbar);
        String search_term = et.getText().toString();
        recorddao.deleteListSearchResults(search_term);
        et.setText("");

        List<Record> lists = recorddao.getAllLists();

        listsAdapter = new ListsAdapter(lists);
        recyclerView.setAdapter(listsAdapter);
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

    //to main activity via intent, center and zoom to list position on map
    public void viewListOnMap(View view) {
        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1);
        String uid = (String) uidTv.getText();
        double lat = recorddao.getLatByUid(uid);
        double lng = recorddao.getLngByUid(uid);
        //int zoomLevel = Integer.parseInt(recorddao.getZoomLevelByUid(uid));
        int zoomLevel = 16;

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("methodName", "ZoomToLatLng");
        intent.putExtra("Lat", lat);
        intent.putExtra("Lng", lng);
        intent.putExtra("Zoomlevel", zoomLevel);
        startActivity(intent);
    }

    //retrieves and sends the listID to spotify to play
    public void API_playThisList(View view) {



        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1);
        String uid = (String) uidTv.getText();
        String listID = recorddao.getSidByUid(uid);

        View contextView = itemView.findViewById(R.id.btn_playList);
        Snackbar.make(contextView, listID, Snackbar.LENGTH_SHORT)
                .show();

        // Play a playlist

        mSpotifyAppRemote.getPlayerApi().play(listID);

    }

    //opens a dialog to confirm deleting the list
    public void onClickOpenListDeleteDialog(View view) {
        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1); //now this gets tv5 of the first item in the list...
        String uid = (String) uidTv.getText();

        new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog)
                .setMessage("Do you want to delete this playcelist?")
                .setNeutralButton("cancel", null)
                .setNegativeButton("delete", (dialog, which) -> deleteRecord(uid))
                .show();
    }

    //deletes selected list from db
    private void deleteRecord(String uid) {
        recorddao.deleteRecordByUID(uid);
        List<Record> lists = recorddao.getAllLists();
        listsAdapter = new ListsAdapter(lists);
        recyclerView.setAdapter(listsAdapter);

        String listID = recorddao.getSidByUid(uid);


        try {mSpotifyAppRemote.getUserApi().removeFromLibrary(listID); } finally {

        };
    }
}
