package com.tomasmichalkevic.seevilnius;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tomasmichalkevic.seevilnius.data.Result;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesCardAdapter extends RecyclerView.Adapter<PlacesCardAdapter.ViewHolder>{

    private final List<Result> placesList;
    private final List<Float> distance;
    private final ItemClickListener listener;
    private static final String GOOGLE_API_KEY = BuildConfig.GOOGLE_API_KEY;

    public PlacesCardAdapter(List<Result> placesList, List<Float> distance, ItemClickListener listener) {
        this.placesList = placesList;
        this.distance = distance;
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(Result view);
    }

    @NonNull
    @Override
    public PlacesCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesCardAdapter.ViewHolder holder, int position) {
        holder.bind(placesList.get(position), distance.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_place) TextView placeTitle;
        @BindView(R.id.tv_distance) TextView distanceTV;
        @BindView(R.id.iv_place) ImageView placeIV;
        @BindView(R.id.place_card_view) CardView place_card_view;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Result place, Float distance, final ItemClickListener listener){
            placeTitle.setText(place.getName());
            distanceTV.setText(MessageFormat.format("Distance: {0}m", Math.round(distance)));
            if(place.getPhotos()!=null){
                if(place.getPhotos().size()>0){
                    if(place.getPhotos().get(0)!=null){
                        Picasso.with(itemView.getContext()).load(getPhotoURL(place.getPhotos().get(0).getPhotoReference())).into(placeIV);
                    }else{
                        Picasso.with(itemView.getContext()).load(place.getIcon()).into(placeIV);
                    }
                }else{
                    Picasso.with(itemView.getContext()).load(place.getIcon()).into(placeIV);
                }
            }else {
                Picasso.with(itemView.getContext()).load(place.getIcon()).into(placeIV);
            }
            place_card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(place);
                }
            });

        }

        String getPhotoURL(String reference){
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
}
