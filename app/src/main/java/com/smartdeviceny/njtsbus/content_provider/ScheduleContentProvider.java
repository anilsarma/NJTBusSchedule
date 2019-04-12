/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartdeviceny.njtsbus.content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.smartdeviceny.njtsbus.route.Route;
import com.smartdeviceny.njtsbus.route.RouteDetails;
import com.smartdeviceny.njtsbus.route.SQLHelper;
import com.smartdeviceny.njtsbus.route.SQLiteLocalDatabase;
import com.smartdeviceny.njtsbus.route.SqlUtils;
import com.smartdeviceny.njtsbus.route.Stop;
import com.smartdeviceny.njtsbus.route.StopTimeDetails;
import com.smartdeviceny.njtsbus.route.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.Nullable;


public class ScheduleContentProvider extends ContentProvider {

    // database
    //private ScheduleDatabaseHelper database;
    private SQLiteLocalDatabase database;

    // used for the UriMacher
    private static final int ROUTES_AT_STOP = 10;
    private static final int RECIPE_ID = 20;
    private static final int ALL_STOPS = 30;
    private static final int TRIP_STOPS = 40;
    private static final int RECIPE_SEARCH = 50;

    private static final String AUTHORITY = "com.smartdeviceny.njtsbus";

    private static final String BASE_PATH = "bus";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/getRoutesAt", ROUTES_AT_STOP);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/getTripStops", TRIP_STOPS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/allstops", ALL_STOPS);

        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/search/*", RECIPE_SEARCH);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/*", RECIPE_ID);
    }

    @Override
    public boolean onCreate() {
        Log.d("PROVIDER", "created");
        // make sure we have a database file
        File sqlfile = new File(getContext().getApplicationInfo().dataDir, "bus_data.db");
        if(!sqlfile.exists())  {
            sqlfile.getParentFile().mkdirs();
            try {
                FileOutputStream os = new FileOutputStream(sqlfile);
                Utils.writeExtractedFileToDisk(getContext().getAssets().open("bus_data.db"), os);
                Log.d("LOOP", "wrote file " + sqlfile + " " + sqlfile.length());
                os.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        database = new SQLiteLocalDatabase(getContext(), sqlfile.getName(), sqlfile.getParentFile().getAbsolutePath());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int uriType = sURIMatcher.match(uri);
        if (uriType == ROUTES_AT_STOP) {
            String stop_id = Uri.decode(uri.getQueryParameter("stop_id"));
            return getRoutesAt(stop_id);
        } else if (uriType == ALL_STOPS) {
            return getAllStops();
        }else if (uriType == TRIP_STOPS) {
            return getTripStops(  Uri.decode(uri.getQueryParameter("trip_id")));
        }


//        if (uriType == RECIPES) {
//        } else if (uriType == RECIPE_ID) {
//            return getRecipe(uri);
//        } else if (uriType == RECIPE_INGREDIENTS) {
//            return getIngredientsByRecipe(uri);
//        } else if (uriType == RECIPE_INSTRUCTIONS) {
//            return getInstructionsByRecipe(uri);
//        } else if (uriType == RECIPE_SEARCH) {
//            String query = Uri.decode(uri.getLastPathSegment());
//            return findRecipes(query);
//        } else {
//            throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
        return null;
    }

    private Cursor getRoutesAt(String stop_id) {
        return SQLHelper.getRoutesAt(database.getReadableDatabase(), stop_id);
    }

    private Cursor getAllStops() {
        return SQLHelper.getStops(database.getReadableDatabase());
    }
    private Cursor getTripStops(String trip_id) {
        return SQLHelper.getTripStops(database.getReadableDatabase(), trip_id);
    }
//    private Cursor findRecipes(String query) {
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        queryBuilder.setTables(RecipeTable.TABLE);
//        String[] projection = { RecipeTable.ID, RecipeTable.TITLE,
//                RecipeTable.DESCRIPTION, RecipeTable.PHOTO,
//                RecipeTable.PREP_TIME};
//        SQLiteDatabase db = database.getReadableDatabase();
//        queryBuilder.appendWhere(RecipeTable.TITLE + " like '%"
//                + query + "%'");
//        return queryBuilder.query(db, projection, null,
//                null, null, null, null);
//    }

//    public Cursor getRecipe(Uri uri) {
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        queryBuilder.setTables(RecipeTable.TABLE);
//        String[] projection = { RecipeTable.ID, RecipeTable.TITLE,
//                RecipeTable.DESCRIPTION, RecipeTable.PHOTO,
//                RecipeTable.PREP_TIME};
//        SQLiteDatabase db = database.getReadableDatabase();
//        queryBuilder.appendWhere(RecipeTable.ID + "='"
//                + uri.getLastPathSegment() + "'");
//        Cursor cursor = queryBuilder.query(db, projection, null,
//                null, null, null, null);
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//
//        return cursor;
//    }

//    public Cursor getIngredientsByRecipe(Uri uri) {
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        queryBuilder.setTables(RecipeTable.TABLE + ", " + RecipeIngredientTable.TABLE);
//        queryBuilder.appendWhere(RecipeTable.ID + "='" + uri.getLastPathSegment() + "' AND " + RecipeIngredientTable.RECIPE_ID + "=" + RecipeTable.ID + "");
//        String[] projection = {RecipeIngredientTable.AMOUNT, RecipeIngredientTable.DESCRIPTION};
//        SQLiteDatabase db = database.getReadableDatabase();
//        Cursor cursor = queryBuilder.query(db, projection, null, null, null, null, null);
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        return cursor;
//    }

//    public Cursor getInstructionsByRecipe(Uri uri) {
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        queryBuilder.setTables(RecipeTable.TABLE + ", " + RecipeInstructionsTable.TABLE);
//        queryBuilder.appendWhere(RecipeTable.ID + "='" + uri.getLastPathSegment() + "' AND " + RecipeInstructionsTable.RECIPE_ID + "=" + RecipeTable.ID + "");
//        String[] projection = {RecipeInstructionsTable.NUM, RecipeInstructionsTable.DESCRIPTION, RecipeInstructionsTable.PHOTO};
//        SQLiteDatabase db = database.getReadableDatabase();
//        Cursor cursor = queryBuilder.query(db, projection, null, null, null, null, null);
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//        return cursor;
//    }

    @Override
    public String getType(Uri uri) {
        return BASE_PATH;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    void extractFile(Context context) throws IOException {
        File sqlfile = new File(context.getApplicationInfo().dataDir, "bus_data.db");
        if (!sqlfile.exists()) {
            sqlfile.getParentFile().mkdirs();
            try {
                FileOutputStream os = new FileOutputStream(sqlfile);
                Utils.writeExtractedFileToDisk(context.getAssets().open("bus_data.db"), os);
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * This helper loads the SQLite database included with the app
     * in the assets folder.
     */
    public class ScheduleDatabaseHelper extends SQLiteAssetHelper {

