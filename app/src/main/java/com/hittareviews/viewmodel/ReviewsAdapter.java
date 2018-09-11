package com.hittareviews.viewmodel;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hittareviews.R;
import com.hittareviews.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviewsList;

    public ReviewsAdapter(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewsList.get(position);
        if (review != null) {
            holder.name.setText(review.getName());
            holder.reviewDesc.setText(review.getDescription());
            holder.reviewDate.setText(review.getDate());
            holder.ratingBar.setRating(review.getRating());
            // hide the bottom line for the last item from list
            if (position == reviewsList.size() - 1) {
                holder.lineBottom.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemName)
        TextView name;
        @BindView(R.id.itemReview)
        TextView reviewDesc;
        @BindView(R.id.itemDate)
        TextView reviewDate;
        @BindView(R.id.itemImg)
        ImageView image;
        @BindView(R.id.starsLayout)
        RatingBar ratingBar;
        @BindView(R.id.lineViewBottom)
        ImageView lineBottom;


        ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
