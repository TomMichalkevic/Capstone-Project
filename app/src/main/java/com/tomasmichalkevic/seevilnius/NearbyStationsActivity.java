package com.tomasmichalkevic.seevilnius;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tomasmichalkevic.seevilnius.data.trafi.Stop;
import com.tomasmichalkevic.seevilnius.data.trafi.TrafiResponse;
import com.tomasmichalkevic.seevilnius.utils.NetworkRequestQueue;

import java.util.ArrayList;
import java.util.Objects;

public class NearbyStationsActivity extends AppCompatActivity {

    private static final String LOG_TAG = NearbyStationsActivity.class.getSimpleName();
    private final ArrayList<Stop> stopsList = new ArrayList<>();
    private static final String TRAFI_API_KEY = BuildConfig.TRAFI_API_KEY;
    private StopsAdapter stopsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_stations);
        RecyclerView recyclerView = findViewById(R.id.stops_recycler_view);
        stopsAdapter = new StopsAdapter(stopsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stopsAdapter);
        recyclerView.setHasFixedSize(true);
        if(getIntent().hasExtra("Lat") && getIntent().hasExtra("Lng")){
            getStopsNearby(getIntent().getDoubleExtra("Lat", 0.0), getIntent().getDoubleExtra("Lng", 0.0));
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

        private void getStopsNearby(double lat, double lng){
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http");
        uri.authority("api-ext.trafi.com");
        uri.path("/stops/nearby");
        uri.appendQueryParameter("lat", String.valueOf(lat));
        uri.appendQueryParameter("lng", String.valueOf(lng));
        uri.appendQueryParameter("radius", "500");
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
