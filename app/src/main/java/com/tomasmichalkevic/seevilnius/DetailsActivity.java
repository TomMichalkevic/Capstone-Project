package com.tomasmichalkevic.seevilnius;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tomasmichalkevic.seevilnius.data.Result;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.title_iv) ImageView titleBackdrop;
    @BindView(R.id.visit_fab) FloatingActionButton fab;
    @BindView(R.id.collapsingDetails) CollapsingToolbarLayout detailsLayout;
    @BindView(R.id.detailsCoordinatorLayout) CoordinatorLayout detailsCoordinatorLayout;
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
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

}
