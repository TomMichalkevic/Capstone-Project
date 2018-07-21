package com.tomasmichalkevic.seevilnius;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomasmichalkevic.seevilnius.data.details.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private final List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList){
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.author.setText(review.getAuthorName());
        holder.content.setText(review.getText());
        holder.posted.setText(holder.posted.getContext().getString(R.string.posted) + review.getRelativeTimeDescription());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.authorTV) TextView author;
        @BindView(R.id.reviewContentTV) TextView content;
        @BindView(R.id.postedValueTV) TextView posted;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}