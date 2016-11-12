package com.krev.trycrypt.server.old;

import android.os.AsyncTask;

import com.krev.trycrypt.asynctasks.Consumer;
import com.krev.trycrypt.model.GooglePlace;
import com.krev.trycrypt.model.entity.Location;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;

import okhttp3.Request;

import static com.krev.trycrypt.server.old.Constants.address;
import static com.krev.trycrypt.server.old.Constants.client;
import static com.krev.trycrypt.server.old.Constants.mapper;


/**
 * Created by Dima on 28.10.2016.
 */
@Deprecated
@SuppressWarnings({"WeakerAccess", "unused"})
public class GooglePlaceTask extends AsyncTask<Void, Void, Void> {
    private Consumer<GooglePlace> consumer;
    private Location location;

    public GooglePlaceTask(Consumer<GooglePlace> consumer, LatLng location) {
        this.consumer = consumer;
        this.location = new Location(location.getLatitude(), location.getLongitude());
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            consumer.accept(GooglePlace.Companion.getMapper().readValue(client
                    .newCall(new Request
                            .Builder()
                            .url(address + "/place/nearby?location=" + mapper.writeValueAsString(location))
                            .get().build())
                    .execute().body().string(), GooglePlace.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
