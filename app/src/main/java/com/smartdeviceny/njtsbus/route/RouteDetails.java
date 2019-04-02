package com.smartdeviceny.njtsbus.route;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RouteDetails {
    public String trip_id;
    public String arrival_time;
    public String departure_time;
    public String stop_id;
    public int stop_sequence;
    public int route_id;
    public String route_short_name;

    public Location location = new Location("");

    public RouteDetails(HashMap<String, Object> data) {
        trip_id = data.get("trip_id").toString();
        arrival_time = data.get("arrival_time").toString();
        departure_time = data.get("departure_time").toString();
        stop_id = data.get("stop_id").toString();
        stop_sequence = (Integer) data.get("stop_sequence");
        route_id = (Integer) data.get("route_id");
        route_short_name = data.get("route_short_name").toString();

    }


    @Override
    public String toString() {
        return "RouteDetails@" + trip_id + " route_short_name:"
                + route_short_name + "/" + route_id ;
    }

    public void marshall(JSONObject payload) throws JSONException {
//        payload.put("stop_id", stop_id);
//        payload.put("stop_code", stop_code);
//        payload.put("stop_name", stop_name);
//        //payload.put("stop_desc", stop_desc);
//        payload.put("stop_lat", stop_lat);
//        payload.put("stop_lon", stop_lon);
//        payload.put("zone_id", zone_id);


    }

    public void unmarshal(JSONObject payload) throws JSONException {
//        stop_id = payload.getString("stop_id");
//        stop_code = payload.getString("stop_code");
//        stop_name = payload.getString("stop_name");
//        //stop_desc = payload.getString("stop_desc");
//        stop_lat = payload.getDouble("stop_lat");
//        stop_lon = payload.getDouble("stop_lon");
//        zone_id = payload.getInt("zone_id");
//
//        location.setLatitude(stop_lat);
//        location.setLongitude(stop_lon);
    }
}