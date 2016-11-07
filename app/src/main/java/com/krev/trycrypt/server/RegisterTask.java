package com.krev.trycrypt.server;

import android.os.AsyncTask;
import android.util.Log;

import com.krev.trycrypt.asynctasks.Consumer;
import com.krev.trycrypt.model.entity.User;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.krev.trycrypt.server.Constants.address;
import static com.krev.trycrypt.server.Constants.JSON;
import static com.krev.trycrypt.server.Constants.client;
import static com.krev.trycrypt.server.Constants.mapper;

/**
 * Created by Dima on 07.11.2016.
 */

public class RegisterTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = RegisterTask.class.getName();
    private Consumer<Boolean> consumer;
    private User user;

    public RegisterTask(Consumer<Boolean> consumer, User user) {
        this.consumer = consumer;
        this.user = user;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Response response = client.newCall(new Request.Builder()
                    .url(address + "/user?create=yes")
                    .post(RequestBody.create(JSON, mapper.writeValueAsString(user)))
                    .build()
            ).execute();
            Log.d(TAG, "doInBackground: " + response);
            Log.d(TAG, "doInBackground: " + response.body().string());
            Log.d(TAG, "doInBackground: " + response.headers());
            consumer.accept(response.code() == 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
