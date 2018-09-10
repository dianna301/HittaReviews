package com.hittareviews.viewmodel

import android.arch.lifecycle.ViewModel
import com.hittareviews.model.Review
import com.hittareviews.utils.Constants.HOURS_MIN_AGO
import com.hittareviews.utils.Constants.MIN_AGO
import java.util.*
import java.util.concurrent.TimeUnit

class ReviewsViewModel : ViewModel() {

    private val reviewsList = ArrayList<Review>()
    private var startTimeReview: Long = 0

    fun getReviewsList(): List<Review> {
        return reviewsList
    }

    fun loadReviews() {
        reviewsList.add(Review("Anonym", "Liked it very much - probably one of the best thai restaurants in the city - recommend!", "12h ago - hitta.se", 4))
        reviewsList.add(Review("Jenny Svensson", "Maybe a bit too fast food. I personally dislike that. Good otherwise.", "1d ago - hitta.se", 3))
        reviewsList.add(Review("happy56", "Super good! Love the food!", "1d ago - yelp.com", 5))
    }

    fun calculateReviewDate(): String {
        val millis = System.currentTimeMillis() - startTimeReview
        val timePassed: String
        timePassed = when {
            TimeUnit.MILLISECONDS.toHours(millis) > 0 ->
                String.format(Locale.getDefault(), HOURS_MIN_AGO, TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.SECONDS.toMinutes(TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))))
            TimeUnit.MILLISECONDS.toMinutes(millis) > 0 -> String.format(Locale.getDefault(), MIN_AGO, TimeUnit.MILLISECONDS.toMinutes(millis))
            else -> String.format(Locale.getDefault(), MIN_AGO, 1)
        }

        return timePassed
    }

    fun setStartTimeReview(startTimeReview: Long) {
        this.startTimeReview = startTimeReview
    }
}
