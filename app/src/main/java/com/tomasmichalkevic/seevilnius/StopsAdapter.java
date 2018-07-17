package com.tomasmichalkevic.seevilnius;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tomasmichalkevic.seevilnius.data.Result;
import com.tomasmichalkevic.seevilnius.data.trafi.Stop;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StopsAdapter extends RecyclerView.Adapter<StopsAdapter.ViewHolder> {

    private ArrayList<Stop> stopsList;

    public StopsAdapter(ArrayList<Stop> stopsList) {
        this.stopsList = stopsList;
    }

    public interface ItemClickListener {
        void onItemClick(Stop view);
    }

    @NonNull
    @Override
    public StopsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopsAdapter.ViewHolder holder, int position) {
        holder.bind(stopsList.get(position));
    }

    @Override
    public int getItemCount() {
        return stopsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.stop_name_tv) TextView stopNameTV;
        @BindView(R.id.stop_distance_value_tv) TextView stopDistanceValueTV;
        @BindView(R.id.in_direction_tv) TextView directionTV;
        @BindView(R.id.stop_icon_iv) ImageView stopIconIV;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Stop stop){
            stopNameTV.setText(stop.getName());
            directionTV.setText(stop.getDirection());
            stopDistanceValueTV.setText(stop.getStopTooltip().getDistanceText());
            Picasso.with(itemView.getContext()).load(stop.getIconUrl()).into(stopIconIV);
        }
    }
}
