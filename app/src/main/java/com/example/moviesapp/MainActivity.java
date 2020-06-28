package com.example.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.background.MovieBackgroundService;
import com.example.moviesapp.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import com.example.moviesapp.MovieAdapter.MovieAdapterOnClickHandler;



public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {
    private int currentSorting=1;
    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private Switch mSwitchSorting;
    ArrayList<Movie> mMoviesList;
    private ProgressBar mLoading;
    private SharedPreferences sharedPreferences;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(mMoviesList == null) {
                showError();
                return;
            }
            mMoviesList = intent.getParcelableArrayListExtra("movies");
            mMovieAdapter.setMoviesData(mMoviesList);
            showPosters();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1)
        //RECYCLER VIEW PART BEGIN
        mMoviesRecyclerView = findViewById(R.id.rv_all_movies);
        mLoading = findViewById(R.id.pb_loading);
        mLoading.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay = findViewById(R.id.tv_error);
        mMoviesList = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(mMoviesList, this);

        //check the current rotation (vertical or horizontal).
        //generate 2 columns for vertical pattern, 3 for horizontal
        int cols = 2;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            cols = mMovieAdapter.getNumberColumnsVertical();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            cols = mMovieAdapter.getNumberColumnsHorizontal();
        }

        GridLayoutManager layoutManager
                = new GridLayoutManager(this,cols);

        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        //RECYCLER VIEW PART END

        //retrieving data from API
        sharedPreferences = getSharedPreferences("sort", MODE_PRIVATE);
        int sort = sharedPreferences.getInt("int_sorting", 0);
        sendNetworkRequest(sort);
        //register listener for updating sharedpreferences BEGIN
//        sharedPreferences = getSharedPreferences("sort", MODE_PRIVATE);
//        SharedPreferences.OnSharedPreferenceChangeListener listner;
//        listner = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
//                Log.i("Retrieving sorting","REEE");
//                sendNetworkRequest();
//            }
//        };
//        sharedPreferences.registerOnSharedPreferenceChangeListener(listner);
        //register listener for updating sharedpreferences END
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("MovieBackgroundService");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.i("Retrieving sorting", ""+getSharedPreferences("sort", MODE_PRIVATE).getInt("int_sorting", 0));
        SharedPreferences.Editor editor;
        switch(item.getItemId()){
            case R.id.sortByPopularity:
                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting",1);
                currentSorting = 1;
                editor.apply();
                Log.i("Selecting sorting","Sorted by popular selected");
                sendNetworkRequest(currentSorting);
                return true;
            case R.id.sortByTopRated:
                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting",2);
                editor.apply();
                currentSorting = 2;
                Log.i("Selecting sorting","Sorted by rated selected");
                sendNetworkRequest(currentSorting);
                return true;
            case R.id.sortByFavourites:
                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting",3);
                editor.apply();
                currentSorting = 3;
                Log.i("Selecting sorting","Sorted by fav selected");
                sendNetworkRequest(currentSorting);
                return true;
            /*case R.id.nightMode:
                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting",4);
                editor.apply();
                currentSorting = 4;
                Log.i("Selecting sorting","night day mode selected");
                return true;
            */
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // COMPLETED (2) Override the onSharedPreferenceChanged method and update the show bass preference
    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(int index) {

        //create new Intent (detailactivity)
        Context context = this;
        Class destinationClass = MovieActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        //send movie data to the new Intent. Also possible to send the object with Parcelable?
        Movie movie = mMovieAdapter.getmMoviesList().get(index);
        intentToStartDetailActivity.putExtra("posterPath",NetworkUtils.getURLBaseAndSizeForPoster()+movie.getPosterPath());
        intentToStartDetailActivity.putExtra("title",movie.getOriginalTitle());
        intentToStartDetailActivity.putExtra("year",movie.getReleaseDate());
        intentToStartDetailActivity.putExtra("rating",movie.getVoteAverage());
        intentToStartDetailActivity.putExtra("overview",movie.getOverview());
        intentToStartDetailActivity.putExtra("backgroundPath", NetworkUtils.getURLBaseAndSizeForBackground()+movie.getBackdropPath());
        startActivity(intentToStartDetailActivity);
    }

    private void showPosters() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.INVISIBLE);
    }

    private void showProgress() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
    }

    private void sendNetworkRequest(int sortOption) {
        showProgress();
        Intent intent = new Intent(MainActivity.this, MovieBackgroundService.class);
        intent.putExtra("sortOption", sortOption);
        Log.i("In sendNetworkRequest", "executing");

        startService(intent);
    }

}
