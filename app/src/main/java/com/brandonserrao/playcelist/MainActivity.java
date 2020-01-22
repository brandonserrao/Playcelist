package com.brandonserrao.playcelist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.brandonserrao.playcelist.model.SPUser;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;

    //database implementation variables
    /*public static*/ String db_name = "sqlstudio_db2_v5.sqlite";
    /*public static*/ SongDAO songdao;
    /*public static*/ List<Song> song_list; //to hold Song objects from db queries

    //marker implementation variables
    private static final String SONGS_SOURCE_ID = "songs";
    private static final String SONGS_ICON_ID = "songs";
    private static final String SONGS_LAYER_ID = "songs";
    //init feature list to be displayed
    public static List<Feature> source_songlayer = new ArrayList<>(); //for starting off markers



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        //??somehow check the boxes, see onClickCheckBox1(); and onClickCheckBox2();

        //-------obsolete; database intialization from file from assets, as shown in tutorials
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
        songdao = database.getSongDAO();
        song_list = songdao.getAllSongs();


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


        //loop to add db records to map
        //??potentially can be moved outside of here+made publicstatic to only be run once
        for (int i = 0; i < song_list.size(); i++) {
            Song song = song_list.get(i);
            double lat = (double) song.getLAT(),
                    lng = (double) song.getLNG();
            //construct geojson feature
            Feature feature = Feature.fromGeometry(Point.fromLngLat(lng, lat));
            feature.addStringProperty("NAME", song.getNAME());
            feature.addStringProperty("SONG_ID", song.getSONG_ID());
            feature.addNumberProperty("UID", song.getUID());
            source_songlayer.add(feature);
        }//produces List<Feature>

        //List<Feature> to FeatureCollection to GeoJsonSource as source
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(source_songlayer);
        Source source = new GeoJsonSource(SONGS_SOURCE_ID, featureCollection);
        //construct layer
        SymbolLayer symbolLayer = new SymbolLayer(SONGS_LAYER_ID, SONGS_SOURCE_ID)
                .withProperties(PropertyFactory.iconImage(SONGS_ICON_ID),
                        iconAllowOverlap(true)
                );

        //create mapstyle + adding marker image
        Style.Builder styleBuilder = new Style.Builder()
                .withImage(SONGS_ICON_ID, BitmapFactory.decodeResource(
                        MainActivity.this.getResources(),
                        R.drawable.songpin
                        )
                )
                .withSource(source)
                .withLayer(symbolLayer);
        //mandatory set mapstyle; onStyleLoaded @end to perform extra data adds + adjustments
        mapboxMap.setStyle(styleBuilder,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        //Map setup+style load completed;
                        //add extra data and perform further adjustments here
                    }
                });



