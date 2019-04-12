package com.smartdeviceny.njtsbus.retrofit;

public class ApiNJT {

    public static final String BASE_URL = "http://mybusnow.njtransit.com/";

    public static NJTLiveBusService getNJTService() {
        return RetrofitClient.getClient(BASE_URL).create(NJTLiveBusService.class);
    }
}
