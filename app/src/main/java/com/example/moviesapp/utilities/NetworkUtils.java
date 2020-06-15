package com.example.moviesapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    private static String URL_API_KEY_QUERY = "api_key";
    private static String URL_SORT_BY_QUERY = "sort_by";

    private static String URL_API_KEY = "cad019638a028a2ef5d2aa2ddf283278"; //to hide on GITHUB
    private static String URL_SORT_BY_POPULARITY = "popularity.desc";
    private static String URL_SORT_BY_RATING = "vote_average.desc";

    private static String URL_VOTECOUNT_GREATER_THAN = "vote_count.gte";
    private static String URL_VOTECOUNT_GREATER_THAN_VALUE = "1000";

    private static String URL_BASE_FOR_POSTER = "https://image.tmdb.org/t/p/";

    private static String URL_SIZE_POSTER = "w185";

    private static String URL_BASE = "https://api.themoviedb.org/3/discover/movie";

    public static String getURLBaseAndSizeForPoster() {
        return URL_BASE_FOR_POSTER+URL_SIZE_POSTER;
    }
    public static String buildUrlForPoster(String posterPath) {
        return URL_BASE_FOR_POSTER + URL_SIZE_POSTER + posterPath;
    }


    public static URL buildUrl(boolean sortByRating) {
        Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                .appendQueryParameter(URL_API_KEY_QUERY,URL_API_KEY)
                .appendQueryParameter(URL_SORT_BY_QUERY,
                        sortByRating ? URL_SORT_BY_RATING : URL_SORT_BY_POPULARITY)
                .appendQueryParameter(URL_VOTECOUNT_GREATER_THAN, URL_VOTECOUNT_GREATER_THAN_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI: " + url);

        return url;

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String s = scanner.next();
                scanner.close();
                return s;
            } else {
                scanner.close();
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
