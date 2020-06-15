package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.moviesapp.utilities.MoviesAPIJsonUtils;
import com.example.moviesapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.moviesapp.MovieAdapter.MovieAdapterOnClickHandler;



public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private Switch mSwitchSorting;
    List<Movie> mMoviesList;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_all_movies);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mLoading.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error);

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

        mSwitchSorting = (Switch) findViewById(R.id.switch_sorting);
        loadMoviePosters();
        mSwitchSorting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                loadMoviePosters();
            }
        });

        //Save switch state in shared preferences so that the main activity state (sorting pattern) is restored
        // after returning from DetailActivity
        // BEGINNING
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        mSwitchSorting.setChecked(sharedPreferences.getBoolean("value", true));

        mSwitchSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSwitchSorting.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    mSwitchSorting.setChecked(true);
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    mSwitchSorting.setChecked(false);
                }
            }
        });
        //END
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
        intentToStartDetailActivity.putExtra("backgroundPath", NetworkUtils.getURLBaseAndSizeForBackground()+movie.getBackgroundPath());
        startActivity(intentToStartDetailActivity);
    }
    private void loadMoviePosters() {
        new FetchMoviesTask().execute(mSwitchSorting.isChecked());
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

    public class FetchMoviesTask extends  AsyncTask<Boolean, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();

        }

        @Override
        protected ArrayList<Movie> doInBackground(Boolean... bool) {

            if(bool.length==0) {
                return null;
            }

            Boolean sort_by = bool[0]; // true = sort by rating, false = sort by popularity
            URL movieRequestURL = NetworkUtils.buildUrl(sort_by);
            Log.e("URL", movieRequestURL.toString());
            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);

                return MoviesAPIJsonUtils.getAllMoviesFromJSON(MainActivity.this, jsonMoviesResponse);

            } catch(Exception e ) {

                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mMovieAdapter.setMoviesData(movies);
                showPosters();
            } else {
                showError();
            }
        }


    }
}
