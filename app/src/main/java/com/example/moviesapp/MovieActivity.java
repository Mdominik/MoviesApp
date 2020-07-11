package com.example.moviesapp;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.api.model.Cast;
import com.example.moviesapp.api.model.ExtendedMovie;
import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.api.model.Review;
import com.example.moviesapp.api.model.Video;
import com.example.moviesapp.background.OnClickCastListener;
import com.example.moviesapp.background.OtherDataService;
import com.example.moviesapp.database.FavMovieViewModel;
import com.example.moviesapp.database.FavouriteMovieForDB;
import com.example.moviesapp.utilities.NetworkUtils;
import com.example.moviesapp.utilities.PreferencesUtils;
import com.example.moviesapp.utilities.UtilitySolveScrolling;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity   implements OnClickCastListener, CastAdapter.CastAdapterOnClickHandler{
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
    private CastAdapter mCastAdapter;
    private ExtendedMovie extendedMovie;
    private ArrayList<Review> reviews;
    private ArrayList<Cast> cast;
    private ArrayList<Video> videos;
    private String directorsName;
    private ConstraintLayout mMovieDetail;
    private ToggleButton buttonFavorite;
    private RecyclerView mCastRecyclerView;
    private CardView mCast;
    private List<Cast> mCastList;
    private ListView mListVideos;

    private ListView mListReviews;
    private FavMovieViewModel favMovieViewModel;
    OnClickCastListener onClickCastListener;
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("Movie Activity", "received data from API");

            extendedMovie = intent.getParcelableExtra("extendedMovie");
            cast = intent.getParcelableArrayListExtra("cast");
            reviews = intent.getParcelableArrayListExtra("reviews");
            videos = intent.getParcelableArrayListExtra("videos");
            directorsName = intent.getStringExtra("director");

            Log.i("List reviews created?", ""+(reviews == null));
            String posterURL = NetworkUtils.getURLBaseAndSizeForPoster() + extendedMovie.getPosterPath();
            Picasso.get().load(posterURL).into(mPoster);

            String poster = extendedMovie.getTitle();
            mTitleDisplay.setText(poster);

            String year = extendedMovie.getReleaseDate().substring(0, 4);
            mYearDisplay.setText(year);

            SharedPreferences sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
            String lan_name = sharedPreferences.getString("language_name", "English");

            String overview = extendedMovie.getOverview().length() == 0 ? "No description in " + lan_name + ", sorry!" : extendedMovie.getOverview();
            mOverviewDisplay.setText(overview);

            Double rating = extendedMovie.getVoteAverage();
            mRatingTextDisplay.setText(rating + "/10");

            mLength.setText(extendedMovie.getRuntime() == 0 ? "unknown" : extendedMovie.getRuntime()+"min");

            mDirector.setText(directorsName);

            mBudget.setText(extendedMovie.getBudget() == 0 ? "unknown" : extendedMovie.getBudget()+"$");
            String path = NetworkUtils.getURLBaseAndSizeForBackground() + extendedMovie.getBackdropPath();
            Picasso.get().load(path).into(mBackdrop);


            //Set Cast rv, adapter
            Log.i("CastAdapter got data", "Cast amount: " + cast.size());
            mCastAdapter = new CastAdapter(MovieActivity.this);
            mCastAdapter.setCasts(cast);

            mCastRecyclerView = findViewById(R.id.rv_cast);
            mCast = findViewById(R.id.cv_cast);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(MovieActivity.this, RecyclerView.HORIZONTAL, false);

            mCastRecyclerView.setLayoutManager(layoutManager);
            mCastRecyclerView.setHasFixedSize(true);
            mCastRecyclerView.setAdapter(mCastAdapter);


            //show videos
            mListVideos = findViewById(R.id.lv_videos);
            Log.i("List Videos created?", ""+(videos.size()));
            videoAdapter = new VideoAdapter(videos);
            mListVideos.setAdapter(videoAdapter);

            //copy pasted from internet to make listview scrolling inside scrollview work
            UtilitySolveScrolling.setListViewHeightBasedOnChildren(mListVideos);


            Log.i("VIDEO COUNT ADAPTER", ""+videoAdapter.getCount());
            mListVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i("YT listener", "yes");
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videos.get(i).getKey()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + videos.get(i).getKey()));
                    try {
                        startActivity(appIntent);
                        Log.i("YT video try", "yes");
                    } catch (ActivityNotFoundException ex) {

                        startActivity(webIntent);
                        Log.i("YT video webintent", "yes");
                    }
                }
            });

            //show reviews
            mListReviews = findViewById(R.id.lv_reviews);
            Log.i("List reviews created?", ""+(reviews.size()));
            reviewAdapter = new ReviewAdapter(reviews);
            mListReviews.setAdapter(reviewAdapter);

            //copy pasted from internet to make listview scrolling inside scrollview work
            UtilitySolveScrolling.setListViewHeightBasedOnChildren(mListReviews);


            Log.i("Review COUNT ADAPTER", ""+reviewAdapter.getCount());



            showMovieDetails();
        }
    };

    class VideoAdapter extends ArrayAdapter<Video> {

        Context context;
        ArrayList<Video> movieVideos;

        public VideoAdapter(ArrayList<Video> movieVideos) {
            super(MovieActivity.this, R.layout.row_video, movieVideos);
            this.movieVideos = movieVideos;
            Log.i("Log from VideoAdapter", ""+movieVideos.size());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_video, parent, false);
            TextView mVideoTitle = row.findViewById(R.id.tv_videoTitle);
            mVideoTitle.setText(movieVideos.get(position).getName());
            Log.i("Position"+position, movieVideos.get(position).getName());
            return row;
        }
    }

    class ReviewAdapter extends ArrayAdapter<Review> {

        Context context;
        ArrayList<Review> mReviews;

        public ReviewAdapter(ArrayList<Review> reviews) {
            super(MovieActivity.this, R.layout.row_review, reviews);
            this.mReviews = reviews;
            Log.i("Log from ReviewAdapter", ""+reviews.size());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_review, parent, false);

            //content
            TextView mReviewContent = row.findViewById(R.id.tv_reviewContent);
            mReviewContent.setText(mReviews.get(position).getContent());
            Log.i("Position"+position, mReviews.get(position).getContent());

            //author
            TextView mReviewAuthor = row.findViewById(R.id.tv_reviewAuthor);
            mReviewAuthor.setText(mReviews.get(position).getAuthor());
            Log.i("Position"+position, mReviews.get(position).getAuthor());

            //reviewID
            TextView mReviewID = row.findViewById(R.id.tv_reviewID);
            mReviewID.setText("Review #" + (position+1));
            Log.i("Postition"+position, "Review #" + (position+1));
            return row;
        }
    }

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
        favMovieViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavMovieViewModel.class);
        Log.i("favMovieViewModel", favMovieViewModel+"");
        buttonFavorite = findViewById(R.id.button_favorite);
        //fav button
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //animation
                compoundButton.startAnimation(scaleAnimation);
                if(isChecked) {
                    favMovieViewModel.insert(new FavouriteMovieForDB(extendedMovie.getId(), extendedMovie.getPosterPath()));
                    Log.i("DB", extendedMovie.getId() + " added");
                } else {
                    favMovieViewModel.deleteMovieByID(extendedMovie.getId());
                    Log.i("DB", extendedMovie.getId() + " deleted");
                }

            }
        });

        //show progressbar and wait for data from API
        showProgressBar();

        //Retrieve ID from the first GET request and make another 3 requests by this ID
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

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        // Save the current movie
//        savedInstanceState.putParcelable(PreferencesUtils.STATE_EXTENDED_MOVIES, extendedMovie);
//        savedInstanceState.putParcelableArrayList(PreferencesUtils.STATE_REVIEWS, reviews);
//        savedInstanceState.putParcelableArrayList(PreferencesUtils.STATE_CAST, cast);
//        savedInstanceState.putParcelableArrayList(PreferencesUtils.STATE_VIDEOS, videos);
//        savedInstanceState.putString(PreferencesUtils.STATE_DIRECTOR, directorsName);
//
//        // Always call the superclass so it can save the view hierarchy state
//        super.onSaveInstanceState(savedInstanceState);
//    }

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

        Log.i("onStart", cast+"");

        IntentFilter intentFilter = new IntentFilter("OtherDataService");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(int index) {

    }
}
