package com.tomasmichalkevic.seevilnius;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tomasmichalkevic.seevilnius.data.Result;
import com.tomasmichalkevic.seevilnius.data.details.Details;
import com.tomasmichalkevic.seevilnius.data.details.DetailsResult;
import com.tomasmichalkevic.seevilnius.utils.NetworkRequestQueue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.title_iv) ImageView titleBackdrop;
    @BindView(R.id.visit_fab) FloatingActionButton fab;
    @BindView(R.id.collapsingDetails) CollapsingToolbarLayout detailsLayout;
    @BindView(R.id.detailsCoordinatorLayout) CoordinatorLayout detailsCoordinatorLayout;
    @BindView(R.id.ratingValueTV) TextView ratingValueTV;
    @BindView(R.id.telephoneValueTV) TextView telephoneValueTV;
    @BindView(R.id.addressValueTV) TextView addressValueTV;
    private Result result = new Result();
    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent!=null){
            Log.i(LOG_TAG, "onCreate: not null, had extra: " + intent.hasExtra("data"));
            result = (new Gson()).fromJson(intent.getStringExtra("data"), result.getClass());
            Picasso.with(this).load(getPhotoURL(result.getPhotos().get(0).getPhotoReference())).into(titleBackdrop);
            ratingValueTV.setText(Double.toString(result.getRating()));
            Log.i(LOG_TAG, "onCreate: " + result.getOpeningHours().isOpenNow());
            Log.i(LOG_TAG, "onCreate: " + result.getReference());

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getPlaceDetails("ChIJoyC4CRxu5kcRRTPcWX5srLc");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getPhotoURL(String reference){
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");
        uri.authority("maps.googleapis.com");
        uri.path("/maps/api/place/photo");
        uri.appendQueryParameter("maxwidth", "400");
        uri.appendQueryParameter("photoreference", reference);
        uri.appendQueryParameter("key", GOOGLE_API_KEY);
        Log.i("STUFF", "getPhotoURL: " + uri.build().toString());
        return uri.build().toString();
    }

    public void getPlaceDetails(String placeID){
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https");
        uri.authority("maps.googleapis.com");
        uri.path("/maps/api/place/details/json");
        uri.appendQueryParameter("placeid", placeID);
        uri.appendQueryParameter("fields", "name,rating,international_phone_number,formatted_address,reviews,opening_hours");
        uri.appendQueryParameter("key", GOOGLE_API_KEY);
        Log.i("STUFF", "getPhotoURL: " + uri.build().toString());
        String url = uri.build().toString();
        final Details[] details = {new Details()};
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(LOG_TAG, "onResponse: " + response.toString());
                details[0] = new Gson().fromJson(response, Details.class);
                telephoneValueTV.setText(details[0].getResult().getInternationalPhoneNumber());
                addressValueTV.setText(details[0].getResult().getFormattedAddress());
                Log.i(LOG_TAG, "onResponse: " + details[0].getResult().getName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, "Error POIs nearby", error);

            }
        });
        NetworkRequestQueue.getInstance(this).addToRequestQueue(request);
    }

}
