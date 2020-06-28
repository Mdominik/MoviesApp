package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {
    private TextView mTitleDisplay;
    private TextView mYearDisplay;
    private RatingBar mRatingDisplay;
    private TextView mRatingTextDisplay;
    private TextView mOverviewDisplay;
    private ImageView mPoster;
    private ImageView mBackdrop;
    boolean rememberSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);
        mTitleDisplay = (TextView) findViewById(R.id.tv_original_title);
        mYearDisplay = (TextView) findViewById(R.id.tv_year);
        //mRatingDisplay = (RatingBar) findViewById(R.id.ratingBar);
        mRatingTextDisplay = (TextView) findViewById(R.id.tv_rating);
        mOverviewDisplay = (TextView) findViewById(R.id.tv_overview);
        mPoster = (ImageView) findViewById(R.id.iv_poster_detail);
        mBackdrop = (ImageView) findViewById(R.id.iv_backdrop);
        Movie movie = null;
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            movie = intentThatStartedThisActivity.getParcelableExtra("movie");

            if (movie != null) {
                String posterURL = NetworkUtils.getURLBaseAndSizeForPoster() + movie.getPosterPath();
                Picasso.get().load(posterURL).into(mPoster);

                String poster = movie.getTitle();
                mTitleDisplay.setText(poster);

                String year = movie.getReleaseDate().substring(0, 4);
                mYearDisplay.setText(year);

                String overview = movie.getOverview().length() == 0 ? "No description in your language, sorry!" : movie.getOverview();
                mOverviewDisplay.setText(overview);

                Double rating = movie.getVoteAverage();
                mRatingTextDisplay.setText(rating + "/10");

                String path = NetworkUtils.getURLBaseAndSizeForBackground() + movie.getBackdropPath();
                Picasso.get().load(path).into(mBackdrop);

                return;
            }
        }
        return;
    }
}
