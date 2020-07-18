package com.example.moviesapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.background.MovieBackgroundService;
import com.example.moviesapp.background.OnClickPosterListener;
import com.example.moviesapp.background.OtherDataService;
import com.example.moviesapp.database.FavMovieViewModel;
import com.example.moviesapp.database.FavouriteMovieForDB;
import com.example.moviesapp.utilities.CSVReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.moviesapp.MovieAdapter.MovieAdapterOnClickHandler;
import com.example.moviesapp.utilities.PreferencesUtils;


public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, OnClickPosterListener{
    private int currentSorting = 1; // current sorting criteria
    private int cols = 2; //number of columns of posters on the screen
    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ImageView mItemView;
    private Switch mSwitchSorting;
    ArrayList<Movie> mMoviesList;
    private ProgressBar mLoading;
    private SharedPreferences sharedPreferences;
    ToggleButton fav;
    OnClickPosterListener onClickPosterListener;
    private FavMovieViewModel favMovieViewModel;
    private List<FavouriteMovieForDB> mFavouriteMovies;
    private boolean bool_buttonToSend;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra("sorting", 1) == 4) {
                mMovieAdapter.setMoviesData(mFavouriteMovies);
                MainActivity.this.setTitle("Favourites");
                showPosters();
                return;
            }
            if(mMoviesList == null) {
                showError();
                return;
            }

            switch (intent.getIntExtra("sorting", 1)) {
                case 1:
                    MainActivity.this.setTitle("Popularity");
                    break;
                case 2:
                    MainActivity.this.setTitle("Top rated");
                    break;
                case 3:
                    MainActivity.this.setTitle("Upcoming");
                    break;
            }
            mMoviesList = intent.getParcelableArrayListExtra("movies");
            mMovieAdapter.setMoviesData(mMoviesList);
            showPosters();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //languages csv parsing
        InputStream inputStream = getResources().openRawResource(R.raw.languages);
        PreferencesUtils.setLanguages(CSVReader.getLanguagesForJSON(inputStream));

        //0) temporary for languages csv
        //1)
        //RECYCLER VIEW PART BEGIN
        mMoviesRecyclerView = findViewById(R.id.rv_all_movies);
        mLoading = findViewById(R.id.pb_loading);
        mLoading.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay = findViewById(R.id.tv_error);
        mMoviesList = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(mMoviesList, this, onClickPosterListener);
        mItemView = findViewById(R.id.movieItem);
        //check the current rotation (vertical or horizontal).
        //generate 2 columns for vertical pattern, 3 for horizontal
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            cols = mMovieAdapter.getNumberColumnsVertical();

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            cols = mMovieAdapter.getNumberColumnsHorizontal();

            //Fixing no margin bug in horizontal view
//            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(10,0,10,10);
//            mItemView.setLayoutParams(lp);
        }

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, cols);

        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
        //RECYCLER VIEW PART END


        favMovieViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FavMovieViewModel.class);
        favMovieViewModel.getAllFavMovies().observe(this, new Observer<List<FavouriteMovieForDB>>() {
            @Override
            public void onChanged(List<FavouriteMovieForDB> favouriteMoviesForDB) {
                mFavouriteMovies = favouriteMoviesForDB;
            }
        });

        Log.i("favMieedelFromMainAC" , favMovieViewModel+"");

        //retrieving data from API
        sharedPreferences = getSharedPreferences("sort", MODE_PRIVATE);

        //current sorting criteria
        int sort = sharedPreferences.getInt("int_sorting", 1);

        //first request
        sendNetworkRequest(sort);


    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("MovieBackgroundService");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, intentFilter);
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

        SharedPreferences.Editor editor;
        switch (item.getItemId()) {
            case R.id.sortByPopularity:

                this.setTitle("Popularity");
                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting", 1);
                currentSorting = 1;
                editor.apply();
                Log.i("Selecting sorting", "Sorted by popular selected");
                sendNetworkRequest(currentSorting);
                return true;
            case R.id.sortByTopRated:

                this.setTitle("Top rated");

                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting", 2);
                editor.apply();
                currentSorting = 2;
                Log.i("Selecting sorting", "Sorted by rated selected");
                sendNetworkRequest(currentSorting);
                return true;
            case R.id.sortByUpcoming:
                this.setTitle("Upcoming");
                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting", 3);
                editor.apply();
                currentSorting = 3;
                Log.i("Selecting sorting", "Sorted by upcoming selected");
                sendNetworkRequest(currentSorting);
                return true;
            case R.id.sortByFavourites:
                this.setTitle("Favourites");
                mMovieAdapter.setMoviesData(mFavouriteMovies);
                editor = getSharedPreferences("sort", MODE_PRIVATE).edit();
                editor.putInt("int_sorting", 4);
                currentSorting = 4;
                editor.apply();
                sendNetworkRequest(currentSorting);
                return true;
            case R.id.menu_pref:
                Intent intent = new Intent(this, LanguagePrefActivity.class);
                startActivity(intent);
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
        Movie movie = null;
        FavouriteMovieForDB fav_movie = null;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        //send movie data to the new Intent. Also possible to send the object with Parcelable?
        if(mMovieAdapter.getmMoviesList().get(index) instanceof Movie) {
            movie = (Movie) mMovieAdapter.getmMoviesList().get(index);
        }
        else if(mMovieAdapter.getmMoviesList().get(index) instanceof FavouriteMovieForDB) {
            fav_movie = (FavouriteMovieForDB) mMovieAdapter.getmMoviesList().get(index);
            movie = new Movie(fav_movie.getPosterPath(), fav_movie.getIdFromAPi());
        }


        //Movie object is parcelable
        intentToStartDetailActivity.putExtra("movie", movie);
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


    //first HTTP request for retrieving poster and ID
    private void sendNetworkRequest(int sorting_criteria) {
        showProgress();
        Intent intent_movie = new Intent(MainActivity.this, MovieBackgroundService.class);
        intent_movie.putExtra("sortOption", sorting_criteria);

        startService(intent_movie);
        Log.i("In sendNetworkRequest", "executing");
    }

    @Override
    public void onClick() {
        startService(new Intent(this, OtherDataService.class));
    }
}
