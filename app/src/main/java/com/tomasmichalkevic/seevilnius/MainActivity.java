package com.tomasmichalkevic.seevilnius;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tomasmichalkevic.seevilnius.utils.NetworkRequestQueue;
import com.tomasmichalkevic.seevilnius.utils.SavingUtilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ArrayList<String> places = new ArrayList<>();
    //private static String url = "http://api-ext.trafi.com";
    boolean isUserFirstTime;

    @BindView(R.id.places_recycler_view) RecyclerView placesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isUserFirstTime = Boolean.valueOf(SavingUtilities.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime)
            startActivity(introIntent);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PlacesCardAdapter placesCardAdapter = new PlacesCardAdapter(places);

        RecyclerView.LayoutManager mLayoutManagerPlaces = new LinearLayoutManager(this);

        placesRecyclerView.setLayoutManager(mLayoutManagerPlaces);

        placesRecyclerView.setAdapter(placesCardAdapter);

        places.clear();

        //places.addAll(recipe.getSteps());
        places.add("Katedra");
        places.add("Rotuse");
        places.add("Uzupys");

        placesCardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

//    public void getStopsNearby(String lat, String lng){
//        Uri.Builder uri = new Uri.Builder();
//        uri.scheme("http");
//        uri.authority("api-ext.trafi.com");
//        uri.path("/stops/nearby");
//        uri.appendQueryParameter("lat", lat);
//        uri.appendQueryParameter("lng", lng);
//        uri.appendQueryParameter("api_key", "bee961f6d1112b8ffbdbd0804ee295ef");
//        String url = uri.build().toString();
//        StringRequest stopsRequest = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i(LOG_TAG, "onResponse: " + response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(LOG_TAG, "Error requesting stops nearby", error);
//            }
//        });
//
//        NetworkRequestQueue.getInstance(this).addToRequestQueue(stopsRequest);
//    }

}
