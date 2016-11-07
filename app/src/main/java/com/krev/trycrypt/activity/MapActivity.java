package com.krev.trycrypt.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.krev.trycrypt.R;
import com.krev.trycrypt.adapters.GooglePlaceAdapter;
import com.krev.trycrypt.asynctasks.Consumer;
import com.krev.trycrypt.model.GooglePlace;
import com.krev.trycrypt.server.GooglePlaceTask;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = MapActivity.class.toString();
    private MapView mapView;
    private ListView listView;
    private GooglePlaceAdapter googlePlaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.accessToken));
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView);
        listView = (ListView) findViewById(R.id.listView);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        new GooglePlaceTask(new Consumer<GooglePlace>() {
                            @Override
                            public void accept(final GooglePlace googlePlace) {
                                googlePlaceAdapter = new GooglePlaceAdapter(googlePlace, MapActivity.this);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listView.setAdapter(googlePlaceAdapter);
                                        registerForContextMenu(listView);
                                    }
                                });
                            }
                        }, point).execute();
                    }
                });

                mapboxMap.setCameraPosition(new CameraPosition.Builder()
                        .bearing(0F)
                        .tilt(0F)
                        .zoom(15F)
                        .target(new LatLng(48.514308545F, 35.0879165F))
                        .build()
                );

                mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng point) {

                    }
                });
            }
        });
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
