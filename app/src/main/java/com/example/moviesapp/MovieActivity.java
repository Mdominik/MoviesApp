package com.example.moviesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.moviesapp.api.model.Cast;
import com.example.moviesapp.api.model.Crew;
import com.example.moviesapp.api.model.ExtendedMovie;
import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.api.model.Review;
import com.example.moviesapp.api.model.Video;
import com.example.moviesapp.background.MovieBackgroundService;
import com.example.moviesapp.background.OtherDataService;
import com.example.moviesapp.utilities.Config;
import com.example.moviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {
    private TextView mTitleDisplay;
    private TextView mYearDisplay;
    private RatingBar mRatingDisplay;
    private TextView mRatingTextDisplay;
    private TextView mDirectorDisplay;
    private TextView mOverviewDisplay;
    private ImageView mPoster;
    private ImageView mBackdrop;
    private TextView mLength;
    private TextView mDirector;
    private TextView mBudget;
    boolean rememberSwitch;

    private ExtendedMovie extendedMovie;
    private ArrayList<Review> reviews;
    private ArrayList<Cast> cast;
    private ArrayList<Video> videos;
    private String directorsName;
    private ConstraintLayout mMovieDetail;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            extendedMovie = intent.getParcelableExtra("extendedMovie");
            cast = intent.getParcelableArrayListExtra("cast");
            Log.i("cast", cast.get(0).getName());
            reviews = intent.getParcelableArrayListExtra("reviews");
            videos = intent.getParcelableArrayListExtra("videos");
            directorsName = intent.getStringExtra("director");

            String posterURL = NetworkUtils.getURLBaseAndSizeForPoster() + extendedMovie.getPosterPath();
            Picasso.get().load(posterURL).into(mPoster);

            String poster = extendedMovie.getTitle();
            mTitleDisplay.setText(poster);

            String year = extendedMovie.getReleaseDate().substring(0, 4);
            mYearDisplay.setText(year);

            String overview = extendedMovie.getOverview().length() == 0 ? "No description in " + Config.getLanguage() + ", sorry!" : extendedMovie.getOverview();
            mOverviewDisplay.setText(overview);

            Double rating = extendedMovie.getVoteAverage();
            mRatingTextDisplay.setText(rating + "/10");

            Integer length = extendedMovie.getRuntime();
            mLength.setText(extendedMovie.getRuntime() == 0 ? "unknown" : extendedMovie.getRuntime()+"min");

            mDirector.setText(directorsName);

            mBudget.setText(extendedMovie.getBudget() == 0 ? "unknown" : extendedMovie.getBudget()+"$");
            String path = NetworkUtils.getURLBaseAndSizeForBackground() + extendedMovie.getBackdropPath();
            Picasso.get().load(path).into(mBackdrop);

            showMovieDetails();
            Log.i("Movie Activity", "RECEIVED DATA IN BroadcastReceiver");
        }
    };

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
        mDirectorDisplay = findViewById(R.id.tv_director);
        mLength = findViewById(R.id.tv_length);
        mDirector = findViewById(R.id.tv_director);
        mBudget = findViewById(R.id.tv_budget);
        Movie movie = null;
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        ToggleButton buttonFavorite = findViewById(R.id.button_favorite);

        //show progressbar and wait for data from API
        showProgressBar();

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
                //send a second request for all remaining data (reviews, cast, videos, extendedmovie)
                sendNetworkRequestForRemainingData(movie.getId());
                return;
            }
        }
        return;
    }

    public void showProgressBar() {
        ConstraintLayout constraintLayout = findViewById(R.id.cl_movieDetails);
        final int childCount = constraintLayout.getChildCount();
        for(int i=0; i<childCount;i++) {
            View v = constraintLayout.getChildAt(i);

            if (v instanceof ProgressBar) {
                v.setVisibility(View.VISIBLE);
            }
            else {
               v.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void showMovieDetails() {
        ConstraintLayout constraintLayout = findViewById(R.id.cl_movieDetails);
        final int childCount = constraintLayout.getChildCount();
        for(int i=0; i<childCount;i++) {
            View v = constraintLayout.getChildAt(i);

            if (v instanceof ProgressBar) {
                v.setVisibility(View.INVISIBLE);
            }
            else {
                v.setVisibility(View.VISIBLE);
            }
        }

    }

    private void sendNetworkRequestForRemainingData(int movie_id) {
        Intent intentRemainingData = new Intent(MovieActivity.this, OtherDataService.class);
        intentRemainingData.putExtra("movie_id", movie_id);
        Log.i("In snedNetworkforRemai1", "Executing");
        startService(intentRemainingData);

        Log.i("In snedNetworkforRemai2", "Executing");
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("OtherDataService");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
    }
}
