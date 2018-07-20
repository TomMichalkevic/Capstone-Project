package com.tomasmichalkevic.seevilnius;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tomasmichalkevic.seevilnius.data.AppExecutors;
import com.tomasmichalkevic.seevilnius.data.Result;
import com.tomasmichalkevic.seevilnius.data.db.AppDatabase;
import com.tomasmichalkevic.seevilnius.data.db.PlaceEntry;
import com.tomasmichalkevic.seevilnius.data.details.Details;
import com.tomasmichalkevic.seevilnius.data.details.Review;
import com.tomasmichalkevic.seevilnius.utils.NetworkRequestQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.title_iv)
    ImageView titleBackdrop;
    @BindView(R.id.visit_fab)
    FloatingActionButton fab;
    @BindView(R.id.collapsingDetails) CollapsingToolbarLayout detailsLayout;
    @BindView(R.id.detailsCoordinatorLayout) CoordinatorLayout detailsCoordinatorLayout;
    @BindView(R.id.ratingValueTV)
    TextView ratingValueTV;
    @BindView(R.id.telephoneValueTV)
    TextView telephoneValueTV;
    @BindView(R.id.addressValueTV)
    TextView addressValueTV;
    @BindView(R.id.openTimesTV)
    TextView openingTimesTV;
    @BindView(R.id.openTimesValueTV)
    TextView openingTimesValueTV;
    @BindView(R.id.review_recycler_view)
    RecyclerView reviewRecyclerView;
    private Result result = new Result();
    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;
    private ReviewAdapter reviewAdapter;
    private final List<Review> reviewList = new ArrayList<>();
    private Details detailsCopy = new Details();
    private boolean isMarkedVisited;
    private String placeID = "";

    private AppDatabase db;

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_details);
        isMarkedVisited = false;
        db = AppDatabase.getInstance(getApplicationContext());
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent!=null){
            result = (new Gson()).fromJson(intent.getStringExtra("data"), result.getClass());
            if(result.getPhotos()==null){
                titleBackdrop.setImageResource(R.drawable.ic_004_map);
            }else{
                Picasso.with(this).load(getPhotoURL(result.getPhotos().get(0).getPhotoReference())).into(titleBackdrop);
            }
            ratingValueTV.setText(Double.toString(result.getRating()));
            reviewAdapter = new ReviewAdapter(reviewList);
            reviewRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManagerReviews = new LinearLayoutManager(this);
            reviewRecyclerView.setLayoutManager(layoutManagerReviews);
            reviewRecyclerView.setAdapter(reviewAdapter);
            reviewList.clear();
            placeID = result.getPlaceId();
            getPlaceDetails(result.getPlaceId());
            reviewAdapter.notifyDataSetChanged();
            final LiveData<PlaceEntry> place = db.placeDao().loadPlaceByPlaceId(result.getPlaceId());
            place.observe(this, new Observer<PlaceEntry>() {
                @Override
                public void onChanged(@Nullable PlaceEntry placeEntry) {
                    place.removeObserver(this);
                    Log.i(LOG_TAG, "onChanged STUFF: getting places that are present");
                    Log.d(LOG_TAG, "Receiving database update from LiveData");
                    if(placeEntry!=null){
                        Log.i(LOG_TAG, "onChanged: STUFF WELL IT IS PRESENT" + placeEntry.getPlace_id());
                        if(placeEntry.getPlace_id().equals(placeID)){
                            Log.i(LOG_TAG, "onChanged: WELL IT IS PRESENT");
                            isMarkedVisited = true;
                            fab.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                        }else{
                            isMarkedVisited = false;
                        }

                    }
                    //Log.i(LOG_TAG, "onChanged: " + placeEntry.getAddress());
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if(isMarkedVisited){
                        Snackbar.make(view, "Already marked visited!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else{
                        fab.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                        Snackbar.make(view, "Marked visited!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        isMarkedVisited = true;
                        Date date = new Date();
                        final PlaceEntry placeEntry = new PlaceEntry(placeID, detailsCopy.getResult().getFormattedAddress(), String.valueOf(detailsCopy.getResult().getRating()), detailsCopy.getResult().getInternationalPhoneNumber(), new Gson().toJson(detailsCopy.getResult().getOpeningHours()), new Gson().toJson(detailsCopy.getResult().getReviews()), isMarkedVisited, date);
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(LOG_TAG, "run: STUFF: inserting new place");
                                db.placeDao().insertPlace(placeEntry);
                            }
                        });
                    }
                }
            });
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private String getPhotoURL(String reference){
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

    private void getPlaceDetails(String placeID){
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
                Log.i(LOG_TAG, "onResponse: " + response);
                details[0] = new Gson().fromJson(response, Details.class);
                telephoneValueTV.setText(details[0].getResult().getInternationalPhoneNumber());
                addressValueTV.setText(details[0].getResult().getFormattedAddress());
                Log.i(LOG_TAG, "onResponse: " + (details[0]==null));
                if(details[0].getResult().getOpeningHours()!=null){
                    openingTimesValueTV.setText(transformToMultiline(details[0].getResult().getOpeningHours().getWeekdayText()));
                }else {
                    openingTimesTV.setVisibility(View.GONE);
                }
                reviewList.clear();
                if(details[0].getResult().getReviews()!=null){
                    reviewList.addAll(details[0].getResult().getReviews());
                    reviewAdapter.notifyDataSetChanged();
                }
                detailsCopy = details[0];
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

    private String transformToMultiline(List<String> list){
        StringBuilder builder = new StringBuilder("");
        for(String s: list){
            builder.append(s);
            builder.append("\n");
        }
        return builder.toString();
    }

}
