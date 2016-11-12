package com.krev.trycrypt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krev.trycrypt.R;
import com.krev.trycrypt.adapters.GooglePlaceAdapter;

public class PlacesActivity extends AppCompatActivity {

    private static final String TAG = PlacesActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        Log.d(TAG, "onCreate: " + "uhmm, something is ok, buuuuut");
        GooglePlaceAdapter googlePlaceAdapter = new GooglePlaceAdapter(MapActivity.googlePlace, this);
        try {
            Log.d(TAG, "onCreate: " + new ObjectMapper().writeValueAsString(MapActivity.googlePlace));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ListView listView = ((ListView) findViewById(R.id.listViewPlaces));
        listView.setAdapter(googlePlaceAdapter);
        registerForContextMenu(listView);
    }
}
