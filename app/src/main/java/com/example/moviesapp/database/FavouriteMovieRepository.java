package com.example.moviesapp.database;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class FavouriteMovieRepository {
    private FavouriteMovieDAO dao;
    private LiveData<List<FavouriteMovieForDB>> allFavMovies;

    public FavouriteMovieRepository(Application application) {
        FavouriteMovieDatabase db = FavouriteMovieDatabase.getInstance(application);
        dao = db.favouriteMovieDao();
        allFavMovies = dao.getFavouritesByDate();
    }

    public void insert(FavouriteMovieForDB movie) {

    }

    public void update(FavouriteMovieForDB movie) {

    }

    public void delete(FavouriteMovieForDB movie) {

    }

    public LiveData<List<FavouriteMovieForDB>> getAllFavMovies() {
        return allFavMovies;
    }

    private static class InsertFavMovieAsyncTaskLoader extends AsyncTaskLoader<FavouriteMovieForDB, Void,Void> {
        private FavouriteMovieDAO movieDAO;

        public InsertFavMovieAsyncTaskLoader(@NonNull Context context) {
            super(context);
        }



        public InsertFavMovieAsyncTaskLoader(@NonNull Context context,FavouriteMovieDAO movieDAO) {
            super(context);
            this.movieDAO = movieDAO;
        }

        @Nullable
        @Override
        public FavouriteMovieForDB loadInBackground() {
            return null;
        }
    }
}
