package com.tomasmichalkevic.seevilnius;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tomasmichalkevic.seevilnius.data.trafi.Stop;
import com.tomasmichalkevic.seevilnius.data.trafi.TrafiResponse;
import com.tomasmichalkevic.seevilnius.utils.NetworkRequestQueue;

import java.util.ArrayList;

import static com.tomasmichalkevic.seevilnius.MainActivity.LOG_TAG;

public class NearbyStationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final String LOG_TAG = NearbyStationsActivity.class.getSimpleName();
    private ArrayList<Stop> stopsList = new ArrayList<>();
    private static final String TRAFI_API_KEY = BuildConfig.TRAFI_API_KEY;
    private StopsAdapter stopsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_stations);
        recyclerView = findViewById(R.id.stops_recycler_view);
        stopsAdapter = new StopsAdapter(stopsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stopsAdapter);
        recyclerView.setHasFixedSize(true);
        getStopsNearby("54.635497458", "25.285998856");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

        public void getStopsNearby(String lat, String lng){
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http");
        uri.authority("api-ext.trafi.com");
        uri.path("/stops/nearby");
        uri.appendQueryParameter("lat", lat);
        uri.appendQueryParameter("lng", lng);
        uri.appendQueryParameter("radius", "1000");
        uri.appendQueryParameter("api_key", TRAFI_API_KEY);
        String url = uri.build().toString();
        Log.i(LOG_TAG, "onResponse: " + url);

        StringRequest stopsRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(LOG_TAG, "onResponse: " + response);
                stopsList.addAll(new Gson().fromJson(response, TrafiResponse.class).getStops());
                stopsAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, "Error requesting stops nearby", error);
            }
        });

        NetworkRequestQueue.getInstance(this).addToRequestQueue(stopsRequest);
    }

}
