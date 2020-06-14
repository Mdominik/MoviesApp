package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ImageView mMovieImage;
    List<Movie> mMoviesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovieImage = (ImageView) findViewById(R.id.movieItem);
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(mMovieImage);

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_all_movies);


        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);

        mMoviesRecyclerView.setLayoutManager(layoutManager);

        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(mMoviesList);

        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        loadMoviePosters();
    }

    private void loadMoviePosters() {
        showPosters();
        //new FetchMoviesTask().execute(location);
    }

    private void showPosters() {
        /* First, make sure the error is invisible */

        // COMPLETED (44) Show mRecyclerView, not mWeatherTextView
        /* Then, make sure the weather data is visible */
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

//    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {
//
//        @Override
//        protected String[] doInBackground(String... strings) {
//            return new String[0];
//        }
//    }
}