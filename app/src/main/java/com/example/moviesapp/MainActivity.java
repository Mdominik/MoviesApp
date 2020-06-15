package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.moviesapp.utilities.MoviesAPIJsonUtils;
import com.example.moviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.moviesapp.MovieAdapter.MovieAdapterOnClickHandler;



public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ImageView mMovieImage;
    private Switch mSwitchSorting;
    List<Movie> mMoviesList;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovieImage = (ImageView) findViewById(R.id.movieItem);
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(mMovieImage);

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_all_movies);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mLoading.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error);

        mMoviesList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(mMoviesList, this);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, mMovieAdapter.numberOfColumns);

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

        //Save switch state in shared preferences BEGINNING
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
        //Save switch state in shared preferences END
    }

    @Override
    public void onClick(int index) {
        Context context = this;
        Class destinationClass = MovieActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        // COMPLETED (1) Pass the weather to the DetailActivity
        Movie movie = mMovieAdapter.getmMoviesList().get(index);
        intentToStartDetailActivity.putExtra("posterPath",NetworkUtils.getURLBaseAndSizeForPoster()+movie.getPosterPath());
        intentToStartDetailActivity.putExtra("title",movie.getOriginalTitle());
        intentToStartDetailActivity.putExtra("year",movie.getReleaseDate());
        intentToStartDetailActivity.putExtra("rating",movie.getVoteAverage());
        intentToStartDetailActivity.putExtra("overview",movie.getOverview());

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
                for(Movie m : mMovieAdapter.getmMoviesList()) {
                }
            } else {
                showError();
                for(Movie m :  mMovieAdapter.getmMoviesList()) {
                }
            }
        }


    }
}
