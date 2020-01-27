package com.brandonserrao.playcelist;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/*
import static com.brandonserrao.playcelist.MainActivity.db_name;
import static com.brandonserrao.playcelist.MainActivity.song_list;
import static com.brandonserrao.playcelist.MainActivity.songdao;*/

public class ListsActivity extends AppCompatActivity {

    //public static final String CLIENT_ID = "fdcc6fcc754e42e3bc7f45f2524816f3"; //use from MAC
    public static final String CLIENT_ID = "cff5c927f91e4e9582f97c827f8632dd"; //- use from PC;
    private static final String REDIRECT_URI = "com.brandonserrao.playcelist://callback";
    public SpotifyAppRemote mSpotifyAppRemote;
    public Call mCall;

    public String db_name = "playcelist_db_v8.sqlite";
    //public String db_name = "sqlstudio_db2_v5.sqlite";
    RecordDAO recorddao;
    List<Record> list_list;
    private boolean isAppLoggedIn = false;

    ListsAdapter listsAdapter;
    RecyclerView recyclerView;
    public String mAccessToken;

    private BottomNavigationView.OnNavigationItemSelectedListener myNavigationItemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        isAppLoggedIn=pref.getBoolean("isAppLoggedIn",false);
        if (isAppLoggedIn) Log.e("SHARED", "isAppLoggedIn1");
        else Log.e("SHARED", "isAppLoggedIn0");
        mAccessToken = pref.getString("mAccessToken", "");
        Log.e("SHARED", "tk+" + mAccessToken);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        //initialize the bottomNavBar
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = this.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener);
        bottomNavigationView.setSelectedItemId(R.id.btn_toLists);
        bottomNavigationView.findViewById(R.id.btn_toLists).setClickable(false);
        bottomNavigationView.findViewById(R.id.btn_toLists).setActivated(true);
        //Todo have the active state be represented in the style too

        //check if this is the first creation after initial spotify log in
        boolean isFirstTimeLists;
        isFirstTimeLists = pref.getBoolean("isFirstTimeLists", true);
        if (isFirstTimeLists) {
            //Todo open welcome dialog
            new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog)
                    .setTitle("Welcome!")
                    .setMessage(getString(R.string.welcomeLists))
                    .setPositiveButton("got it!", ((dialog, which) -> onClickChangeFirstTimeFlag()))
                    .show();
        }

        //create db instance for this activity
        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class, db_name)
                        .allowMainThreadQueries()
                        .createFromAsset(db_name)
                        .build();

        //setup recycler view and contents
        recorddao = database.getRecordDAO();
        list_list = recorddao.getAllLists();
        //reversing so most recent at top
        Collections.reverse(list_list);
        List<Record> list_values = list_list;

        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listsAdapter = new ListsAdapter(list_values);
        recyclerView.setAdapter(listsAdapter);

        Log.e("SPOTIFY", "login attemt");
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

        else {

         //   Toast.makeText(this, R.string.SAccountName, Toast.LENGTH_LONG).show();
           redirectToLauncher();

        }

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.sv_lists);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svSearchListsByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //sets the firstTimeFlag to false.
    private void onClickChangeFirstTimeFlag() {
        //change flag to indicate the welcome dialog doesn't have to show again
        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        boolean isFirstTimeLists;
        isFirstTimeLists = pref.getBoolean("isFirstTimeLists", true);
        if (isFirstTimeLists) {
            editor.putBoolean("isFirstTimeLists", false);
            editor.apply();
        }
    }

    private void svSearchListsByName(String query) {
        List<Record> search_results = recorddao.searchListsByName(query);
        listsAdapter = new ListsAdapter(search_results);
        recyclerView.setAdapter(listsAdapter);
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
        if (isAppLoggedIn == false || mAccessToken != null) {
            redirectToLauncher();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickStartListsActivity(MenuItem item) {
        //Intent intent = new Intent(this, ListsActivity.class);
        //startActivity(intent);
    }

    //to main activity via intent, center and zoom to list position on map
    public void viewListOnMap(View view) {
        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1);
        String uid = (String) uidTv.getText();
        double lat = recorddao.getLatByUid(uid);
        double lng = recorddao.getLngByUid(uid);
        double zoomLevel = Double.parseDouble(recorddao.getZoomLevelByUid(uid));

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("methodName", "ZoomToLatLng");
        intent.putExtra("Lat", lat);
        intent.putExtra("Lng", lng);
        intent.putExtra("ZoomLevel", zoomLevel);
        startActivity(intent);
    }

    //retrieves and sends the listID to spotify to play
    public void API_playThisList(View view) {
        View itemView = (View) view.getParent().getParent();
        TextView uidTv = itemView.findViewById(R.id.tv1);
        String uid = (String) uidTv.getText();
        String listID = recorddao.getSidByUid(uid);
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
        String listID = recorddao.getSidByUid(uid);

        recorddao.deleteRecordByUID(uid);
        List<Record> lists = recorddao.getAllLists();
        listsAdapter = new ListsAdapter(lists);
        recyclerView.setAdapter(listsAdapter);



        final OkHttpClient mOkHttpClient = new OkHttpClient();
        if (mAccessToken != null) {

            Log.e("SPOTIFY","delete attemt id"+listID);
            RequestBody body =RequestBody.create(null, new byte[0]);
            final Request request = new Request.Builder()
                    .url("https://api.spotify.com/v1/playlists/"+listID.replace("spotify:playlist:","")+"/followers") //get user data
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .delete(body).build();
            cancelCall();
            mCall = mOkHttpClient.newCall(request);
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Response", "Request fail");//Fail
                }

                @Override
                public void onResponse(Call call, Response response) {





                    Log.e("SPOTIFY","deleted "+response);
                }

            });
            }

    }

    public void redirectToLauncher() {
        Intent intent = new Intent(this, LauncherActivity.class);
        startActivity(intent);
    }


    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}



//Todo add listart picture from Spotify?