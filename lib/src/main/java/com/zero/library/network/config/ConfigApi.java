package com.zero.library.network.config;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ConfigApi {

    private final static String CONFIG_URL = "https://raw.githubusercontent.com/super1117/Config/master/healthy/config";

    public static void get(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(CONFIG_URL).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