/* //markers using style; version 1
        mapboxMap.setStyle(new Style.Builder()
                .fromUri(getResources().getString(R.string.darkstyleURL))
                //add SymbolLayer icon image to this style
                .withImage(SONGS_ICON_ID, BitmapFactory.decodeResource(
                        MainActivity.this.getResources(),
                        R.drawable.listpin)
                )
                //add geojson (location data) source for these icons
                .withSource(new GeoJsonSource(SONGS_SOURCE_ID,
                                FeatureCollection.fromFeatures(source_songlayer)
                        )
                )
                //add the SymbolLayer to the mapstyle; icon placement properties handled
                .withLayer(new SymbolLayer(SONGS_LAYER_ID, SONGS_SOURCE_ID)
                        .withProperties(PropertyFactory.iconImage(SONGS_ICON_ID),
                                iconAllowOverlap(true)
                        )
                ), new Style.OnStyleLoaded() {

                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        //addtional data and adjustments done in here

                        enableLocationComponent(style);

                        //---old code used for testing
                        SymbolManager symbolManager =
                                new SymbolManager(mapView, mapboxMap, style); //init symbolmanager

                        //shift camera to ?? device location i.e. current user locaton
                        LatLng focus = new LatLng(51.051877, 13.741517);
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(focus)
                                        .zoom(12)
                                        .build()));

                        //-------demonstration; add star marker image
                        mapboxMap.getStyle().addImage("my-star-marker",
                                BitmapFactory.decodeResource(getResources(), R.drawable.star_marker));
                        symbolManager.create(new SymbolOptions()
                                .withLatLng(new LatLng(51.02855, 13.723903))
                                .withIconImage("my-star-marker")
                                .withIconAnchor("bottom"));
                        //--------
                    }
                }
        );
*/


        //---backup code for song loading
        /*
        mapboxMap.setStyle(
                new Style.Builder()
                        .fromUri(getResources()
                                .getString(R.string.darkstyleURL)),
                new Style.OnStyleLoaded() {



            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                mapboxMap.getStyle().addImage("red_marker",
                        BitmapFactory.decodeResource(getResources(),
                                R.drawable.songpin)); //add the symbol

                SymbolManager symbolManager =
                        new SymbolManager(mapView, mapboxMap, style); //init symbolmanager

                //adding songs from database to the map
                //?? replace this construction loop using
                SymbolOptions song_symbol = new SymbolOptions() //settings for default song symbol; init'd without latlng
                        .withIconImage("red_marker")
                        .withIconAnchor("bottom");
                for (int i = 0; i < song_list.size(); i++) { //loop to add db records to map
                    Song song = song_list.get(i);
                    double lat = (double) song.getLAT();
                    double lng = (double) song.getLNG();
                    symbolManager.create(song_symbol.withLatLng(new LatLng(lat, lng))
                    );
                }

                //shift camera to ?? device location i.e. current user locaton
                LatLng focus = new LatLng(51.051877, 13.741517);
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder()
                                .target(focus)
                                .zoom(12)
                                .build()));

                //-------demonstration; add star marker image
                mapboxMap.getStyle().addImage("my-star-marker",
                        BitmapFactory.decodeResource(getResources(), R.drawable.star_marker));
                symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(51.02855, 13.723903))
                        .withIconImage("my-star-marker")
                        .withIconAnchor("bottom"));
                //--------
                //--------demonstration; adding behavior/listeners
                symbolManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {
                        Toast.makeText(MainActivity.this, "HÜL/S590: Computer Pool",
                                Toast.LENGTH_LONG).show();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(symbol.getLatLng())
                                        .build()));
                    }
                });
            }
        });
*/

        //Map Listeners

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                Toast.makeText(MainActivity.this, "onClick: longclick to place marker", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
            @Override
            public boolean onMapLongClick(@NonNull LatLng point) {

                //??old code
                Style style = mapboxMap.getStyle();
                SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setIconIgnorePlacement(true);
                // Add symbol at specified lat/lon
                double lat = point.getLatitude(), lng = point.getLongitude();
                symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(lat, lng))
                        .withIconImage("red_marker")
                        .withIconAnchor("bottom")
                );

                Toast.makeText(MainActivity.this, "onLongClick: marker placed", Toast.LENGTH_LONG).show();

                //??***
                //Include a dialog to choose between playcing a song or a list
                // in case of playcing a list, the radius should be entered (further dialog) and then the db item should be created
                // if aborted, delete marker again
                //*****

                //creating new song entry and placing in database
                Song song = new Song();
                song.setLNG((float) lng);
                song.setLAT((float) lat);
                song.setNAME("!placeholdername!");
                songdao.insert(song);
                Toast.makeText(MainActivity.this, "record successfully added,", Toast.LENGTH_LONG).show();


                return true;
            }
        });

        //??***
        // we need an onclicklistener that starts playing songs / lists when you click on a marker on the map
        //*****

        mapboxMap.addOnFlingListener(new MapboxMap.OnFlingListener() {
            @Override
            public void onFling() {
                Toast.makeText(MainActivity.this, "onFling: Weeeeee", Toast.LENGTH_LONG).show();
            }
        });
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


    /*button IDs for reference
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

    //navigates to main activity which shows now playing info and a map with all playced songs as colorful bubbles
    public void onClickStartMainActivity(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //opens Lists Activity which shows all playcelists in a recycler view
    public void onClickStartListsActivity(MenuItem item) {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    //creates new item in Songs DB using now playing info and GPS info
    public void onClickPlayceCurrentHere(View view) {
        View contextView = findViewById(R.id.btn_playcer);
        Snackbar.make(contextView, R.string.btnWorking, Snackbar.LENGTH_SHORT)
                .show();
        //actual code:
        //*****
        // get GPS info
        // get API info / nowplaying
        // navigate to current position
        // set new marker
        // open dialog (do you want to playce [now playing song] here?)
        // if confirmed
        //   create new DB item
        //   reload map / make sure it appears on map
        // else
        //   dismiss and delete marker
        //*****
    }

    //slides in navigation drawer which handles account information (and what is displayed on the map)
    public void onClickOpenNavDrawer(View view) {
        DrawerLayout mDrawer = findViewById(R.id.mDrawer);
        mDrawer.openDrawer(findViewById(R.id.nav_drawer));
    }


    //opens spotify account dialog
    public void onClickOpenAccountDialog(View view) {
        // open dialog to log in or out / change account

        new MaterialAlertDialogBuilder(this, R.style.DialogTheme)
                .setPositiveButton("log in with Spotify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogIntoSpotify();
                    }
                })
                .setNegativeButton("log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogOutOfSpotify();
                    }
                })
                .setNeutralButton("load user pic", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoadUserPic();
                    }
                })
                .show();

        // align with API
        // -
        // -
        // -
    }



    //nav drawer checkbox handlers
    public void onClickCheckBox1(MenuItem item) {
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

    public static final String CLIENT_ID = "089d841ccc194c10a77afad9e1c11d54";
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;
    public SPUser CUser; // user profile

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public String mAccessToken;
    public String mAccessCode;
    public Call mCall;

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();

    }

    public void RequestCode() {
        final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.CODE);
        AuthenticationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request);
    }

    public void RequestToken() {
        final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN);
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }


    public void LogIntoSpotify() {
        if (mAccessToken == null) {
            // no login
            //Log.e("Chek","Check");
            RequestToken();
            Log.e("Chek", "Check32");
        } else {
            Toast.makeText(this, "ELSE", Toast.LENGTH_SHORT).show();
        }
    }

    public void LogOutOfSpotify() {
        Toast.makeText(this, R.string.btnWorking, Toast.LENGTH_LONG).show();
        //function to log out of spotify
        // -
        // -
        // -
    }


    private void LoadUserPic() {
        ImageView Upic = findViewById(R.id.nav_header_SProfilePicture);
        String url = CUser.getImages().get(0).getUrl();
        Glide.with(Upic).load(url).into(Upic);
    }


    public void GetUser() {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me") //get user data
                //.url("https://api.spotify.com/v1/me/player/currently-playing") //get current song
                .addHeader("Authorization", "Bearer " + mAccessToken)
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
                    CUser = gson.fromJson(JsonResponse, SPUser.class);
                    TextView Username = findViewById(R.id.nav_header_SUserName);
                    Username.setText(CUser.getDisplayName());
                    Log.e("Response", "User" + JsonResponse);
                    response.close();

                } catch (JSONException e) {
                    //Fail
                }
            }
        });
    }


    private AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type) {
        return new AuthenticationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email", "user-read-playback-state", "user-read-currently-playing", "user-read-private"})
                .setCampaign("your-campaign-token")
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.getAccessToken();
            Log.e("Token", "THere" + mAccessToken);
            GetUser();


        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            mAccessCode = response.getCode();
            Log.e("Code", "CHere " + mAccessCode);
            GetUser();
        }
    }


    private Uri getRedirectUri() {
        return new Uri.Builder()
                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(getString(R.string.com_spotify_sdk_redirect_host))
                .build();
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }


}