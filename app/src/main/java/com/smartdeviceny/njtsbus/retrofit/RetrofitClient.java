package com.smartdeviceny.njtsbus.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            //retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
            retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory( SimpleXmlConverterFactory.create()).build();
        }
        return retrofit;
    }
}
