package com.example.moviesapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moviesapp.api.model.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FavouriteMovieDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FavouriteMovieForDB movie);

    @Update
    void update(FavouriteMovieForDB movie);

    @Delete
    void delete(FavouriteMovieForDB movie);

    @Query("DELETE FROM favourite_movies")
    void deleteAllFavourites();

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovieForDB>> getFavourites();

    @Query("DELETE FROM favourite_movies WHERE idFromAPI=:idd")
    void deleteMovieByID(int idd);


    @Query("SELECT idFromAPI FROM favourite_movies WHERE idFromAPI= :id")
    Integer getByID(Integer id);
}
