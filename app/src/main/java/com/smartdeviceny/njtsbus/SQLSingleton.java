package com.smartdeviceny.njtsbus;

import android.content.Context;
import android.util.Log;

import com.smartdeviceny.njtsbus.route.SQLWrapper;
import com.smartdeviceny.njtsbus.route.Utils;

import java.io.File;
import java.io.FileOutputStream;

public class SQLSingleton {
    static SQLSingleton singleton=null;
     SQLWrapper wrapper=null;
    private SQLSingleton(Context context) {
        File sqlfile = new File(context.getApplicationInfo().dataDir, "bus_data.db");
        if (!sqlfile.exists()) {
            sqlfile.getParentFile().mkdirs();
            try {
                FileOutputStream os = new FileOutputStream(sqlfile);
                Utils.writeExtractedFileToDisk(context.getAssets().open("bus_data.db"), os);
                //Utils.writeExtractedFileToDisk(Utils.getFileFromZip(getAssets().open("bus_data.db"), "bus_data.db"), os);
                Log.d("LOOP", "wrote file " + sqlfile + " " + sqlfile.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        wrapper = new SQLWrapper(context.getApplicationContext(), sqlfile.getName(), sqlfile.getName());
        wrapper.open();
    }

    public SQLWrapper getWrapper() {
        return  wrapper;
    }
    public static SQLSingleton getInstance(Context context) {
        if( singleton ==null) {
            singleton = new SQLSingleton(context);
        }
        return singleton;
    }
}
