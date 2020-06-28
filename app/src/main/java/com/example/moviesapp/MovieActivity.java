package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

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
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        ToggleButton buttonFavorite = findViewById(R.id.button_favorite);
        Log.i("btn", "" + buttonFavorite.isChecked());
        buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //animation
                compoundButton.startAnimation(scaleAnimation);

            }
        });
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
