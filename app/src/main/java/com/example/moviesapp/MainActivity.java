package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
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



public class MainActivity extends AppCompatActivity  {

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ImageView mMovieImage;
    private Switch mSwitchSorting;
    List<Movie> mMoviesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovieImage = (ImageView) findViewById(R.id.movieItem);
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(mMovieImage);

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_all_movies);



        mMoviesList = new ArrayList<>();

        mMovieAdapter = new MovieAdapter(mMoviesList);
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

    }


    private void loadMoviePosters() {
        showPosters();
        new FetchMoviesTask().execute(mSwitchSorting.isChecked());
    }

    private void showPosters() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showMoviesDataView() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    public class FetchMoviesTask extends  AsyncTask<Boolean, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Boolean... bool) {

            if(bool.length==0) {
                return null;
            }

            Boolean sort_by = bool[0]; // true = sort by rating, false = sort by popularity
            Log.v("SORTING IN doInBackg", sort_by.toString());
            URL movieRequestURL = NetworkUtils.buildUrl(sort_by);

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                Log.v("JSON!!!!", jsonMoviesResponse);
                return MoviesAPIJsonUtils.getAllMoviesFromJSON(MainActivity.this, jsonMoviesResponse);

            } catch(Exception e ) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            Log.i("ELEMTNTS IN MOVIESLIST", String.valueOf(movies==null));
            if (movies != null) {
                showMoviesDataView();

                mMovieAdapter.setMoviesData(movies);
                for(Movie m : mMovieAdapter.getmMoviesList()) {
                    Log.v("NOT NULL", m.getOriginalTitle());
                }
            } else {
                for(Movie m :  mMovieAdapter.getmMoviesList()) {
                    Log.v("NULL AS HELL", m.getOriginalTitle());
                }
            }
            Log.i("ELEMTNTS IN MOVIESLIST", String.valueOf(mMoviesList.size()));
        }


    }
}
