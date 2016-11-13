package com.krev.trycrypt.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.krev.trycrypt.R;
import com.krev.trycrypt.asynctasks.Consumer;
import com.krev.trycrypt.asynctasks.DownloadImageTask;
import com.krev.trycrypt.asynctasks.Supplier;
import com.krev.trycrypt.model.Id;
import com.krev.trycrypt.model.entity.Location;
import com.krev.trycrypt.model.entity.User;
import com.krev.trycrypt.server.LoginController;
import com.krev.trycrypt.server.Task;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.methods.VKApiUsers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

/**
 * try
 * Created by Dima on 15.10.2016.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.toString();
    private static final String EX = "https://maps.googleapis.com/maps/api/place/photo?photoreference=CoQBdwAAAEk8DgoRZKfmwQY6U8pYH7C52yHuYbJ16nED5IIuBl6_tAzZR41V-9jtr7tXZYkk7XH4oMJMTLH6pQCJ3FFxUEZjB12GSpfsg1SsP40Ai7pRQDIfHFscjSeaJPqF9Z6M0qhMaZ0hWmRw4SBoBB4lv26e9RAUURDci9weyCUElmZYEhAP23JEaU7cwP6Y5PtMxrKnGhT2vEhqcqDK0n4hChmJZnirk7opuQ&key=AIzaSyBgnGyxIek6PtMuVARZmVfaEtlH0Wiazms&maxwidth=600";
    private static String address = null;
    private static VKAccessToken token = null;
    private Consumer<ProfileDrawerItem> finishConsumer = new Consumer<ProfileDrawerItem>() {
        @Override
        public void accept(ProfileDrawerItem profileDrawerItem) {
            MapActivity.profile = profileDrawerItem;
            startActivity(new Intent(LoginActivity.this, MapActivity.class));
        }
    };
    private Consumer<Boolean> loginConsumer = new Consumer<Boolean>() {
        @Override
        public void accept(Boolean result) {
            if (!result) {
                register();
                return;
            }
            loginFinished();
        }
    };
    private Consumer<Boolean> registerConsumer = new Consumer<Boolean>() {
        @Override
        public void accept(Boolean result) {
            login();
        }
    };
    private Consumer<Boolean> checkConsumer = new Consumer<Boolean>() {
        @Override
        public void accept(Boolean result) {
            if (result) {
                loginFinished();
            } else {
                login();
            }
        }
    };

    private HashSet<Id> parseFriends(String friends) {
        HashSet<Id> parsed = new HashSet<>();
        for (String friend : friends.replaceAll("[\\[\\]]", "").split(",")) {
            parsed.add(new Id(Long.parseLong(friend)));
        }
        return parsed;
    }

    private void check() {
        LoginController.INSTANCE.check(checkConsumer, address, VKAccessToken.currentToken());
    }

    private void login() {
        LoginController.INSTANCE.login(loginConsumer, address, VKAccessToken.currentToken());
    }

    private void register() {
        VKRequest.VKRequestListener listener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                HashSet<Id> friends = new HashSet<>();
                try {
                    friends = parseFriends(response.json
                            .getJSONObject("response")
                            .getJSONArray("items").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                User user = new User(
                        new Id(Long.parseLong(token.userId)),
                        new Location(), friends, new HashSet<Id>(), new HashSet<Id>()
                );
                LoginController.INSTANCE.register(registerConsumer, user);
            }
        };
        new VKApiFriends().get(new VKParameters()).executeWithListener(listener);
    }

    private void loginFinished() {
        VKRequest.VKRequestListener listener = new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                final StringBuilder name = new StringBuilder();
                final StringBuilder photo = new StringBuilder();
                try {

                    JSONObject obj = response.json.getJSONArray("response").getJSONObject(0);
                    Log.d(TAG, "onComplete: " + obj.toString());

                    name.append(obj.getString("first_name")).append(" ")
                            .append(obj.getString("last_name"));
                    photo.append(obj.getString("photo_50"));

                    new Task<>(new Supplier<ProfileDrawerItem>() {
                        @Override
                        public ProfileDrawerItem accept() {
                            try {
                                return new ProfileDrawerItem()
                                        .withName(name.toString())
                                        .withIcon(BitmapFactory
                                                .decodeStream(new URL(photo.toString()).openStream()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }, finishConsumer).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        VKParameters parameters = new VKParameters();
        parameters.put("fields", "photo_50");
        new VKApiUsers().get(parameters).executeWithListener(listener);
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (address == null) {
            address = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                    .getConnectionInfo()
                    .getMacAddress();
        }
        if (VKSdk.isLoggedIn()) {
            check();
        }
        findViewById(R.id.buttonLoginVK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "login initiated");
                if (!VKSdk.isLoggedIn()) {
                    VKSdk.login(LoginActivity.this, "friends", "photos");
                } else {
                    token = VKAccessToken.currentToken();
                    check();
                }
            }
        });
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadImageTask((ImageView) findViewById(R.id.imageView), null).execute(EX);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {

            @Override
            public void onResult(VKAccessToken res) {
                Log.d(TAG, "everything is fine, going to talk with server");
                check();
            }

            @Override
            public void onError(VKError error) {
                Log.d(TAG, "shit happens");
            }
        })) {
            token = VKAccessToken.currentToken();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
