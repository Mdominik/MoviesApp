package com.example.moviesapp.database;

import android.app.Application;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviesapp.api.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavMovieViewModel extends AndroidViewModel{
    private final FavouriteMovieRepository repository;
    private LiveData<List<FavouriteMovieForDB>> allFavMovies;


    public FavMovieViewModel(@NonNull Application application) {
        super(application);
        repository = new FavouriteMovieRepository(application);
    }

    public void insert(FavouriteMovieForDB movie) {
        repository.insert(movie);
        Log.i("Is repositIrt null?", ""+(repository ==null));
    }
    public void update(FavouriteMovieForDB movie) {
        repository.update(movie);
    }
    public void delete(FavouriteMovieForDB movie) {
        repository.delete(movie);
    }
    public void deleteAllFavMovies() {
        repository.deleteAll();
    }

    public LiveData<List<FavouriteMovieForDB>> getAllFavMovies() {
        return repository.getAllFavMovies();
    }
    public void deleteMovieByID(int id) {
        Log.i("Deleting movie by id", id+"");
        repository.deleteMovieByID(id);
        Log.i("Is repo indelete null?", ""+(repository ==null));
    }

    public Integer getByID(Integer id) {
        return repository.getByID(id);
    }

}
