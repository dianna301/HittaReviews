package com.hittareviews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hittareviews.R;
import com.hittareviews.RequestData;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hittareviews.utils.Constants.RATING_DESCRIPTION;
import static com.hittareviews.utils.Constants.RATING_NAME;
import static com.hittareviews.utils.Constants.RATING_SAVE;
import static com.hittareviews.utils.Constants.RATING_STARS;

public class AddReviewActivity extends AppCompatActivity {


    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.nameEditText)
    EditText nameEditText;
    @BindView(R.id.reviewEditText)
    EditText reviewEditText;
    @BindView(R.id.statusTv)
    TextView statusTextView;

    private boolean saveInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        ButterKnife.bind(this);
        setActionBarViews();
        setDataFromBundle();

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) ->
                setRatingStatus((int) rating));
    }

    private void setActionBarViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            // set company name
            RequestData.getInstance(this).getData(response -> actionBar.setTitle(response),
                    error -> Log.e(getClass().getName(), error.getMessage(), error));
        }
    }

    private void setDataFromBundle() {
        Bundle extras = getIntent().getExtras();
        float rating = 0;
        String name = "";
        String description = "";
        if (extras != null) {
            rating = extras.getFloat(RATING_STARS);
            name = extras.getString(RATING_NAME);
            description = extras.getString(RATING_DESCRIPTION);
        }
        ratingBar.setRating(rating);
        setRatingStatus((int) rating);
        if (!TextUtils.isEmpty(name)) {
            nameEditText.setText(name);
        }
        if (!TextUtils.isEmpty(description)) {
            reviewEditText.setText(description);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (TextUtils.isEmpty(nameEditText.getText().toString()) && TextUtils.isEmpty(reviewEditText.getText().toString())) {
                    onBackPressed();
                } else {
                    showExitDialog();
                }
                return true;
            case R.id.save_menu_item:
                saveReview();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveReview() {
        RequestData.getInstance(AddReviewActivity.this).saveReview(nameEditText.getText().toString().trim(), reviewEditText.getText().toString(), ratingBar.getRating(),
                response -> showSaveDialog(),
                error -> showErrorDialog());
    }

    private void showSaveDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.thank_you_for_review)
                .setMessage(R.string.you_are_helping_others)
                .setPositiveButton(R.string.okay, (dialog, which) -> {
                    onBackPressed();
                })
                .show();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.save_review)
                .setPositiveButton(R.string.save_string, (dialog, which) -> {
                    saveInfo = true;
                    onBackPressed();
                })
                .setNegativeButton(R.string.do_not_save, (dialog, which) -> {
                    saveInfo = false;
                    onBackPressed();
                })
                .setNeutralButton(R.string.cancel, (dialog, which) -> {
                    // dialog is dismissed
                })
                .show();
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.problem_while_saving)
                .setNeutralButton(R.string.cancel, (dialog, which) -> {
                    // dialog is dismissed
                })
                .setPositiveButton("Try again", (dialog, which) -> saveReview())
                .show();
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(RATING_NAME, nameEditText.getText().toString().trim());
        bundle.putString(RATING_DESCRIPTION, reviewEditText.getText().toString());
        bundle.putFloat(RATING_STARS, ratingBar.getRating());
        bundle.putBoolean(RATING_SAVE, saveInfo);

        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }

    /**
     * set the rating status according to the number of start selected
     *
     * @param rating - number of start selected
     */
    private void setRatingStatus(int rating) {
        int ratingStatusId;
        switch (rating) {
            case 1:
                ratingStatusId = R.string.rating_status_1;
                break;
            case 2:
                ratingStatusId = R.string.rating_status_2;
                break;
            case 3:
                ratingStatusId = R.string.rating_status_3;
                break;
            case 4:
                ratingStatusId = R.string.rating_status_4;
                break;
            case 5:
                ratingStatusId = R.string.rating_status_5;
                break;
            default:
                ratingStatusId = R.string.rating_status_1;
                break;
        }
        statusTextView.setText(getResources().getString(ratingStatusId));

    }
}
