package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;

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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);
        mTitleDisplay = (TextView) findViewById(R.id.tv_original_title);
        mYearDisplay = (TextView) findViewById(R.id.tv_year);
        //mRatingDisplay = (RatingBar) findViewById(R.id.ratingBar);
        mRatingTextDisplay = (TextView) findViewById(R.id.tv_rating);
        mOverviewDisplay = (TextView) findViewById(R.id.tv_overview);
        mPoster = (ImageView) findViewById(R.id.iv_poster_detail);
        mBackdrop = (ImageView) findViewById(R.id.iv_backdrop);
        Intent intentThatStartedThisActivity = getIntent();

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("posterPath")) {
                String poster= intentThatStartedThisActivity.getStringExtra("posterPath");
                Picasso.get().load(poster).into(mPoster);
            }
            if (intentThatStartedThisActivity.hasExtra("title")) {
                String poster= intentThatStartedThisActivity.getStringExtra("title");
                mTitleDisplay.setText(poster);
            }
            if (intentThatStartedThisActivity.hasExtra("year")) {
                String year= intentThatStartedThisActivity.getStringExtra("year");
                mYearDisplay.setText(year);
            }
            if (intentThatStartedThisActivity.hasExtra("overview")) {
                String overview= intentThatStartedThisActivity.getStringExtra("overview");
                mOverviewDisplay.setText(overview);
            }
            if (intentThatStartedThisActivity.hasExtra("rating")) {
                Double rating= intentThatStartedThisActivity.getDoubleExtra("rating", 0.0);
                mRatingTextDisplay.setText(String.valueOf(rating)+"/10");
                //mRatingDisplay.setRating(Float.parseFloat(rating));
            }
            if (intentThatStartedThisActivity.hasExtra("backgroundPath")) {
                String path = intentThatStartedThisActivity.getStringExtra("backgroundPath");
                Picasso.get().load(path).into(mBackdrop);
            }
        }
    }

}
