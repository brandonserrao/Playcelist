package com.brandonserrao.playcelist;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.brandonserrao.playcelist.model.Image;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.brandonserrao.playcelist.model.SPUser;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;


import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    LocationManager locationManager;
    LocationListener locationListener;

    //database implementation variables
    public /*static*/ String db_name = "playcelist_db_v8.sqlite";
    //public String db_name = "sqlstudio_db2_v5.sqlite";
    public RecordDAO recorddao;
    public List<Record> song_list; //to hold Song objects from db queries

    //marker implementation variables
    private static final String SONGS_SOURCE_ID = "songs";
    private static final String SONGS_ICON_ID = "songs";
    private static final String SONGS_LAYER_ID = "songs";
    //init feature list to be displayed
    public /*static*/ List<Feature> featurelist_songlayer = new ArrayList<>(); //for starting off markers
    FeatureCollection song_featureCollection;
    GeoJsonSource song_source;
    SymbolLayer song_symbolLayer;
    Style.Builder song_styleBuilder;


    //we should find a way to be able to use a loggedIn flag...
    private boolean isUpicloaded = false;
    private boolean isAppLoggedIn = false;
    private boolean isWebLoggedIn = false;

    // spotify stufff

   // public static final String CLIENT_ID = "cff5c927f91e4e9582f97c827f8632dd" - use from PC;
    public static final String CLIENT_ID = "fdcc6fcc754e42e3bc7f45f2524816f3"; //use from MAC
    private static final String REDIRECT_URI = "com.brandonserrao.playcelist://callback";
    public SpotifyAppRemote mSpotifyAppRemote;
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;

    public SPUser CUser; // user profile

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public String mAccessToken;
    public String mAccessCode;
    public Call mCall;

    public String CurrentTrackID;
    public String CurrentTrackName;
    public String CurrentTrackArtist;

    public String CUserName = "Please log in";


    // saving state


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // restoring important variables state

        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        Editor editor = pref.edit();

        mAccessToken = pref.getString("mAccessToken", "");
        Log.e("SHARED", "tk+" + mAccessToken);

        //  isUpicloaded=pref.getBoolean("isUpicloaded",false);
        if (isUpicloaded) Log.e("SHARED", "isUpicloaded1");
        else Log.e("SHARED", "isUpicloaded0");

        //isAppLoggedIn=pref.getBoolean("isAppLoggedIn",false);
        if (isAppLoggedIn) Log.e("SHARED", "isAppLoggedIn1");
        else Log.e("SHARED", "isAppLoggedIn0");

        //isWebLoggedIn=pref.getBoolean("isWebLoggedIn",false);
        if (isWebLoggedIn) Log.e("SHARED", "isWebLoggedIn1");
        else Log.e("SHARED", "isWebLoggedIn0");

        CUserName = pref.getString("CUserName", "Please log in");
        Log.e("SHARED", "Name " + CUserName);


        /*
        -------obsolete; database intialization from file from assets, as shown in tutorials
        */

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
        //-----------

        //create db instance + interface for this activity
        AppDatabase database =
                Room.databaseBuilder(this, AppDatabase.class, db_name)
                        .allowMainThreadQueries()
                        .createFromAsset(db_name)
                        .build();
        recorddao = database.getRecordDAO();
        song_list = recorddao.getAllSongs();


        //mapbox map creation + styling
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapbox);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        //started on successful map creation; main activities start here
        MainActivity.this.mapboxMap = mapboxMap;

        Intent intent = getIntent();
        if (intent.hasExtra("methodName")) {
            if (intent.getStringExtra("methodName").equals("ZoomToLatLng")) {
                zoomToLatLng(intent.getDoubleExtra("Lat", 0), intent.getDoubleExtra("Lng", 0), intent.getIntExtra("ZoomLevel", 0));
            }
        }

        /*
        Todo
         loop to add db records to map
         potentially can be moved outside of here+made publicstatic to only be run once
        */
        for (int i = 0; i < song_list.size(); i++) {
            Record song = song_list.get(i);
            addSongToFeaturelist(song, featurelist_songlayer);
        }

        updateLayerSources();

        //firsttime construction of symbollayer
        song_symbolLayer = new SymbolLayer(SONGS_LAYER_ID, SONGS_SOURCE_ID)
                .withProperties(PropertyFactory.iconImage(SONGS_ICON_ID),
                        iconAllowOverlap(true)
                );

        //create mapstyle + adding marker image
        song_styleBuilder = new Style.Builder()
                .fromUri(getResources().getString(R.string.darkstyleURL))
                .withImage(SONGS_ICON_ID, BitmapFactory.decodeResource(
                        MainActivity.this.getResources(),
                        R.drawable.pin
                        )
                )
                .withSource(song_source)
                .withLayer(song_symbolLayer);

        //mandatory set mapstyle; onStyleLoaded @end to perform extra data adds + adjustments
        mapboxMap.setStyle(song_styleBuilder,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        //Map setup+style load completed;
                        //add extra data and perform further adjustments here


                        //----demonstration of shift map focus
/*                        LatLng focus = new LatLng(51.051877, 13.741517);
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(focus)
                                        .zoom(12)
                                        .build()));*/

                    }
                });

        //Map Listeners

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                //conv latlng to screen pixel + check rendered feats there
                final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
                List<Feature> features = mapboxMap.queryRenderedFeatures(pixel, SONGS_LAYER_ID);
                TextView textView = findViewById(R.id.debug_textview);
                //get topmost feature of query results
                if (features.size() > 0) {
                    Feature f = features.get(0);
                    //check for feature properties; return properties
                    if (f.properties() != null) {
                        textView.setText(f.toJson());
/*                        for (Map.Entry<String, JsonElement> entry: feature.properties().entrySet()) {
                            textView.setText(String.format(
                                    "%s = %s",
                                    entry.getKey(),entry.getValue()
                            ));
                        }*/
                    } else {
                        textView.setText("no feature properties ie. is null");
                    }
                } else {//get all visible/rendered markers
                    RectF rectF = new RectF(
                            mapView.getLeft(),
                            mapView.getTop(),
                            mapView.getRight(),
                            mapView.getBottom()
                    );
                    features = mapboxMap.queryRenderedFeatures(rectF);
                    String feats_str = "";
                    for (int i = 0; i < features.size(); i++) {
                        String feat = features.get(i).toJson();
                        feats_str.concat(feat);
                    }
                    String text = "Got rendered song features \n" + feats_str + "\n .";
                    textView.setText(text);
                }

                Toast.makeText(MainActivity.this, "onClick: testing mapbox map query", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
            @Override
            public boolean onMapLongClick(@NonNull LatLng point) {

                double lat = point.getLatitude(), lng = point.getLongitude();
                String name = "!placeholdername!", song_id = "!placeholder_songid!", artist = "!placeholderArtist";//??
/*                new MaterialAlertDialogBuilder(MainActivity.this, R.style.AppTheme_Dialog)
                        .setTitle("Playce currently playing song")
                        .setMessage("here?")
                        .setNeutralButton("cancel", null)
                        .setPositiveButton("song", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createSongItem(lng, lat);
                            }
                        })
                        .show();
                return true;
            }
        });*/

        /*
        Todo
         we need an onclicklistener that starts playing songs / lists when you click on a marker on the map
        */

                //creating new song entry and placing in database
                Record song = new Record();
                song.setLNG((float) lng);
                song.setLAT((float) lat);
                song.setNAME(name);
                song.setS_ID(song_id);
                song.setIsLIST(false);
                recorddao.insert(song);
                //adding to featurelist to be placed as a marker on map
                addSongToFeaturelist(song, featurelist_songlayer);
                updateLayerSources();
                resetMapStyle();
                //resetting the style after reconstructing source shows newly added marker

                Toast.makeText(MainActivity.this, "record added,", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        //??***
        // we need an onclicklistener that starts playing songs / lists when you click on a marker on the map
        //*****

/*        private void createSongItem(double lat, double lng) {
            Record song = new Record();
            song.setLNG((float) lng);
            song.setLAT((float) lat);
            song.setNAME(CurrentTrackName);
            song.setS_ID(CurrentTrackID);
            song.setIsLIST(false);
            song.setARTIST(CurrentTrackArtist);
            songdao.insert(song);
            //adding to featurelist to be placed as a marker on map
            addSongToFeaturelist(song, featurelist_songlayer);
            updateLayerSources();
            resetMapStyle();
            // resetting the style after reconstructing source shows newly added marker
        *//*
        Todo @Brandon: somewhere here the pins are changed again - don't know how to change that /V
        Todo (is fine again after recreating mainActivity)
        *//*
            Toast.makeText(MainActivity.this, "record added,", Toast.LENGTH_LONG).show();
        }*/

        mapboxMap.addOnFlingListener(new MapboxMap.OnFlingListener() {
            @Override
            public void onFling() {
                Toast.makeText(MainActivity.this, "onFling: Weeeeee", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void addSongToFeaturelist(@NonNull Record song, List<Feature> featurelist_songlayer) {
        Feature feature = Feature.fromGeometry(Point.fromLngLat(song.getLNG(), song.getLAT()));
        feature.addStringProperty("NAME", song.getNAME());
        feature.addStringProperty("SONG_ID", song.getS_ID());
        feature.addNumberProperty("UID", song.getUID());
        featurelist_songlayer.add(feature);
    }

    public void updateLayerSources() {
/*        // "proper" method from stackoverflow response
        GeoJsonSource song_source = mapboxMap.getSourceAs(SONGS_SOURCE_ID); //Todo mapboxMap.getSourceAs() doesn't exist
        song_source.setGeoJson(song_featureCollection);*/

        //my method
        //List<Feature> to FeatureCollection to GeoJsonSource as source
        song_featureCollection = FeatureCollection.fromFeatures(featurelist_songlayer);
        song_source = new GeoJsonSource(SONGS_SOURCE_ID, song_featureCollection);
        /*
        Todo
         insert other additional updates here
         //construct layer//necessary or else attempts to add layer twice and breaks
        */
        song_symbolLayer = new SymbolLayer(SONGS_LAYER_ID, SONGS_SOURCE_ID)
                .withProperties(PropertyFactory.iconImage(SONGS_ICON_ID),
                        iconAllowOverlap(true)
                );
    }

    public void resetMapStyle() {
        //create mapstyle + adding marker image
        song_styleBuilder = new Style.Builder()
                .fromUri(getResources().getString(R.string.darkstyleURL))
                .withImage(SONGS_ICON_ID, BitmapFactory.decodeResource(
                        MainActivity.this.getResources(),
                        R.drawable.songpin))
                .withSource(song_source)
                .withLayer(song_symbolLayer);
        mapboxMap.setStyle(song_styleBuilder);
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent
                    .activateLocationComponent(
                            LocationComponentActivationOptions.builder(this,
                                    loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setRenderMode(RenderMode.NORMAL);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    //HANDLERS

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode,
                permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "user_location_permission_explanation",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "user_location_permission_not_granted",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //INTENT handler methods
    //zooms map to a given lat&lng
    private void zoomToLatLng(Double lat, Double lng, Integer zoomlevel) {
        LatLng focus;
        focus = new LatLng(lat, lng);
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(focus)
                        .zoom(zoomlevel)
                        .build()));
    }

    //Todo  write method to zoom to a given extent (to be used when coming from lists activity

    /*Todo probably get rid of this:
       button IDs for reference
        btn_nd [bar at top left in main activity] > opens nav drawer
        btn_playcer [bottom right in main activity] > zooms to current position (GPS) and playces current playing at current position
        btn_toSongs [left in bottom nav bar] > navigates via intent to Songs Activity
        btn_toMap [middle in bottom nav bar] > navigates via intent to Main Activity
        btn_toLists [right in bottom nav bar] > navigates via intent to Lists Activity
    */


    //Button Click Handlers
    //opens Songs Activity which shows all playced songs in a recycler view
    public void onClickStartSongsActivity(MenuItem item) {
        Intent intent = new Intent(this, SongsActivity.class);
        startActivity(intent);
    }

    //navigates to main activity which shows now playing info and a map with all playced songs as Todo colorful bubbles
    public void onClickStartMainActivity(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //opens Lists Activity which shows all playcelists in a recycler view
    public void onClickStartListsActivity(MenuItem item) {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    //opens dialog to confirm playcing the current playing song at the current GPS position
    public void onClickPlayceCurrentSongHere(View view) {
        zoomToCurrentLocation();
        new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog)
                .setTitle("Playce currently playing song")
                .setMessage("at your current GPS position?")
                .setNeutralButton("cancel", null)
                .setPositiveButton("playce", (dialog, which) -> onClickPlayceSong(mapView))
                .show();
    }

    //a method that retrieves the current location and tells the map to center and zoom to that location
    private void zoomToCurrentLocation() {
        /*
        Todo
         use getCurrentLocation() to tell the map where to zoom to
        */
    }

    //returns current lat&long for further processing
    private void getCurrentLocation() {
        /*Todo
         */
    }

    //zooms to current GPS position on the map and creates a song marker
    //creates new song item in the DB using now playing info and GPS info
    private void onClickPlayceSong(MapView mapView) {
        View contextView = findViewById(R.id.btn_playcer);
        Snackbar.make(contextView, R.string.btnWorking, Snackbar.LENGTH_SHORT)
                .show();
        getCurrentLocation();

        /*
        Todo actual code:
         get GPS info
         get API info / nowplaying
         USE CurrentTrackID CurrentTrackName CurrentTrackArtistVariable
         set new marker
         open dialog (do you want to playce [now playing song] here?)
         if confirmed
           create new DB item
           reload map / make sure it appears on map
         else
           dismiss and delete marker
        */
    }

    //opens a custom Dialog to ask for playcelist name input and confirmation
    //creates list item in DB
    //Todo this keeps crashing on clicking OK (when edt.getTect().toString() is used)
    // check https://bhavyanshu.me/tutorials/create-custom-alert-dialog-in-android/08/20/2015/ again....
    public void onClickCreatePlaycelist(View view) {
        AlertDialog.Builder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_input, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = findViewById(R.id.nameInput);
        //Todo somehow make clear that playcelist will be made from the songs currently shown on the map...
        dialogBuilder.setTitle("New Playcelist");
        dialogBuilder.setMessage("enter a name for your playcelist");
        dialogBuilder.setPositiveButton("create playcelist", (dialog, whichButton) -> {
            View contextView = mapView;
            Snackbar.make(contextView, R.string.btnWorking, Snackbar.LENGTH_SHORT)
                    .show();
            //createPlaycelist(mapView, edt.getText().toString());
        });
        dialogBuilder.setNeutralButton("Cancel", null);
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    /*private void createPlaycelist(MapView mapview, String nameInput) {
        View contextView = mapView;
        Snackbar.make(contextView, R.string.btnWorking, Snackbar.LENGTH_SHORT)
                .show();
        /*
        ToDo
         CODE TO CREATE PLAYCELIST INCLUDING THE SONGS SHOWN ATM
         send array with currently shown songs and the chosen name to spotify to create a playlist
         get list ID
         create list item in DB incl name and info from spotify
         (create rectangular shape on map)
        */
    //}


    //slides in navigation drawer which handles account information (and what is displayed on the map)
    public void onClickOpenNavDrawer(View view) {
        DrawerLayout mDrawer = findViewById(R.id.mDrawer);
        mDrawer.openDrawer(findViewById(R.id.nav_drawer));
    }


    //nav drawer checkbox handlers
    /*public void onClickCheckBox1(MenuItem item) {
        NavigationView navDrawer = findViewById(R.id.nav_drawer);
        MenuItem menuItem1 = navDrawer.getMenu().findItem(R.id.check_SongsOnMap);
        CompoundButton checkbox1 = (CompoundButton) menuItem1.getActionView();
        boolean checked = checkbox1.isChecked();
        if (checked) {
            checkbox1.setChecked(false);
        } else {
            checkbox1.setChecked(true);
        }
        //align with map content
        // -
        // -
        // -
    }

    public void onClickCheckBox2(MenuItem item) {
        NavigationView navDrawer = findViewById(R.id.nav_drawer);
        MenuItem menuItem2 = navDrawer.getMenu().findItem(R.id.check_listsOnMap);
        CompoundButton checkbox2 = (CompoundButton) menuItem2.getActionView();
        boolean checked = checkbox2.isChecked();
        if (checked) {
            checkbox2.setChecked(false);
        } else {
            checkbox2.setChecked(true);
        }
        //align with map content
        // -
        // -
        // -
    }*/

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


    // spotify stufff
    @Override
    protected void onStart() {


        super.onStart();
//connection to the Sporify APP
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
                            Log.e("SPOTIFY", " APP connected");
                            connected();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("SPOTIFY", " APP conn fail");
                            Log.e("MyActivity", throwable.getMessage(), throwable);
                        }
                    });


    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);

    }

    // On succcesful connection to the Spotify APP
    private void connected() {
        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);;
        Editor editor = pref.edit();
        isAppLoggedIn = true;
        editor.putBoolean("isAppLoggedIn", true);
        editor.commit();
        if (isAppLoggedIn) Log.e("SHARED", "isAppLoggedIn1");
        else Log.e("SHARED", "isAppLoggedIn0");

        Log.e("SPOTIFY", "connected");
        // Subscribe to PlayerState
        TextView Current_song = findViewById(R.id.nowplaying_info);
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Log.e("SPOTIFY", "player");
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.e("MainActivity", track.name + " by " + track.artist.name);
                        Current_song.setText(track.name + "\n" + track.artist.name);
                        CurrentTrackID = track.uri;
                        CurrentTrackName = track.name;
                        CurrentTrackArtist = track.artist.name;
                    }
                });
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();


    }



    public void LogOutOfSpotify(MenuItem menuItem) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.spotify.com/en/logout ")));
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        isAppLoggedIn = false;
        isWebLoggedIn = false;
        //Todo clear name and pic

    }


    private void LoadUserPic() {

        ImageView Upic = findViewById(R.id.nav_header_SProfilePicture);
        String url = CUser.getImages().get(0).getUrl();
        Glide.with(Upic).load(url).into(Upic);

    }



    // auth request to spotify WEB







}