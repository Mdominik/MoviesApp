package com.example.moviesapp.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.moviesapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class PreferencesUtils {
    public static final String STATE_EXTENDED_MOVIES = "extendedMovies";
    public static final String STATE_REVIEWS = "reviews";
    public static final String STATE_CAST = "cast";
    public static final String STATE_DIRECTOR = "director";
    public static final String STATE_VIDEOS = "videos";

    public static void setLanguages(TreeMap<String, String> languages) {
        PreferencesUtils.languages = languages;
    }

    public static TreeMap<String, String> getLanguages() {
        return languages;
    }

    public static ArrayList<String> getLanguagesAsList() {
        return new ArrayList<>(languages.keySet());
    }

    private static TreeMap<String,String> languages;

    public static String getLanguageCode(String languageName) {
        return languages.get(languageName) + "-" + languages.get(languageName).toUpperCase(); // example pl-PL
    }
    
}
