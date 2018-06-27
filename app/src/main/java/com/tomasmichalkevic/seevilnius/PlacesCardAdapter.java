package com.tomasmichalkevic.seevilnius;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesCardAdapter extends RecyclerView.Adapter<PlacesCardAdapter.ViewHolder>{

    private final List<String> placesList;

    public PlacesCardAdapter(List<String> placesList) {
        this.placesList = placesList;
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
        holder.bind(placesList.get(position));
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_place) TextView placeTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final String place){
            placeTitle.setText(place);
        }
    }
}
