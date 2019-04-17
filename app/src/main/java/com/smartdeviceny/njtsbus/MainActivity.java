package com.smartdeviceny.njtsbus;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.smartdeviceny.njtsbus.content_provider.ScheduleContentProvider;
import com.smartdeviceny.njtsbus.model.BusRoutesModelView;
import com.smartdeviceny.njtsbus.retrofit.ApiNJT;
import com.smartdeviceny.njtsbus.retrofit.Buses;
import com.smartdeviceny.njtsbus.retrofit.NJTLiveBusService;
import com.smartdeviceny.njtsbus.route.RouteStartStopDetails;
import com.smartdeviceny.njtsbus.route.Stop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private RecyclerViewAdapter adapter;
    private int color = 0;
    private List<Stop> data;
    private String insertData;
    private boolean loading;
    private int loadTimes;
    private String currentFilter = "";
    NJTLiveBusService service;
    BusRoutesModelView busRoutesModelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ApiNJT.getNJTService();

        busRoutesModelView = ViewModelProviders.of(this).get(BusRoutesModelView.class);

        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initData();
        initView();

        final android.widget.SearchView searchView = (android.widget.SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(searchView, "Search result " + query + " "  + searchView.getContext(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                Intent intent = new Intent(searchView.getContext(), SearchActivity.class);
//                intent.setAction(Intent.ACTION_SEARCH);
//                intent.putExtra(SearchManager.QUERY, query);
//                startActivity(intent);
                doBackgroundLoad(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                //doBackgroundLoad(s);
                currentFilter = s;
                new Handler(getApplicationContext().getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if( currentFilter.equals(s)) {
                            doBackgroundLoad(s);
                        }
                    }
                });
                //nackbar.make(searchView, "Search Change  " + s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.bus_stops_nearby) {
            Intent intent = new Intent();
            intent.setClass(this, StopsNearbyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView)item.getActionView();
            // Assumes current activity is the searchable activity
            if(searchView !=null){
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
            }
            Intent intent = new Intent();
            intent.setClass(this, SearchableActivity.class);
            //startActivity(intent);


        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initData() {
        data = new ArrayList<>();
        doBackgroundLoad(null);
        insertData = "0";
        loadTimes = 0;
    }

    private void initView() {
        //fab = findViewById(R.id.fab_recycler_view);
        mRecyclerView = findViewById(R.id.main_recycler_view_recycler_view);

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

        adapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(adapter);
        adapter.addHeader();
        adapter.setItems(data);
        //adapter.addFooter();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //adapter.addItem(linearLayoutManager.findFirstVisibleItemPosition() + 1, insertData);
                getBuses();
            }
        });
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 12);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 13);
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

        swipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout_recycler_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // talk tot he looper ??
                doBackgroundLoad(null); // what about filters.

            }
        });
    }


    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }


    private void doBackgroundLoad(String filter) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                Log.d("MainAct", "doInBackground ... " + filter);
                ArrayList<Stop> undstops =new ArrayList<Stop>();

                if(filter == null || filter.isEmpty()) {
                    undstops = ScheduleContentProvider.getAllStops(getApplicationContext(), filter);
                }

                try {
                    List<RouteStartStopDetails> details = ScheduleContentProvider.getStartStopBusRoutes(getApplicationContext(), "WASHINGTON BLVD", "RT-18");
                    for (RouteStartStopDetails d : details) {
                        Log.d("STSP", d.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
               List<RecyclerViewAdapter.StopHolder> tmpStops = null;
                if( !undstops.isEmpty()) {
                    tmpStops = adapter.decorateMiles(undstops);
                }
                List<RecyclerViewAdapter.StopHolder> stops = tmpStops;
               // System.err.println("here getStops:" + stops.size());

                new Handler(getApplicationContext().getMainLooper()).post(() -> {
                    color %= 4;
                    if( stops !=null ) {
                        adapter.setDecoratedItems(stops);
                    } else {
                        adapter.applyFilters(filter);
                    }
                    adapter.setColor(++color);
                    swipeRefreshLayout.setRefreshing(false);

                    adapter.notifyDataSetChanged();
                });

                return "";
            }
        }.execute("");



    }


    void getBuses() {
        service.getBuses("1").enqueue(new Callback<Buses>() {
            @Override
            public void onResponse(Call<Buses> call, Response<Buses> response) {
                if(response.isSuccessful()) {
                    Log.d("MAIN", "got" + response.body().getBus());
                } else {
                    Log.d("MAIN", "fail" + response);
                    //Snackbar.make(get, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<Buses> call, Throwable t) {
                Log.d("MAIN", "call failed" + call);
                t.printStackTrace();
            }
        });
    }
}
