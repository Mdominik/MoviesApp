package com.example.moviesapp.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.content.Context;
@Database(entities = {FavouriteMovieForDB.class}, version = 1)
public abstract class FavouriteMovieDatabase extends RoomDatabase {
    private static FavouriteMovieDatabase instance;
    public abstract FavouriteMovieDAO favouriteMovieDao();

    public static synchronized FavouriteMovieDatabase getInstance(Context context) {

        //singleton
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FavouriteMovieDatabase.class, "fav movies database")
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }


}
