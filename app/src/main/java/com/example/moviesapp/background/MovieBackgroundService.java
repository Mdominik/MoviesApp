package com.example.moviesapp.background;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.api.service.MovieClient;
import com.example.moviesapp.utilities.NetworkUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieBackgroundService extends IntentService {
    public MovieBackgroundService() {
        super("Movie Background Service");
        Log.i("Retrofit data", "created1");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.i("Retrofit data", "created2");
        //create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(NetworkUtils.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        MovieClient client = retrofit.create(MovieClient.class);
        Call<Movie> call = client.getPopularMovie();
        try{
            Response<Movie> result = call.execute();
            Log.i("MOVIES", result.body().getOriginalTitle());
            Log.i("Retrofit data", result.toString());
            Log.i("Retrofit","Success!");

        }catch(IOException ioe) {
            Log.i("Retrofit","Failure!");
        }

    }
}
