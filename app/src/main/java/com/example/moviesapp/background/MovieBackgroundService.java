package com.example.moviesapp.background;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.moviesapp.api.model.Cast;
import com.example.moviesapp.api.model.ExtendedMovie;
import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONCast;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONPopularityTopRated;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONReviews;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONUpcoming;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONVideos;
import com.example.moviesapp.api.model.Review;
import com.example.moviesapp.api.model.Video;
import com.example.moviesapp.api.service.MovieClient;
import com.example.moviesapp.utilities.CSVReader;
import com.example.moviesapp.utilities.Config;
import com.example.moviesapp.utilities.NetworkUtils;
import com.example.moviesapp.utilities.PreferencesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
        Call<ResponseFromJSONPopularityTopRated> callPopularityTopRated = null;
        Call<ResponseFromJSONUpcoming> callUpcoming = null;
        Response<ResponseFromJSONPopularityTopRated> resultPopularityTopRated=null;
        Response<ResponseFromJSONUpcoming> resultUpcoming=null;
        ArrayList<Movie> movies = null;


        try {
            AssetManager am = getAssets();
            InputStream inputStream = am.open("languages.csv");
            PreferencesUtils.setLanguages(CSVReader.getLanguagesForJSON(inputStream));

        } catch(IOException e) {Log.i("NIE DZIALA", "REE");}

        String lan = Config.getLanguage();;
        switch(sortOption) {

            case 1:
                callPopularityTopRated = client.getPopularMovie(NetworkUtils.URL_API_KEY, PreferencesUtils.getLanguageCode(lan));
                break;
            case 2:
                callPopularityTopRated = client.getTopRatedMovie(NetworkUtils.URL_API_KEY, PreferencesUtils.getLanguageCode(lan), 500);
                break;
            case 3:
                callUpcoming = client.getUpcomingMovie(NetworkUtils.URL_API_KEY, PreferencesUtils.getLanguageCode(lan), 500);
                break;
            default:
                callPopularityTopRated = client.getPopularMovie(NetworkUtils.URL_API_KEY, PreferencesUtils.getLanguageCode(lan));
        }

        try{
            if(sortOption == 1 || sortOption == 2) {
                resultPopularityTopRated = callPopularityTopRated.execute();
                movies = (ArrayList<Movie>)resultPopularityTopRated.body().getMovies();
            }
            else if(sortOption == 3) {
                resultUpcoming = callUpcoming.execute();
                movies = (ArrayList<Movie>)resultUpcoming.body().getMovies();
            }
            Log.i("Retrofit","Success!");
            Log.i("sort",""+getSharedPreferences("sort", MODE_PRIVATE).getInt("sort",0));

            //send it to MainActivity:
            Intent resultIntent = new Intent("MovieBackgroundService");
            resultIntent.putParcelableArrayListExtra("movies", movies);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(resultIntent);
        }catch(IOException ioe) {
            Log.i("Retrofit","Failure!");
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
