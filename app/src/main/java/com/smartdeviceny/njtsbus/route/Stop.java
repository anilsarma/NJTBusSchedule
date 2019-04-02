package com.smartdeviceny.njtsbus.route;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Stop {
    public String stop_id;
    public String stop_code;
    public String stop_name;
    //public String stop_desc;
    public double stop_lat;
    public double stop_lon;
    public int zone_id;

    public Location location = new Location("");

    public Stop(HashMap<String, Object> data) {
        stop_id = data.get("stop_id").toString();
        stop_code = data.get("stop_code").toString();
        stop_name = data.get("stop_name").toString();
        //stop_desc = data.get("stop_desc").toString();
        stop_lat = (Double) data.get("stop_lat");
        stop_lon = (Double) data.get("stop_lon");
        zone_id = (Integer) data.get("zone_id");

        location.setLatitude(stop_lat);
        location.setLongitude(stop_lon);
    }


    @Override
    public String toString() {
        return "Stop@" + stop_id + " stop_name:" + stop_name + "/" + stop_code + " [" + stop_lat + "," + stop_lon + "]";
    }

    public void marshall(JSONObject payload) throws JSONException {
        payload.put("stop_id", stop_id);
        payload.put("stop_code", stop_code);
        payload.put("stop_name", stop_name);
        //payload.put("stop_desc", stop_desc);
        payload.put("stop_lat", stop_lat);
        payload.put("stop_lon", stop_lon);
        payload.put("zone_id", zone_id);


    }

    public void unmarshal(JSONObject payload) throws JSONException {
        stop_id = payload.getString("stop_id");
        stop_code = payload.getString("stop_code");
        stop_name = payload.getString("stop_name");
        //stop_desc = payload.getString("stop_desc");
        stop_lat = payload.getDouble("stop_lat");
        stop_lon = payload.getDouble("stop_lon");
        zone_id = payload.getInt("zone_id");

        location.setLatitude(stop_lat);
        location.setLongitude(stop_lon);
    }
}