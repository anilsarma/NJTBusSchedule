package com.smartdeviceny.njtsbus.route;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Route {
    final DateFormat dateTim24HrFmt = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    //final DateFormatCont time24HFmt = new SimpleDateFormat("HH:mm:ss");
    final DateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
    public String station_code;
    public String departture_time;
    public String arrival_time;
    public String block_id;
    public String route_name;
    public String trip_id;

    public String date;
    public String header;
    public String from;
    public String to;
    public boolean favorite = false;

    public Date date_as_date;
    public Date departure_time_as_date;
    public Date arrival_time_as_date;



    public Route(String station_code, String date, String from, String to, HashMap<String, Object> data, boolean isfavorite) {
        this.station_code = station_code;
        departture_time = data.get("departure_time").toString();
        arrival_time = data.get("destination_time").toString();
        block_id = data.get("block_id").toString();
        route_name = data.get("route_long_name").toString();
        trip_id = data.get("trip_id").toString();
        this.from = from;
        this.to = to;


        this.favorite = isfavorite;
        try {
            // remember hours are more than 24 hrs here to represent the next day.
            this.departure_time_as_date = dateTim24HrFmt.parse(date + " " + departture_time);
            this.arrival_time_as_date = dateTim24HrFmt.parse(date + " " + arrival_time);

            this.date = dateFmt.format(departure_time_as_date);
            this.date_as_date = dateFmt.parse(this.date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = "" + this.date;
        this.header = this.date + " " + from + " => " + to;
    }


    public Date getDate(String time) throws ParseException {
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date tm = dateTimeFormat.parse(date + " " + time);
        return tm;
    }


    @Override
    public String toString() {
        return "Route@" + block_id + " Time:" + departture_time;
    }

    public void marshall(JSONObject payload) throws JSONException {
        payload.put("station_code", station_code);
        payload.put("departture_time", departture_time);
        payload.put("arrival_time", arrival_time);
        payload.put("block_id", block_id);
        payload.put( "route_name", route_name);
        payload.put( "trip_id", trip_id);
        payload.put("date", date);
        payload.put( "header", header);
        payload.put( "from", from);
        payload.put( "to", to);
        payload.put("favorite", favorite);
        payload.put( "date_as_date", date_as_date);
        payload.put( "departure_time_as_date", departure_time_as_date);
        payload.put( "arrival_time_as_date", arrival_time_as_date);

    }

    public void unmarshal(JSONObject payload) throws JSONException {
        station_code = payload.getString("station_code");
        departture_time = payload.getString("departture_time");
        block_id = payload.getString("block_id");
        route_name = payload.getString("route_name");
        trip_id = payload.getString("trip_id");
        date = payload.getString("date");
        header = payload.getString("header");
        from = payload.getString("from");
        to = payload.getString("to");
        favorite = payload.getBoolean("favorite");

        date_as_date = new Date(payload.getLong("date_as_date"));
        departure_time_as_date = new Date(payload.getLong("departure_time_as_date"));
        arrival_time_as_date = new Date(payload.getLong("arrival_time_as_date"));

    }
}