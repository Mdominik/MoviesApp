package com.example.moviesapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavouriteMovieDAO {

    @Insert
    void insert(FavouriteMovieForDB movie);

    @Update
    void update(FavouriteMovieForDB movie);

    @Delete
    void delete(FavouriteMovieForDB movie);

    @Query("DELETE FROM favourite_movies")
    void deleteAllFavourites();

    @Query("SELECT * FROM favourite_movies ORDER BY addedToFavDate DESC")
    LiveData<List<FavouriteMovieForDB>> getFavouritesByDate();

}
