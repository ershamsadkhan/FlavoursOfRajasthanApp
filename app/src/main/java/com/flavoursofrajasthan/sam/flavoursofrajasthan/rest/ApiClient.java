package com.flavoursofrajasthan.sam.flavoursofrajasthan.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Configuration.Settings;

public class ApiClient {

    public static final String BASE_URL = Settings.BaseApiUrl;
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
