package com.tomasmichalkevic.seevilnius;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.preference.PreferenceManager;
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
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
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
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private final List<Result> places = new ArrayList<>();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private boolean isUserFirstTime;
    private double currentLat = 0;
    private double currentLng = 0;
    private LocationManager locationManager;
    private PlacesCardAdapter placesCardAdapter;
    private Location location;
    private final List<Float> distance = new ArrayList<>();
    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;
    private int radius = 1000;

    @BindView(R.id.places_recycler_view)
    RecyclerView placesRecyclerView;
    @BindView(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
        PreferenceManager.setDefaultValues(this, R.xml.pref_main, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        radius = sharedPref.getInt(SettingsActivity.KEY_PREF_RADIUS, 1) * 1000;


        isUserFirstTime = Boolean.valueOf(SavingUtilities.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));
        Intent introIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime){
            startActivityForResult(introIntent, 1);
        }


        RecyclerView.LayoutManager mLayoutManagerPlaces = new LinearLayoutManager(this);

        placesRecyclerView.setLayoutManager(mLayoutManagerPlaces);

        placesCardAdapter = new PlacesCardAdapter(places, distance, new PlacesCardAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Result view) {
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
    protected void onResume() {
        super.onResume();
        if (!isUserFirstTime){
            getCurrentLocation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
            getPlaces();
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            Toast.makeText(this, getString(R.string.location_reguest_message), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            getCurrentLocation();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
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
        switch (id){
            case R.id.action_settings:{
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_nearby_stops:{
                Intent nearbyStopsIntent = new Intent(MainActivity.this, NearbyStationsActivity.class);
                nearbyStopsIntent.putExtra("Lat", currentLat);
                nearbyStopsIntent.putExtra("Lng", currentLng);
                startActivity(nearbyStopsIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_FINE_LOCATION);
        }else{
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            long UPDATE_INTERVAL = 900000;
            locationRequest.setInterval(UPDATE_INTERVAL);
            long FASTEST_INTERVAL = 300000;
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

    private void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        this.location = location;
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        getPlaces();
    }

    private void getPlaces(){
        final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleInverse);
        progressBar.setVisibility(View.VISIBLE);
        final String location = "" + currentLat +
                "," +
                currentLng;
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");
        uri.authority("maps.googleapis.com");
        uri.path("/maps/api/place/nearbysearch/json");
        uri.appendQueryParameter("location", location);
        uri.appendQueryParameter("radius", String.valueOf(radius));
        uri.appendQueryParameter("type", "museum");
        uri.appendQueryParameter("key", GOOGLE_API_KEY);
        String url = uri.build().toString();
        places.clear();
        distance.clear();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                places.addAll(new Gson().fromJson(response, PlacesAPIResult.class).getResults());
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

}
