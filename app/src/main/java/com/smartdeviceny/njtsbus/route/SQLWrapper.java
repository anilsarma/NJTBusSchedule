package com.smartdeviceny.njtsbus.route;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;

public class SQLWrapper implements Closeable {
    SQLiteLocalDatabase sql;
    Context context;

    String masterFile = "rails_db.sql";
    String sqlFileName = "rails_checker_db.sql";
    SharedPreferences config;

    public SQLWrapper(Context context, String dbName, String dbMaster) {
        this.context = context;
        sqlFileName = dbName;
        masterFile = dbMaster;
        config = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public SQLWrapper(Context context, String dbName) {
        this.context = context;
        sqlFileName = dbName;
        config = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public void setSqlFileName(String sqlFileName) {
        this.sqlFileName = sqlFileName;
    }

    public String getSqlFileName() {
        return sqlFileName;
    }

    @Override
    public void close() {
        if (sql != null) {
            sql.close();
        }
    }

    public void setSql(SQLiteLocalDatabase sql) {
        this.sql = sql;
    }

    public SharedPreferences getConfig() {
        return config;
    }

    public SQLiteLocalDatabase getSql() {
        return sql;
    }

    public String makeFullPath(String path) {
        return context.getApplicationContext().getApplicationInfo().dataDir + File.separator + path;
    }

    public void open() {
        if (!Utils.copyFileIfNewer(makeFullPath(masterFile), makeFullPath(sqlFileName))) {
            throw new RuntimeException("cannot create " + sqlFileName + " from " + masterFile);
        }
        File f = new File(context.getApplicationContext().getApplicationInfo().dataDir + File.separator + getSqlFileName());
        if (f.exists()) {
            if (sql != null) {
                try {
                    sql.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sql = new SQLiteLocalDatabase(context.getApplicationContext(), f.getName(), null);
            sql.opendatabase();
            Log.d("WRAPPER", "opened sql database sql=" + sql);
        }
    }

    public String getStationCode(String station) {
        if (sql == null) {
            return "NY";
        }
        String value = SqlUtils.getStationCode(sql.getReadableDatabase(), station);
        Log.d("SVC", "looking up station code " + station + "=" + value);
        if (value == null || value.isEmpty()) {
            return "NY";
        }
        return value;
    }

    public HashSet<String> getFavortes() {
        HashSet<String> favorites = new HashSet<>();
        if (config != null) {
            Set<String> tmp = config.getStringSet(Config.FAVORITES, favorites);
            favorites = new HashSet<>(tmp); // re-init the data structures
        }
        return favorites;
    }

    public String[] get_values(String sqls, String key) {
        if (sql != null) {
            return SQLHelper.get_values(sql.getReadableDatabase(), sqls, key);
        }
        ArrayList<String> njtr = new ArrayList<>();
        return njtr.toArray(new String[]{});
    }

    public String[] getRoutes() {
        return get_values("select * from routes", "route_short_name");
    }

    public String[] getRouteStations(String route_name) {
        Log.d("SVC", "getRouteStations " + route_name + " SQL:" + sql);
        if (sql != null) {
            return SQLHelper.getRouteStations(sql.getReadableDatabase(), route_name);
        }
        ArrayList<String> njtr = new ArrayList<>();
        return njtr.toArray(new String[]{});
    }

    public ArrayList<Stop> getStops() {
        ArrayList<Stop> r = new ArrayList<>();
        if (sql == null) {
            return r;
        }
        SQLiteDatabase db = sql.getReadableDatabase();
        try {
            ArrayList<HashMap<String, Object>> stops = Utils.parseCursor(SQLHelper.getStops(db));
            for (HashMap<String, Object> stop : stops) {
                r.add(new Stop(stop));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return r;
    }


    public ArrayList<Route> getRoutes(String from, String to, @Nullable Integer date, @Nullable Integer delta) {
        ArrayList<Route> r = new ArrayList<>();

        if (sql == null) {
            return r;
        }
        if (delta == null) {
            delta = new Integer(2);
        }

        if (date == null) {
            date = Integer.parseInt(Utils.getLocaDate(0));
        }
        try {
            SQLiteDatabase db = sql.getReadableDatabase();
            HashSet<String> favorites = getFavortes();
            String station_code = getStationCode(from);
            delta = Math.max(1, delta);
            for (int i = -delta; i < delta; i++) {
                try {
                    Date stDate = DateFormatCont.yyyyMMddFmt.parse("" + date);
                    stDate = Utils.adddays(stDate, i);
                    ArrayList<HashMap<String, Object>> routes = Utils.parseCursor(SQLHelper.getRoutes(db, from, to, Integer.parseInt(DateFormatCont.yyyyMMddFmt.format(stDate))));
                    for (HashMap<String, Object> rt : routes) {
                        r.add(new Route(station_code, DateFormatCont.yyyyMMddFmt.format(stDate), from, to, rt, favorites.contains(rt.get("block_id").toString())));
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            return r;
        }
        return r;
    }

    public ArrayList<RouteDetails> getRoutesAtStop(String stop_id) {
        ArrayList<RouteDetails> r = new ArrayList<>();
        if (sql == null) {
            return r;
        }
        SQLiteDatabase db = sql.getReadableDatabase();
        try {
            ArrayList<HashMap<String, Object>> stops = Utils.parseCursor(SQLHelper.getRoutesAt(db, stop_id));
            for (HashMap<String, Object> stop : stops) {
                r.add(new RouteDetails(stop));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return r;
    }
    public ArrayList<StopTimeDetails> getTripStops(String trip_id) {
        ArrayList<StopTimeDetails> r = new ArrayList<>();
        if (sql == null) {
            return r;
        }
        SQLiteDatabase db = sql.getReadableDatabase();
        try {
            ArrayList<HashMap<String, Object>> stops = Utils.parseCursor(SQLHelper.getTripStops(db, trip_id));
            for (HashMap<String, Object> stop : stops) {
                r.add(new StopTimeDetails(stop));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return r;
    }


}
