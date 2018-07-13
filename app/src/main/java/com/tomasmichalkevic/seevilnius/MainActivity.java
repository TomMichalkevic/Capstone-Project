package com.tomasmichalkevic.seevilnius;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;
import com.tomasmichalkevic.seevilnius.data.PlacesAPIResult;
import com.tomasmichalkevic.seevilnius.data.Result;
import com.tomasmichalkevic.seevilnius.utils.NetworkRequestQueue;
import com.tomasmichalkevic.seevilnius.utils.SavingUtilities;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

//Influenced by code from https://guides.codepath.com/android/Retrieving-Location-with-LocationServices-API

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback{

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<Result> places = new ArrayList<>();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    boolean isUserFirstTime;
    private double currentLat = 0;
    private double currentLng = 0;
    private LocationManager locationManager;
    private PlacesCardAdapter placesCardAdapter;
    private Location location;
    private List<Float> distance = new ArrayList<>();
    private LocationRequest locationRequest;
    private long UPDATE_INTERVAL = 900000;
    private long FASTEST_INTERVAL = 300000;
    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;

    @BindView(R.id.places_recycler_view)
    RecyclerView placesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isUserFirstTime = Boolean.valueOf(SavingUtilities.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));
        Intent introIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime){
            startActivityForResult(introIntent, 1);
        }
        ButterKnife.bind(this);

        RecyclerView.LayoutManager mLayoutManagerPlaces = new LinearLayoutManager(this);

        placesRecyclerView.setLayoutManager(mLayoutManagerPlaces);

        placesCardAdapter = new PlacesCardAdapter(places, distance, new PlacesCardAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Result view) {
                Toast.makeText(MainActivity.this, "Clicked on card! " + view.getName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(view));
                startActivity(intent);
            }
        });

        placesRecyclerView.setAdapter(placesCardAdapter);

        places.clear();
        distance.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isUserFirstTime){
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            getPlaces();
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            Toast.makeText(this, "You have to grant location permission. Restart the app and try again", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            getCurrentLocation();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
            } else {
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }else{
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(UPDATE_INTERVAL);
            locationRequest.setFastestInterval(FASTEST_INTERVAL);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();
            SettingsClient settingsClient = LocationServices.getSettingsClient(this);
            settingsClient.checkLocationSettings(locationSettingsRequest);
            getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
        }
    }

    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        this.location = location;
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        getPlaces();
    }

    public void getPlaces(){
        final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleInverse);
        progressBar.setVisibility(View.VISIBLE);
        StringBuilder builder = new StringBuilder("");
        builder.append(currentLat);
        builder.append(",");
        builder.append(currentLng);
        final String location = builder.toString();
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");
        uri.authority("maps.googleapis.com");
        uri.path("/maps/api/place/nearbysearch/json");
        uri.appendQueryParameter("location", location);
        uri.appendQueryParameter("radius", "5000");
        uri.appendQueryParameter("type", "museum");
        uri.appendQueryParameter("key", GOOGLE_API_KEY);
        String url = uri.build().toString();
        places.clear();
        distance.clear();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                places.addAll(new Gson().fromJson(response, new PlacesAPIResult().getClass()).getResults());
                updateDistances(places);
                placesCardAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, "Error POIs nearby", error);
                progressBar.setVisibility(View.GONE);
            }
        });

        NetworkRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    private void updateDistances(List<Result> places) {
        int i = 0;
        for(Result place: places){
            float[] dist = new float[1];
            //Log.i(LOG_TAG, "updateDistances: " + (place.getPhotos()!=null));
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), place.getGeometry().getLocation().getLat(), place.getGeometry().getLocation().getLng(), dist);
            distance.add(dist[0]);
            i++;
        }
    }

//    public void getStopsNearby(String lat, String lng){
//        Uri.Builder uri = new Uri.Builder();
//        uri.scheme("http");
//        uri.authority("api-ext.trafi.com");
//        uri.path("/stops/nearby");
//        uri.appendQueryParameter("lat", lat);
//        uri.appendQueryParameter("lng", lng);
//        uri.appendQueryParameter("api_key", "DUMMY");
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
