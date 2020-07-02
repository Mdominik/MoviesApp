package com.example.moviesapp.utilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class CSVReader {
    public static TreeMap<String, String> getLanguages() {
        return languages;
    }

    private static TreeMap<String, String> languages = new TreeMap<>();

    private static BufferedReader br;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static TreeMap<String,String> getLanguagesForJSON(InputStream inputStream) {
        try {
            String sCurrentLine;
            br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int count = 0;
            while ((sCurrentLine = br.readLine ()) != null) {
                if(count > 1) {
                    sCurrentLine = sCurrentLine.substring(1, sCurrentLine.length()-1);
                    String[] s = sCurrentLine.split("\",\"");
                    languages.put(s[1], s[0]);

                }
                count++;
            }

            return languages;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
                return languages;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return languages;
    }

}
