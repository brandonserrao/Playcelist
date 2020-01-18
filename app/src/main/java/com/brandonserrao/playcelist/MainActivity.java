package com.brandonserrao.playcelist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.brandonserrao.playcelist.R;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.FeatureCollection;
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
import com.mapbox.mapboxsdk.plugins.annotation.CircleManager;
import com.mapbox.mapboxsdk.plugins.annotation.CircleOptions;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    //map+mapbox var inits
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;

    //database implementation variables
    String db_name = "sqlstudio_db2_v5.sqlite";
    SongDAO songdao;
    List<Song> song_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Room.databaseBuilder(this, AppDatabase.class,db_name)
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
    public void onMapReady(@NonNull final MapboxMap mapboxMap) { //started on successfuly map creation; main activities start here

        MainActivity.this.mapboxMap = mapboxMap;//get the map java object
        //set style and
        mapboxMap.setStyle(new Style.Builder().fromUri(getResources().getString(R.string.darkstyleURL)), new Style.OnStyleLoaded() {

            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                mapboxMap.getStyle().addImage("red_marker",
                        BitmapFactory.decodeResource(getResources(),
                                R.drawable.red_marker)); //add the symbol

                SymbolManager symbolManager =
                        new SymbolManager(mapView, mapboxMap, style); //init symbolmanager

                //adding songs from database to the map
                SymbolOptions song_symbol = new SymbolOptions() //settings for default song symbol; init'd without latlng
                        .withIconImage("red_marker")
                        .withIconAnchor("bottom")
                        ;
                for (int i=0; i<song_list.size(); i++) { //loop to add db records to map
                    Song song = song_list.get(i);
                    double lat = (double) song.getLAT();
                    double lng = (double) song.getLNG();
                    symbolManager.create(song_symbol.withLatLng(new LatLng(lat,lng))
                    );
                }

                //shift camera to desired focus
                LatLng focus = new LatLng(51.051877,13.741517); //ideally current device location
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
        //-----------testing Map Listeners; taken from mapbox sdk documentation---------

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
                Style style = mapboxMap.getStyle();
                SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setIconIgnorePlacement(true);
                // Add symbol at specified lat/lon
                double lat = point.getLatitude(), lng = point.getLongitude();
                symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(lat,lng))
                        .withIconImage("red_marker")
                        .withIconAnchor("bottom")
                );
                Toast.makeText(MainActivity.this, "onLongClick: marker placed", Toast.LENGTH_LONG).show();

                //creating new song entry and placing in database
                Song song = new Song();
                song.setLNG((float)lng);song.setLAT((float)lat);
                song.setNAME("!placeholdername!");
                songdao.insert(song);
                Toast.makeText(MainActivity.this, "record successfully added,",Toast.LENGTH_LONG).show();


                return true;
            }
        });


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


    //Button Click Handlers
    public void onClickStartListsActivity(MenuItem item) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
    public void onClickStartSongsActivity(MenuItem item) {
        Intent intent = new Intent(this, SongsActivity.class);
        startActivity(intent);
    }
    public void onClickStartPlayground(MenuItem item) {
        Intent intent = new Intent(this, SpotifyPlayground.class);
        startActivity(intent);
    }


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