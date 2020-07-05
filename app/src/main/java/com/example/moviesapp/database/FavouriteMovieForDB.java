package com.example.moviesapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.joda.time.DateTime;

@Entity(tableName = "favourite_movies")
public class FavouriteMovieForDB {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movieID;
    private String title;
    private DateTime addedToFavDate;

    public DateTime getAddedToFavDate() {
        return addedToFavDate;
    }

    public void setAddedToFavDate(DateTime addedToFavDate) {
        this.addedToFavDate = addedToFavDate;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FavouriteMovieForDB(int id, int movieID, String title) {
        this.id = id;
        this.movieID = movieID;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }
}
