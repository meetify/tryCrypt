package com.krev.trycrypt.server;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.krev.trycrypt.asynctasks.Consumer;
import com.krev.trycrypt.model.Id;
import com.krev.trycrypt.model.entity.Login;
import com.vk.sdk.VKAccessToken;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.krev.trycrypt.server.Constants.address;
import static com.krev.trycrypt.server.Constants.JSON;
import static com.krev.trycrypt.server.Constants.client;
import static com.krev.trycrypt.server.Constants.mapper;

/**
 * sdklfajdskf
 * Created by Dima on 07.11.2016.
 */

public class LoginTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = LoginTask.class.getName();
    private String mac;
    private VKAccessToken token;
    private Consumer<Boolean> consumer;


    public LoginTask(Consumer<Boolean> consumer, String mac, VKAccessToken token) {
        this.consumer = consumer;
        this.token = token;
        this.mac = mac;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Login login = new Login(new Id(Long.parseLong(token.userId)), token.accessToken, mac);
        try {
            System.out.println(mapper.writeValueAsString(login));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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
