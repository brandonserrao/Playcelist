package com.brandonserrao.playcelist;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.brandonserrao.playcelist.SPPlaylist.SPPlaylist;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.brandonserrao.playcelist.SPUser.SPUser;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;


import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Track;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    public LocationManager locationManager;
    public LocationListener locationListener;
    public Location device_location;
    //Todo set initial location value so list building doesn't break
    public String address;

    //database implementation variables
    public /*static*/ String db_name = "playcelist_db_v8.sqlite";
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
    //public static final String CLIENT_ID = "fdcc6fcc754e42e3bc7f45f2524816f3"; //use from MAC
    public static final String CLIENT_ID = "cff5c927f91e4e9582f97c827f8632dd"; //- use from PC;
    private static final String REDIRECT_URI = "com.brandonserrao.playcelist://callback";
    public SpotifyAppRemote mSpotifyAppRemote;


    public SPUser CUser; // user profile

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public String mAccessToken;
    public String mAccessCode;
    public Call mCall;

    public String CurrentTrackID;
    public String CurrentTrackName;
    public String CurrentTrackArtist;

    public String CUserName;
    public String CUserUpiclnk;
    public String CUserID;
    public String PlaylistID;
    private BottomNavigationView.OnNavigationItemSelectedListener myNavigationItemListener;


    // saving state


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        //Todo somehow select the right bottom navigation item in main, lists and songs activities.

        Log.e("MAIN", "the are in the main");
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


        CUserUpiclnk = pref.getString("CUserUpiclnk", "");
        Log.e("SHARED", "Piclink " + CUserUpiclnk);

        CUserID = pref.getString("CUserID", "_");
        Log.e("SHARED", "CUserID " + CUserID);


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

        //---testing device locating code
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                if (location != null) {
                    device_location = location;
                    //--displaying gps location for debugging
                    TextView textView = findViewById(R.id.debug_textview);
                    double lat = device_location.getLatitude(), lng = device_location.getLongitude();
                    String debug_text = String.valueOf(lat) + String.valueOf(lng);
                    textView.setText(debug_text);

                    //getting reverse geocoded address for potential use in playlist placement
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        int num_results = 1;
                        List<Address> address_list = geocoder.getFromLocation(lat, lng, num_results);
                        address = address_list.get(0).getAddressLine(0);
                        /*////--from my tutorial
                        int num_results=4;
                        List<Address> address_list = geocoder.getFromLocation(lat, lng, num_results);
                        String address = address_list.get(0).getAddressLine(0); // just gets tested for existence by the following loop
                        if (address!=null) {


                            String multi_address = "";
                            for(int i=0;i<num_results;i++){
                                multi_address = multi_address
                                        + "\u2794 " + address_list.get(i).getAddressLine(0)
                                        + "\n" + "URL: " + ((address_list.get(i).getUrl()!=null)?address_list.get(i).getUrl():"none available")
                                        + System.getProperty("line.separator");
                            }
//                            addressField.setText("\u2794 " + address);
                            addressField.setText(multi_address);
                        }*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (location == null) {/*
                    textview_lat.setText(R.string.label_lat+R.string.unknown_location);
                    textview_long.setText(R.string.label_long+R.string.unknown_location);*/
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    10000, 10, locationListener);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
        }

    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        //started on successful map creation; main activities start here
        MainActivity.this.mapboxMap = mapboxMap;
        LoadUsertoNavBar();

        //initialize the bottomNavBar
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = this.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener);
        bottomNavigationView.setSelectedItemId(R.id.btn_toMap);
        bottomNavigationView.findViewById(R.id.btn_toMap).setClickable(false);
        bottomNavigationView.findViewById(R.id.btn_toMap).setActivated(true);
        //Todo have the active state be represented in the style too

        //checks where to zoom to
        //for viewOnMap intents, zoom to the clicked list or song
        //for other intents zoom to current location if available
        Intent intent = getIntent();
        if (intent.hasExtra("methodName")) {
            if (intent.getStringExtra("methodName").equals("ZoomToLatLng")) {
                zoomToLatLng(intent.getDoubleExtra("Lat", 0), intent.getDoubleExtra("Lng", 0), intent.getDoubleExtra("ZoomLevel", 0));
            }
        } else {
            //Todo activate device_location upon creation so != null...
            if (device_location != null) {
                double lat = device_location.getLatitude();
                double lng = device_location.getLongitude();
                zoomToLatLng(lat, lng, 12.);
            } else {
                Toast.makeText(this, "GPS not found", Toast.LENGTH_LONG).show();
            }
        }

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
                Feature f;
                //get topmost feature of query results
                if (features.size() > 0) { //if any features found
                    f = features.get(0); //get topmost
                    //check for feature properties; return properties
                    if (f.properties() != null) {
                        textView.setText(f.toJson());
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
                    features = mapboxMap.queryRenderedFeatures(rectF, SONGS_LAYER_ID); //returns List<Feature> of marker features
                    String s = "";
                    for (int i = 0; i < features.size(); i++) {
                        f = features.get(i);
                        s = s + "Marker " + String.valueOf(i) + ": \n" + f.toString() + "\n"; //??
                    }
                    //--testing getting device current location
                    String numberOfFeatures = String.valueOf(features.size());
                    String current_location = "Device \n Latitude: " + String.valueOf(device_location.getLatitude())
                            + "\n Longitude: " + String.valueOf(device_location.getLongitude())
                            + "\n Time: " + device_location.getTime()
                            + "\n Address: " + address;
                    String debug_text = current_location + "\n"
                            + "Got " + numberOfFeatures + " rendered song features \n"
                            //+ features.toString()
                            + s;
                    textView.setText(debug_text);
                }

                Toast.makeText(MainActivity.this, "onClick: testing mapbox map query", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //when longclicking on the map, a dialog opens and the currently playing song can be playced
        //Todo add option to choose colour (if not, delete extra colours in color file)???
        mapboxMap.addOnMapLongClickListener(point -> {
            double lat = point.getLatitude();
            double lng = point.getLongitude();
            new MaterialAlertDialogBuilder(MainActivity.this, R.style.AppTheme_Dialog)
                    .setTitle("Playce currently playing song")
                    .setMessage("here?")
                    .setNeutralButton("cancel", null)
                    .setPositiveButton("playce", (dialog, which) -> onClickPlayceSong(lat, lng))
                    .show();
            return true;
        });

        mapboxMap.addOnFlingListener(new MapboxMap.OnFlingListener() {
            @Override
            public void onFling() {
                Toast.makeText(MainActivity.this, "onFling: Weeeeee", Toast.LENGTH_SHORT).show();
            }
        });

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.sv_map);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                svSearchMap("");
                return false;
            }
        });
        //searchView.setOnSearchClickListener();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svSearchMap(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //svSearchMap(query);
                return false;
            }
        });
    }

    private void svSearchMap(String query) {
        if (!query.isEmpty()) {

            //search for songs, apply different pin icon

            //init storage for bounds values
            double latSouth=0, latNorth=0, lonWest=0, lonEast=0;

            song_list = recorddao.searchSongsByName(query);
            featurelist_songlayer.clear();

            for (int i = 0; i < song_list.size(); i++) {
                Record song = song_list.get(i);
                addSongToFeaturelist(song, featurelist_songlayer);

                double lat = song.getLAT(), lng = song.getLNG();
                //check and store max coord bounds
                if (i>0) {
                    if (lat > latNorth) {
                        latNorth = lat;
                    } else if (lat < latSouth) {
                        latSouth = lat;
                    }
                    if (lng > lonEast) {
                        lonEast = lng;
                    } else if (lng > lonWest) {
                        lonWest = lng;
                    }
                } else if (i == 0) {
                    latNorth = lat; latSouth = lat;
                    lonEast = lng; lonWest = lng;
                }
            }

            updateLayerSources();
            //resetting mapstyle//to selection pin instead
            song_styleBuilder = new Style.Builder()
                    .fromUri(getResources().getString(R.string.darkstyleURL))
                    .withImage(SONGS_ICON_ID, BitmapFactory.decodeResource(
                            MainActivity.this.getResources(),
                            R.drawable.red_marker)) //Todo change selection marker
                    .withSource(song_source)
                    .withLayer(song_symbolLayer);
            mapboxMap.setStyle(song_styleBuilder);

/*          //debug
            Toast.makeText(this, String.valueOf(latNorth)+"\n"+String.valueOf(latSouth)+"\n"+String.valueOf(lonEast)+"\n"+String.valueOf(lonWest),
                    Toast.LENGTH_LONG).show();*/
            //make bounds for search results view
            LatLngBounds bounds = LatLngBounds.from(latNorth, lonEast, latSouth, lonWest);

            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50), 1250);

        }
        else if (query == "") {
            //get all songs,rebuild layer source, reset style
            song_list = recorddao.getAllSongs();
            for (int i = 0; i < song_list.size(); i++) {
                Record song = song_list.get(i);
                addSongToFeaturelist(song, featurelist_songlayer);
            }
            updateLayerSources();
            resetMapStyle();
        }
    }



    public void addSongToFeaturelist(@NonNull Record song, List<Feature> featurelist_songlayer) {
        Feature feature = Feature.fromGeometry(Point.fromLngLat(song.getLNG(), song.getLAT()));
        feature.addStringProperty("NAME", song.getNAME());
        feature.addStringProperty("S_ID", song.getS_ID());
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

        song_symbolLayer = new SymbolLayer(SONGS_LAYER_ID, SONGS_SOURCE_ID)
                .withProperties(PropertyFactory.iconImage(SONGS_ICON_ID),
                        iconAllowOverlap(true)
                );
    }

    //Todo figure out why the pins are almost transparent when resetting the MapStyle after placing a new marker
    public void resetMapStyle() {
        //create mapstyle + adding marker image
        song_styleBuilder = new Style.Builder()
                .fromUri(getResources().getString(R.string.darkstyleURL))
                .withImage(SONGS_ICON_ID, BitmapFactory.decodeResource(
                        MainActivity.this.getResources(),
                        R.drawable.pin))
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
    private void zoomToLatLng(Double lat, Double lng, double zoomlevel) {
        LatLng focus;
        focus = new LatLng(lat, lng);
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(focus)
                        .zoom(zoomlevel)
                        .build()));
    }


    //Button Click Handlers
    //opens Songs Activity which shows all playced songs in a recycler view
    public void onClickStartSongsActivity(MenuItem item) {
        Intent intent = new Intent(this, SongsActivity.class);
        startActivity(intent);
    }

    //navigates to main activity which shows now playing info and a map with all playced songs as Todo colorful bubbles
    public void onClickStartMainActivity(MenuItem item) {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }

    //opens Lists Activity which shows all playcelists in a recycler view
    public void onClickStartListsActivity(MenuItem item) {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    //zooms to current GPS posittion on the map and
    //opens dialog to confirm playcing the current playing song at the current GPS position
    //Todo add option to choose colour (if not, delete extra colours in color file)
    public void onClickPlayceSongAtGPS(View view) {
        double lat = device_location.getLatitude();
        double lng = device_location.getLongitude();
        zoomToLatLng(lat, lng, 16);
        new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog)
                .setTitle("Playce currently playing song")
                .setMessage("at your current GPS position?")
                .setNeutralButton("cancel", null)
                .setPositiveButton("playce", (dialog, which) -> onClickPlayceSong(lat, lng))
                .show();
    }

    //Todo make sure current location is not null in all methods using it

    //creates new song item in the DB using now playing info and lat and lng as input
    private void onClickPlayceSong(double dlat, double dlng) {
        //getting all the information for a new record item
        float lat = (float) dlat;
        float lng = (float) dlng;
        String songID = CurrentTrackID;
        String songArtist = CurrentTrackArtist;
        String songName = CurrentTrackName;

        //creating new song entry and inserting it in db table
        Record song = new Record();
        song.setLNG(lng);
        song.setLAT(lat);
        song.setNAME(songName);
        song.setS_ID(songID);
        song.setIsLIST(false);
        song.setARTIST(songArtist);
        recorddao.insert(song);

        //adding to featurelist to be placed as a marker on map
        addSongToFeaturelist(song, featurelist_songlayer);
        updateLayerSources();
        //resetting the style after reconstructing source shows newly added marker
        resetMapStyle();
    }

    //opens a custom Dialog to ask for playcelist name input and confirmation
    public void onClickCreatePlaycelist(View view) {
        AlertDialog.Builder dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_Dialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_input, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = dialogView.findViewById(R.id.inputET);
        dialogBuilder.setTitle("New Playcelist");
        dialogBuilder.setMessage("including all songs currently visible on the map");
        dialogBuilder.setPositiveButton("create playcelist", (dialog, whichButton) -> onClickPrepareListForAPI(edt.getText().toString()));
        dialogBuilder.setNeutralButton("Cancel", null);
        AlertDialog d = dialogBuilder.create();
        d.show();
    }

    //prepares a information to be sent to Spotify to create a playlist
    private void onClickPrepareListForAPI(String nameInput) {
        double dlat = mapboxMap.getCameraPosition().target.getLatitude();
        double dlng = mapboxMap.getCameraPosition().target.getLongitude();
        double dzoom = mapboxMap.getCameraPosition().zoom;
        String listName = nameInput;
        String songIDs = getVisibleSongs();
        String listID = null;
        if (songIDs != null) {
            createPlaycelist(listName, songIDs);
            Toast.makeText(this, listName + "\n" + listID, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "no visible songs", Toast.LENGTH_LONG).show();
        }
    }

    //checks the mapView for visible song items and creates a String including their spotify IDs
    private String getVisibleSongs() {
        RectF rectF = new RectF(
                mapView.getLeft(),
                mapView.getTop(),
                mapView.getRight(),
                mapView.getBottom()
        );
        String songIDs = null;
        List<Feature> features = mapboxMap.queryRenderedFeatures(rectF, SONGS_LAYER_ID); //returns List<Feature> of marker features
        if (features.size() > 0) {
            songIDs = features.get(0).getProperty("S_ID").getAsString();
            if (features.size() > 1) {
                for (int i = 1; i < features.size(); i++) {
                    songIDs = songIDs + "," + features.get(i).getProperty("S_ID").getAsString();
                }
            }
        }
        return songIDs;
    }

    //retrieves all necessary information and creates list item in db
    //Todo call this upon receiving the listID from Spotify
    private void createListItem(String listID, String listName) {
        double dlat = mapboxMap.getCameraPosition().target.getLatitude();
        double dlng = mapboxMap.getCameraPosition().target.getLongitude();
        double dzoom = mapboxMap.getCameraPosition().zoom;
        //converting latlng to floats for db
        float lat = (float) dlat;
        float lng = (float) dlng;
        String zoom = Double.toString(dzoom);

        //creating new list entry and inserting it in db table
        Record list = new Record();
        list.setLNG(lng);
        list.setLAT(lat);
        list.setNAME(listName);
        list.setS_ID(listID);
        list.setIsLIST(true);
        list.setARTIST(zoom);
        recorddao.insert(list);

        Toast.makeText(this, listName + "\n" + listID, Toast.LENGTH_LONG).show();
    }

    //Todo make listID local (might not have to be necessary) and return it not null (very necessary!)
    //takes a string with several spotify IDs and creates a playlist of them on spotify
    //returns spotify ID of playlist
    private String createPlaycelist(String name, String songIDs) {
        if (mAccessToken != null) {
            final Request request = new Request.Builder()
                    .url("https://api.spotify.com/v1/users/" + CUserID + "/playlists") //get user data
                    .addHeader("Authorization", "Bearer " + mAccessToken)
                    .post(RequestBody
                            .create(MediaType
                                            .parse("application/json"),
                                    "{\"name\": \"" + name + "\"}"
                            ))
                    .build();
            cancelCall();
            mCall = mOkHttpClient.newCall(request);
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Response", "Request fail");//Fail
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        final JSONObject jsonObject = new JSONObject(response.body().string());
                        String JsonResponse = jsonObject.toString();
                        Gson gson = new Gson();
                        SPPlaylist CPlaylist;
                        CPlaylist = gson.fromJson(JsonResponse, SPPlaylist.class);
                        PlaylistID = CPlaylist.getId();

                        Log.e("Spotify", "Playlist created, id:" + PlaylistID);
                        Log.e("Spotify", JsonResponse);
                        // adding songs to the playlist


                        final Request request2 = new Request.Builder()
                                .url("https://api.spotify.com/v1/playlists/" + PlaylistID + "/tracks?uris=" + songIDs) //get user data
                                .addHeader("Authorization", "Bearer " + mAccessToken)
                                .post(RequestBody
                                        .create(MediaType
                                                        .parse("application/json"),
                                                "{}"
                                        ))
                                .build();


                        Log.e("Spotify", request2.toString());
                        Log.e("Spotify", request2.body().toString());
                        cancelCall();
                        mCall = mOkHttpClient.newCall(request2);
                        mCall.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("Response", "Request fail");//Fail
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    final JSONObject jsonObject = new JSONObject(response.body().string());
                                    String JsonResponse = jsonObject.toString();


                                    Log.e("Spotify", "Songs added");
                                    Log.e("Spotify", JsonResponse);
                                    // adding songs to the playlist


                                } catch (JSONException e) {
                                    //Fail
                                }
                            }
                        });


                    } catch (JSONException e) {
                        //Fail
                    }
                }
            });


        } else {

            //todo please log in
        }

        return PlaylistID;
    }


    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    //slides in navigation drawer which handles account information (and what is displayed on the map)
    public void onClickOpenNavDrawer(View view) {
        DrawerLayout mDrawer = findViewById(R.id.mDrawer);
        mDrawer.openDrawer(findViewById(R.id.nav_drawer));
    }



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
        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_WORLD_READABLE);
        Editor editor = pref.edit();
        editor.clear();


    }

    // On succcesful connection to the Spotify APP
    private void connected() {
        SharedPreferences pref = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        ;
        Editor editor = pref.edit();
        isAppLoggedIn = true;
        editor.putBoolean("isAppLoggedIn", true);
        editor.commit();
        if (isAppLoggedIn) Log.e("SHARED", "isAppLoggedIn1");
        else Log.e("SHARED", "isAppLoggedIn0");

        Log.e("SPOTIFY", "connected");
        // Subscribe to PlayerState
        TextView Current_song = findViewById(R.id.nowplaying_info_song);
        TextView Current_artist = findViewById(R.id.nowplaying_info_artist);
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    Log.e("SPOTIFY", "player");
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.e("MainActivity", track.name + " by " + track.artist.name);
                        Current_song.setText(track.name);
                        Current_artist.setText(track.artist.name);
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
        ImageView Upic = findViewById(R.id.nav_header_SProfilePicture);
        Upic.setImageDrawable(null);
        TextView Username = findViewById(R.id.nav_header_SUserName);
        Username.setText(R.string.SAccountName);
        //Todo make sure to go back to Launcher (not main) when going back to the app.
    }


    public void LoadUsertoNavBar() {
        TextView Username = findViewById(R.id.nav_header_SUserName);
        Username.setText(CUserName);
        ImageView Upic = findViewById(R.id.nav_header_SProfilePicture);
        Glide.with(Upic).load(CUserUpiclnk).into(Upic);


    }


}

//Todo add onclick play song functionality in map

//Todo clean up code: through out unnecessary stuff
//Todo comment the code
//Todo arrange code in a sensible order
//Todo at some point we wanted to update lists when playcing new songs but I guess we dropped that...