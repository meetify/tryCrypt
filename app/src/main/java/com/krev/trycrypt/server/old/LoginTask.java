package com.krev.trycrypt.server.old;

import android.os.AsyncTask;
import android.util.Log;

import com.krev.trycrypt.asynctasks.Consumer;
import com.krev.trycrypt.model.Id;
import com.krev.trycrypt.model.entity.Login;
import com.vk.sdk.VKAccessToken;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.krev.trycrypt.server.old.Constants.JSON;
import static com.krev.trycrypt.server.old.Constants.address;
import static com.krev.trycrypt.server.old.Constants.client;
import static com.krev.trycrypt.server.old.Constants.mapper;

/**
 * sdklfajdskf
 * Created by Dima on 07.11.2016.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Deprecated
public class LoginTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = LoginTask.class.getName();
    private String mac;
    private VKAccessToken token;
    private Consumer<Boolean> consumer;


    public LoginTask(Consumer<Boolean> consumer, String mac, VKAccessToken token) {
        this.consumer = consumer;
        this.token = token;
        this.mac = mac;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }
        };
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Login login = new Login(new Id(Long.parseLong(token.userId)), token.accessToken, mac);
        try {
            Response response = client.newCall(new Request.Builder()
                    .url(address + "/login")
                    .post(RequestBody.create(JSON, mapper.writeValueAsString(login)))
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
