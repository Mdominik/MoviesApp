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

    private static int NUMBER_FIELDS = 6;

    //fields from JSON that I'm using
    private static final String[] MOVIEAPI_FIELDS= new String[]{
            "results","original_title","poster_path","overview", "release_date","vote_average","backdrop_path",
    };

    public static ArrayList<Movie> getAllMoviesFromJSON(Context context, String moviesJsonStr){
        ArrayList<Movie> parsedMoviesData = null;
        JSONObject moviesJson;
        JSONArray moviesArray = null;
        try {
            moviesJson = new JSONObject(moviesJsonStr);

            moviesArray = moviesJson.getJSONArray(MOVIEAPI_FIELDS[0]); // get results array
            parsedMoviesData = new ArrayList<>(moviesArray.length());
        } catch(JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i < moviesArray.length(); i++) {
            JSONObject singleMovie=null;

            //first exception handling
            try {
                singleMovie = moviesArray.getJSONObject(i);
            } catch(JSONException e) {
                e.printStackTrace();
            }

            // I know it should be in a HashMap
            String[] fieldsValues = new String[NUMBER_FIELDS]; //title,posterPath,overview,date,vote_average
            for(int j=0; j<NUMBER_FIELDS;j++) {

                //JSON fields error handling.
                //if any of the fields is not present, a field will be filled in with default data
                try{

                    fieldsValues[j] = singleMovie.getString(MOVIEAPI_FIELDS[j+1]);
                    if(j==3) fieldsValues[j] = fieldsValues[j].substring(0,4);  //first 4 digits for a year

                } catch(JSONException e) {

                    //if double (4th in the array) absent
                    if(j==4) {
                        fieldsValues[j] = "0.0";
                    }

                    else fieldsValues[j] = "unknown";
                }
            }

            //create movie object with parameters taken from JSON after error handling
            Movie movie = new Movie(fieldsValues[0],
                                    fieldsValues[1],
                                    fieldsValues[2],
                                    fieldsValues[3],
                                    Double.parseDouble(fieldsValues[4]),
                                    fieldsValues[5]);

            parsedMoviesData.add(movie);
        }
        return parsedMoviesData;
    }


}
