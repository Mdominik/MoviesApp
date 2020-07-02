package com.example.moviesapp.background;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.moviesapp.api.model.Cast;
import com.example.moviesapp.api.model.Crew;
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

public class OtherDataService extends IntentService {
    public OtherDataService(){
        super("OtherDataService");
    }
    //1=sort by popular, 2=sort by top rated, 3=sort by fav
    private int movie_id;

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

    public OtherDataService(String name, int movie_id) {
        super(name);
        this.movie_id = movie_id;
    }

//    @Override
//    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        sortOption = intent.getIntExtra("sortOption", 1);
//        return Service.START_STICKY;
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        movie_id = intent.getIntExtra("movie_id", 0);
        Log.i("Movie ID", ""+movie_id);
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

        Call<ResponseFromJSONReviews> callReviews  = null;
        Call<ResponseFromJSONCast> callCast = null;
        Call<ResponseFromJSONVideos> callVideos  = null;
        //ExtendedMovie is also a response class
        Call<ExtendedMovie> callExtendedMovie = null;

        Response<ResponseFromJSONReviews> resultReviews  = null;
        Response<ResponseFromJSONCast> resultCast = null;
        Response<ResponseFromJSONVideos> resultVideos  = null;
        //ExtendedMovie is also a response class
        Response<ExtendedMovie> resultExtendedMovie = null;


        ExtendedMovie extendedMovie= null;
        ArrayList<Review> reviews = null;
        ArrayList<Video> videos = null;
        ArrayList<Cast> cast = null;
        ArrayList<Crew> crew = null;

        try {
            AssetManager am = getAssets();
            InputStream inputStream = am.open("languages.csv");
            PreferencesUtils.setLanguages(CSVReader.getLanguagesForJSON(inputStream));

        } catch(IOException e) {Log.i("NIE DZIALA", "REE");}

        // REQUESTS
        SharedPreferences sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
        String lan = sharedPreferences.getString("language_name", "English");

        callReviews = client.getReviewsByMovieID(movie_id, NetworkUtils.URL_API_KEY);
        callCast = client.getCastByMovieID(movie_id, NetworkUtils.URL_API_KEY);
        callExtendedMovie = client.getMovieByID(movie_id, NetworkUtils.URL_API_KEY, PreferencesUtils.getLanguageCode(lan));
        callVideos = client.getVideoByMovieID(movie_id, NetworkUtils.URL_API_KEY);

        try{
            resultReviews = callReviews.execute();
            reviews = (ArrayList<Review>)resultReviews.body().getReviews();
            Log.i("OTherDataService", "Reviews"+reviews.size());

            resultExtendedMovie = callExtendedMovie.execute();
            extendedMovie = resultExtendedMovie.body();
            Log.i("OTherDataService", "extendedMovie"+extendedMovie.getTitle());

            resultCast = callCast.execute();
            cast = (ArrayList<Cast>) resultCast.body().getCast();
            String directorsName = null;
            for(Crew c : resultCast.body().getCrew()) {
                if(c.getJob().equals("Director")) {
                    directorsName = c.getName();
                    break;
                }
                else {
                    directorsName = "unknown";
                }
            }

            resultVideos = callVideos.execute();
            videos = (ArrayList<Video>) resultVideos.body().getVideos();
            Log.i("OTherDataService", "videos"+videos.size());

            //send it to MovieActivity:
            Intent resultIntent = new Intent("OtherDataService");
            resultIntent.putParcelableArrayListExtra("review", reviews);
            resultIntent.putExtra("extendedMovie", extendedMovie);
            resultIntent.putParcelableArrayListExtra("cast", cast);
            resultIntent.putParcelableArrayListExtra("videos", videos);
            resultIntent.putParcelableArrayListExtra("crew", crew);
            resultIntent.putExtra("director", directorsName);

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
