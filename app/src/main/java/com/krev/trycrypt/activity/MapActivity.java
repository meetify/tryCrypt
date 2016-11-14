package com.krev.trycrypt.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.krev.trycrypt.R;
import com.krev.trycrypt.adapters.GooglePlaceAdapter;
import com.krev.trycrypt.server.PlaceController;
import com.krev.trycrypt.server.model.GooglePlace;
import com.krev.trycrypt.server.model.entity.Location;
import com.krev.trycrypt.utils.Consumer;
import com.krev.trycrypt.utils.DrawerUtils;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = MapActivity.class.toString();
    public static ProfileDrawerItem profile;
    private MapView mapView;
    private SweetSheet sweetSheet;
    private CustomDelegate customDelegate;
    private View view;


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, getString(R.string.accessToken));

        @SuppressLint("InflateParams")
        ViewGroup group = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_map, null);
        sweetSheet = new SweetSheet(group);

        customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangAnimation);
        view = LayoutInflater.from(this).inflate(R.layout.activity_google_places, group, false);
        customDelegate.setCustomView(view);
        sweetSheet.setDelegate(customDelegate);
        setContentView(group);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        MapActivity.this.onMapClick(point);
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
        DrawerUtils.INSTANCE.getDrawer(this);
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

    public void onMapClick(LatLng point) {
        PlaceController.INSTANCE.nearby(new Consumer<GooglePlace>() {
            @Override
            public void accept(final GooglePlace googlePlace) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sweetSheet.isShow()) {
                            sweetSheet.dismiss();
                        }
                        ((ListView) view.findViewById(R.id.listViewGPlaces))
                                .setAdapter(new GooglePlaceAdapter(googlePlace, MapActivity.this));
                        customDelegate.setCustomView(view);
                        sweetSheet.show();
                    }
                });
            }
        }, new Location(point.getLatitude(), point.getLongitude()));
    }
}
