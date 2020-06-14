package com.example.moviesapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.moviesapp.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesAPIJsonUtils {


    private static String MOVIEAPI_LIST_NAME = "results";
    private static String MOVIEAPI_TITLE = "original_title";
    private static String MOVIEAPI_POSTER_PATH = "poster_path";
    private static String MOVIEAPI_OVERVIEW = "overview";
    private static String MOVIEAPI_RELEASE_DATE = "release_date";
    private static String MOVIEAPI_VOTE_AVERAGE = "vote_average";

    /* String array to hold each day's weather String */



    private static final String format = "json";


    public static ArrayList<Movie> getAllMoviesFromJSON(Context context, String moviesJsonStr) throws JSONException {
        ArrayList<Movie> parsedMoviesData = null;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray moviesArray = moviesJson.getJSONArray(MOVIEAPI_LIST_NAME);
        parsedMoviesData = new ArrayList<>(moviesArray.length());

        for(int i=0; i < moviesArray.length(); i++) {
            JSONObject singleMovie = moviesArray.getJSONObject(i);
            Movie movie = new Movie(singleMovie.getString(MOVIEAPI_TITLE),
                                    singleMovie.getString(MOVIEAPI_POSTER_PATH),
                                    singleMovie.getString(MOVIEAPI_OVERVIEW),
                                    singleMovie.getString(MOVIEAPI_RELEASE_DATE),
                                    singleMovie.getDouble(MOVIEAPI_VOTE_AVERAGE));

            parsedMoviesData.add(movie);
        }
        return parsedMoviesData;
    }


}
