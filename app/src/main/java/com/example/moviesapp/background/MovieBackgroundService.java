package com.example.moviesapp.background;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.moviesapp.MainActivity;
import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.api.model.ResponseFromJSON;
import com.example.moviesapp.api.service.MovieClient;
import com.example.moviesapp.utilities.NetworkUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieBackgroundService extends IntentService{

    //1=sort by popular, 2=sort by top rated, 3=sort by fav
    private int sortOption;

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public MovieBackgroundService() {
        super("Movie Background Service");
        Log.i("Retrofit data", "created1");
    }

//    @Override
//    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        sortOption = intent.getIntExtra("sortOption", 1);
//        return Service.START_STICKY;
//    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sortOption = intent.getIntExtra("sortOption", 1);
        Log.i("Sort option", ""+sortOption);
        //create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(NetworkUtils.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        MovieClient client = retrofit.create(MovieClient.class);
        Call<ResponseFromJSON> call = null;
        switch(sortOption) {
            case 1:
                call = client.getPopularMovie();
                break;
            case 2:
                call = client.getTopRatedMovie();
                break;
            default:
                call = client.getPopularMovie();
        }
        try{
            Response<ResponseFromJSON> result = call.execute();
            Log.i("MOVIES", result.body().getMovies().get(0).getOriginalTitle());
            Log.i("Retrofit data", result.toString());
            Log.i("Retrofit","Success!");

            ArrayList<Movie> movies = (ArrayList<Movie>)result.body().getMovies();
            //send it to MainActivity:

            Intent intentBackToMain = new Intent(MovieBackgroundService.this, MainActivity.class);
            intentBackToMain.putParcelableArrayListExtra("movies", movies); // Be sure con is not null here
            intentBackToMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentBackToMain);
        }catch(IOException ioe) {
            Log.i("Retrofit","Failure!");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
