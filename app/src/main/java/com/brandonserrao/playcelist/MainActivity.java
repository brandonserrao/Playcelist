package com.brandonserrao.playcelist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.brandonserrao.playcelist.R;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapbox);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        //mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
        mapboxMap.setStyle(new Style.Builder().fromUri(getResources().getString(R.string.darkstyleURL)), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
/*                mapboxMap.getStyle().addImage("red-marker",
                        BitmapFactory.decodeResource(getResources(),
                                R.drawable.red_marker));*/
                SymbolManager symbolManager =
                        new SymbolManager(mapView, mapboxMap, style);
                symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(0,0))
                        .withIconImage("red-marker")
                        .withIconAnchor("bottom")
                );
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

}


