package com.smartdeviceny.njtsbus.route;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class StopTimeDetails {

    public String trip_headsign;
    public int trip_id;
    public String arrival_time;
    public String departure_time;
    public int stop_id;
    public int stop_sequence;
    //pickup_type;
    //drop_off_type;
    public double shape_dist_traveled;
    public double stop_lat;
    public double stop_lon;
    public String stop_name;
    public String route_short_name;


    public StopTimeDetails(HashMap<String, Object> data) {
        trip_headsign = data.get("trip_headsign").toString();
        trip_id = (Integer) data.get("trip_id");
        arrival_time = data.get("arrival_time").toString();
        departure_time = data.get("departure_time").toString();
        stop_id = (Integer) data.get("stop_id");
        stop_sequence = (Integer) data.get("stop_sequence");
        shape_dist_traveled = (Double) data.get("shape_dist_traveled");
        //stop_desc = data.get("stop_desc").toString();
        stop_lat = (Double) data.get("stop_lat");
        stop_lon = (Double) data.get("stop_lon");
        stop_name = data.get("stop_name").toString();


        //location.setLatitude(stop_lat);
        //location.setLongitude(stop_lon);
    }


    @Override
    public String toString() {
        return "StopTimeDetails@" + stop_id + " stop_name:" + stop_name + "/" + stop_name + " [" + stop_lat + "," + stop_lon + "]";
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