package com.example.moviesapp.database;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.example.moviesapp.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMovieRepository {
    private FavouriteMovieDAO dao;
    private LiveData<List<FavouriteMovieForDB>> allFavMovies;

    public FavouriteMovieRepository(Application application) {
        FavouriteMovieDatabase db = FavouriteMovieDatabase.getInstance(application);
        dao = db.favouriteMovieDao();
        allFavMovies = dao.getFavourites();
    }

    //API That repository exposes to outside
    public void insert(FavouriteMovieForDB movie) {
        new InsertFavMovieAsyncTask(dao).execute(movie);
    }
    public void update(FavouriteMovieForDB movie) {
        new UpdateFavMovieAsyncTask(dao).execute(movie);
    }
    public void delete(FavouriteMovieForDB movie) {
        new DeleteFavMovieAsyncTask(dao).execute(movie);
    }
    public void deleteAll() {
        new DeleteAllFavMoviesAsyncTask(dao).execute();
    }
    public LiveData<List<FavouriteMovieForDB>> getAllFavMovies() {
        return allFavMovies;
    }
    public void deleteMovieByID(int id) {
        new DeleteFavMovieByIDAsyncTask(dao).execute(id);
    }




    private static class DeleteFavMovieByIDAsyncTask extends AsyncTask<Integer, Void,Void> {
        private FavouriteMovieDAO movieDAO;

        public DeleteFavMovieByIDAsyncTask(FavouriteMovieDAO dao) {
            movieDAO = dao;
        }



        public DeleteFavMovieByIDAsyncTask(@NonNull Context context,FavouriteMovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            movieDAO.deleteMovieByID(ids[0]);
            return null;
        }
    }

    private static class InsertFavMovieAsyncTask extends AsyncTask<FavouriteMovieForDB, Void,Void> {
        private FavouriteMovieDAO movieDAO;

        public InsertFavMovieAsyncTask(FavouriteMovieDAO dao) {
            movieDAO = dao;
        }



        public InsertFavMovieAsyncTask(@NonNull Context context,FavouriteMovieDAO movieDAO) {
            this.movieDAO = movieDAO;
        }

        @Override
        protected Void doInBackground(FavouriteMovieForDB... movies) {
            movieDAO.insert(movies[0]);
            return null;
        }
    }

    private static class UpdateFavMovieAsyncTask extends AsyncTask<FavouriteMovieForDB, Void,Void> {
        private FavouriteMovieDAO movieDAO;

        public UpdateFavMovieAsyncTask(FavouriteMovieDAO dao) {
            movieDAO = dao;
        }

        @Override
        protected Void doInBackground(FavouriteMovieForDB... movies) {
            movieDAO.update(movies[0]);
            return null;
        }
    }

    private static class DeleteFavMovieAsyncTask extends AsyncTask<FavouriteMovieForDB, Void,Void> {
        private FavouriteMovieDAO movieDAO;

        public DeleteFavMovieAsyncTask(FavouriteMovieDAO dao) {
            movieDAO = dao;
        }

        @Override
        protected Void doInBackground(FavouriteMovieForDB... movies) {
            movieDAO.delete(movies[0]);
            return null;
        }
    }

    private static class DeleteAllFavMoviesAsyncTask extends AsyncTask<Void, Void,Void> {
        private FavouriteMovieDAO movieDAO;

        public DeleteAllFavMoviesAsyncTask(FavouriteMovieDAO dao) {
            movieDAO = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDAO.deleteAllFavourites();
            return null;
        }
    }
}
