package com.hittareviews.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hittareviews.R;
import com.hittareviews.adapter.ReviewsAdapter;
import com.hittareviews.viewmodel.ReviewsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hittareviews.utils.Constants.RATING_DESCRIPTION;
import static com.hittareviews.utils.Constants.RATING_NAME;
import static com.hittareviews.utils.Constants.RATING_SAVE;
import static com.hittareviews.utils.Constants.RATING_STARS;


public class ReviewsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_BACK_FROM_ADD_REVIEW = 1;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.reviewsRecycler)
    RecyclerView reviewsRecycler;
    @BindView(R.id.rateReviewLayout)
    ConstraintLayout rateReviewLayout;
    @BindView(R.id.anonymReviewLayout)
    ConstraintLayout anonymReviewLayout;

    @BindView(R.id.reviewTitle)
    TextView reviewTitle;
    @BindView(R.id.itemName)
    TextView reviewName;
    @BindView(R.id.itemReview)
    TextView reviewDescription;
    @BindView(R.id.itemExperience)
    TextView reviewExperience;
    @BindView(R.id.itemDate)
    TextView reviewDate;
    @BindView(R.id.starsLayout)
    RatingBar starsLayout;
    @BindView(R.id.moreImg)
    ImageView moreImg;

    private ReviewsViewModel reviewsViewModel;
    private ReviewsAdapter reviewsAdapter;
    private float ratingValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        reviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        setListeners();
        initRecycler();
        loadReviews();
    }

    private void setListeners() {
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            ratingValue = rating;
            reviewsViewModel.setStartTimeReview(System.currentTimeMillis());
            gotoAddReviewActivity(ratingValue);
        });
    }

    private void initRecycler() {
        reviewsAdapter = new ReviewsAdapter(reviewsViewModel.getReviewsList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        reviewsRecycler.setLayoutManager(mLayoutManager);
        reviewsRecycler.setItemAnimator(new DefaultItemAnimator());
        reviewsRecycler.setAdapter(reviewsAdapter);
    }

    private void loadReviews() {
        reviewsViewModel.loadReviews();
        reviewsAdapter.notifyDataSetChanged();
    }

    public void gotoAddReviewActivity(float rating) {
        Intent goToAddReview = new Intent(ReviewsActivity.this, AddReviewActivity.class);
        goToAddReview.putExtra(RATING_STARS, rating);
        String name = reviewName.getText().toString();
        goToAddReview.putExtra(RATING_NAME, !name.equals(getResources().getString(R.string.anonymous)) ? name : "");
        goToAddReview.putExtra(RATING_DESCRIPTION, reviewDescription.getText().toString());
        startActivityForResult(goToAddReview, REQUEST_CODE_BACK_FROM_ADD_REVIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BACK_FROM_ADD_REVIEW && resultCode == Activity.RESULT_OK) {
            rateReviewLayout.setVisibility(View.GONE);
            anonymReviewLayout.setVisibility(View.VISIBLE);
            Bundle extras = data.getExtras();
            if (extras != null) {
                boolean saveInfo = extras.getBoolean(RATING_SAVE);
                String name = extras.getString(RATING_NAME);
                String description = extras.getString(RATING_DESCRIPTION);
                float starsRating = extras.getFloat(RATING_STARS);
                reviewTitle.setVisibility(View.VISIBLE);
                starsLayout.setRating(starsRating);

                if (saveInfo) {
                    showSavedInfoView(name, description);
                } else {
                    showPreviousInfoView();
                }
            } else {
                reviewName.setText(R.string.anonymous);
                showExperienceView();
            }
            setReviewDate();
            reviewExperience.setOnClickListener(v ->
                    gotoAddReviewActivity(starsLayout.getRating()));

        }
    }

    private void showDescription(String description) {
        reviewDescription.setText(description);
        reviewDescription.setVisibility(View.VISIBLE);
        reviewExperience.setVisibility(View.GONE);
    }

    private void showExperienceView() {
        reviewDescription.setVisibility(View.GONE);
        reviewExperience.setVisibility(View.VISIBLE);
        moreImg.setVisibility(View.VISIBLE);
    }

    private void showSavedInfoView(String name, String description) {
        if (!TextUtils.isEmpty(name)) {
            reviewName.setText(name);
        } else {
            reviewName.setText(R.string.anonymous);
        }

        showExperienceView();

        if (!TextUtils.isEmpty(description)) {
            showDescription(description);
        }
    }

    private void showPreviousInfoView() {
        if (TextUtils.isEmpty(reviewName.getText().toString())) {
            reviewName.setText(R.string.anonymous);
        }
        if (TextUtils.isEmpty(reviewDescription.getText().toString())) {
            showExperienceView();
        }
    }

    private void setReviewDate() {
        reviewDate.setText(reviewsViewModel.calculateReviewDate());
    }
}
