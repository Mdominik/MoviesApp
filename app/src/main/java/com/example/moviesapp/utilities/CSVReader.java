package com.example.moviesapp.utilities;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class CSVReader {
    private static HashMap<String, String> languages = new HashMap<>();
    private static BufferedReader br;
    public static HashMap<String,String> getLanguagesForJSON(InputStream inputStream) {
        try {
            String sCurrentLine;
            br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
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
