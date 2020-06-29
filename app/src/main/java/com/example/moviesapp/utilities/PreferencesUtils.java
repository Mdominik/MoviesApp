package com.example.moviesapp.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.moviesapp.R;

import java.util.HashMap;

public class PreferencesUtils {
    public static void setLanguages(HashMap<String, String> languages) {
        PreferencesUtils.languages = languages;
    }

    public static HashMap<String,String> languages;

    public static String getLanguageCode(String languageName) {
        return languages.get(languageName) + "-" + languages.get(languageName).toUpperCase(); // example pl-PL
    }
    
}
