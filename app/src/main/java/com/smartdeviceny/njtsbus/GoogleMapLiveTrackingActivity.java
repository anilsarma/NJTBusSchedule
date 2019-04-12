package com.smartdeviceny.njtsbus;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartdeviceny.njtsbus.content_provider.ScheduleContentProvider;
import com.smartdeviceny.njtsbus.route.RouteDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class GoogleMapLiveTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

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
        setContentView(R.layout.google_map_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int widthFfragment = 300;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
//        widthTot = size.x;
//        heightTot = size.y;

        //findViewById(R.id.map).getLayoutParams().width = size.x-size.y;
        findViewById(R.id.map).getLayoutParams().height = size.y * 10 ;
    }

    // get busesh ttp://mybusnow.njtransit.com/bustime/map/getBusesForRoute.jsp?route=1
    // http://mybusnow.njtransit.com/bustime/map/getBusPredictions.jsp?bus=5820
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MAP", "called back" + googleMap);
        LatLng bus6357 =new LatLng(40.73301166666667,-74.16392833333333);
        LatLng bus5820 =new LatLng(40.544637, -74.504814);
        googleMap.addMarker(new MarkerOptions()
                .position(bus6357)
                .title("Bus 6357"));

        googleMap.addMarker(new MarkerOptions()
                .position(bus5820)
                .title("Bus 5820")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        PolylineOptions pt = new PolylineOptions();
        //
        pt.add(bus6357);
        pt.add(bus5820);
        Polyline line=googleMap.addPolyline(pt.width(5).color(Color.GREEN));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(bus6357));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);



        //googleMap.addMarker(new MarkerOptions().position(tmp).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


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
