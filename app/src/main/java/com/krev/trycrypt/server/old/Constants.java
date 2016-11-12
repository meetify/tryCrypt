package com.krev.trycrypt.server.old;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by Dima on 12.11.2016.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public final class Constants {
    public static final String address = "http://192.168.1.40:8080";
    public static final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final String device = "";
}
