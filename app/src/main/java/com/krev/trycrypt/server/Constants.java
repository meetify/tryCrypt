package com.krev.trycrypt.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by Dima on 07.11.2016.
 */

class Constants {
    static final String address = "http://192.168.1.40:8080";
    static final OkHttpClient client = new OkHttpClient();
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static final ObjectMapper mapper = new ObjectMapper();
}
