package com.smartdeviceny.njtsbus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartdeviceny.njtsbus.content_provider.ScheduleContentProvider;
import com.smartdeviceny.njtsbus.route.RouteDetails;
import com.smartdeviceny.njtsbus.route.SQLWrapper;
import com.smartdeviceny.njtsbus.route.Stop;
import com.smartdeviceny.njtsbus.route.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RouteListActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private RouteListRVAdapter adapter;
    private int color = 0;
    private List<RouteDetails> data;


    String stop_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        String tmp = getIntent().getStringExtra("stop_id");

        if(tmp !=null && !tmp.isEmpty()) {
            stop_id=tmp;
        }
        if(savedInstanceState !=null) {
            tmp = savedInstanceState.getString("stop_id");
        }
        if(tmp !=null && !tmp.isEmpty()) {
            stop_id=tmp;
        }
        SharedPreferences config =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if( stop_id ==null || stop_id.isEmpty()) {
            stop_id = config.getString("current_stop_id", "");
        } else {
            config.edit().putString("current_stop_id", stop_id).commit();
        }
        Toolbar toolbar = findViewById(R.id.toolbar_recycler_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initData();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String tmp = "";
        if(savedInstanceState !=null) {
            tmp = savedInstanceState.getString("stop_id");
        }
        if(tmp !=null && !tmp.isEmpty()) {
            stop_id=tmp;
        }
    }

    private void initData() {
        data = new ArrayList<>();
//        for (int i = 1; i <= 20; i++) {
//            data.add(i + "");
//        }
//        HashMap<String, Object> payload = new HashMap<>();
//        payload.put("stop_id", "");
//        payload.put("stop_code", "");
//        payload.put("stop_name", "");
//        //payload.put("stop_desc", stop_desc);
//        payload.put("stop_lat", 0.0);
//        payload.put("stop_lon", 0.0);
//        payload.put("zone_id", 0);
        //data.add(route);
        doBackgroundLoad(stop_id);
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            String tmp = (String)savedInstanceState.getSerializable("stop_id");
            if (!tmp.isEmpty()) {
                if(!stop_id.isEmpty()) {
                    stop_id = tmp;
                }
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("stop_id", stop_id);
        super.onSaveInstanceState(outState);

       // outState.putString("stop_id", stop_id);
    }

    private void initView() {
        fab = findViewById(R.id.fab_recycler_view);
        mRecyclerView = findViewById(R.id.recycler_view_recycler_view);

        if (getScreenWidthDp() >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (getScreenWidthDp() >= 800) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new RouteListRVAdapter(this);
        mRecyclerView.setAdapter(adapter);
        adapter.addHeader();
        adapter.setItems(data);
        adapter.addFooter();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //adapter.addItem(linearLayoutManager.findFirstVisibleItemPosition() + 1, insertData);
            }
        });
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_COARSE_LOCATION  },
                 12 );
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    13 );
        }
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_recycler_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doBackgroundLoad(stop_id);
            }
        });

       // mRecyclerView.addOnScrollListener(scrollListener);
    }



    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    private void doBackgroundLoad(String stop_id) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                Log.d("LOOP", "rnninger ... ");
                //SQLWrapper wrapper = SQLSingleton.getInstance(getApplicationContext()).getWrapper();
                ArrayList<RouteDetails> routes = ScheduleContentProvider.getRoutesAtStop(getApplicationContext(), stop_id);
                System.err.println("here routes:" + routes.size());
                Collections.sort(routes, new Comparator<RouteDetails>() {
                    @Override
                    public int compare(RouteDetails o1, RouteDetails o2) {
                        return o1.arrival_time.compareTo(o2.arrival_time);
                    }
                });
                new Handler(getApplicationContext().getMainLooper()).post(() -> {
                    color %= 4;
                    adapter.setItems(routes);

                    adapter.setColor(++color);
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                });

                return "";
            }
        }.execute("");
    }

}
