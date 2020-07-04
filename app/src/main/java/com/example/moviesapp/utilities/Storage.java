package com.example.moviesapp.utilities;

import com.example.moviesapp.api.model.Cast;

import java.util.ArrayList;

public class Storage {
    private static ArrayList<Cast> mCast;

    public static ArrayList<Cast> getCast() {
        return mCast;
    }

    public static void setCast(ArrayList<Cast> cast) {
        mCast = cast;
    }
}