        //private static final String DATABASE_NAME = "bus_data.db";
        private static final int DATABASE_VERSION = 1;

        public ScheduleDatabaseHelper(Context context, File name) {
            super(context, name.getName(), name.getParentFile().getAbsolutePath(), null, DATABASE_VERSION);
        }
    }


    public static ArrayList<Stop> getAllStops(Context context, @Nullable String filter) {
        Uri ingredientsUri = ScheduleContentProvider.CONTENT_URI.buildUpon().appendPath("allstops").build();
        Cursor cursor = context.getContentResolver().query(ingredientsUri, null, null, null, null);
        ArrayList<Stop> stops =  Utils.parseCursor(Stop.class, cursor);
        if( filter!=null && !filter.isEmpty()) {
            ArrayList<Stop> filterStops = new ArrayList<>();
            String tokens[] = filter.split("\\s+");
            for(Stop stop:stops) {
                String sstr = stop.stop_name + " " + stop.location + " " + stop.stop_code;
                sstr = sstr.toLowerCase();
                boolean add = true;
                for( String s: tokens) {
                    if(!sstr.contains(s.toLowerCase())) {
                        add = false;
                        break;
                    }
                }
                if(add) {
                    filterStops.add(stop);
                }
            }
            stops = filterStops;
        }

        return stops;
    }
    public static ArrayList<RouteDetails> getRoutesAtStop(Context context, String stop_id) {
        Log.d("PROVIDER", "getRoutesAtStop?" + stop_id);

        Uri ingredientsUri = ScheduleContentProvider.CONTENT_URI.buildUpon().appendPath("getRoutesAt").appendQueryParameter("stop_id", stop_id).build();
        Cursor cursor = context.getContentResolver().query(ingredientsUri, null, null, null, null);
        return Utils.parseCursor(RouteDetails.class, cursor);
    }
    public static ArrayList<StopTimeDetails> getTripStops(Context context, String trip_id) {
        Log.d("PROVIDER", "getTripStops?" + trip_id);
        Uri ingredientsUri = ScheduleContentProvider.CONTENT_URI.buildUpon().appendPath("getTripStops").appendQueryParameter("trip_id", trip_id).build();
        Cursor cursor = context.getContentResolver().query(ingredientsUri, null, null, null, null);
        return Utils.parseCursor(StopTimeDetails.class, cursor);
    }
}
