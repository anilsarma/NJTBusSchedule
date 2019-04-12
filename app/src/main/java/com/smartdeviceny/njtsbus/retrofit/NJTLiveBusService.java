package com.smartdeviceny.njtsbus.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NJTLiveBusService {

    @GET("/bustime/map/getBusesForRoute.jsp")
    Call<Buses> getBuses(@Query("route")String route_id );

    @GET("/bustime/map/getBusPredictions.jsp")
    Call<BusPredictions> getPrediction(@Query("bus")String bus_id );
}
