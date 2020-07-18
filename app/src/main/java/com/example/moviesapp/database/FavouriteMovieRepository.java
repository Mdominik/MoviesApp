package com.example.moviesapp.database;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.example.moviesapp.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavouriteMovieRepository {
    private FavouriteMovieDAO dao;
    private LiveData<List<FavouriteMovieForDB>> allFavMovies;
    private FavouriteMovieDatabase db;
    public FavouriteMovieRepository(Application application) {
        db = FavouriteMovieDatabase.getInstance(application);
        dao = db.favouriteMovieDao();
        allFavMovies = dao.getFavourites();
        Log.i("FavMOvieRepo created","yes");
        Log.i("FavMovieREpoallFavMoies", ""+(allFavMovies.getValue()==null) );
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

     public Integer getByID(Integer i) {
         try {
             Log.i("Has item called", "yes");
             return new CheckIfMovieExistsAsyncTask(dao).execute(i).get();
         } catch (ExecutionException e) {
             Log.i("No element in DB", "yes");
             return 0;
         } catch (InterruptedException e) {
             e.printStackTrace();

         }
         return 0;
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
    private static class CheckIfMovieExistsAsyncTask extends AsyncTask<Integer, Void, Integer> {
        private FavouriteMovieDAO movieDAO;

        public CheckIfMovieExistsAsyncTask(FavouriteMovieDAO dao) {
            movieDAO = dao;
        }

        @Override
        protected Integer doInBackground(Integer... ids) {
            return (movieDAO.getByID(ids[0]) == null) ? 0 : movieDAO.getByID(ids[0]);
        }

        @Override
        protected void onPostExecute(Integer intt) {
            Log.i("CheckIfMovie finished", ""+intt);
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
