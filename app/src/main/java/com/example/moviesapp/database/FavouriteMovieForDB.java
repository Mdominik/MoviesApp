package com.example.moviesapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.moviesapp.api.model.Cast;
import com.example.moviesapp.api.model.ExtendedMovie;
import com.example.moviesapp.api.model.Review;
import com.example.moviesapp.api.model.Video;
import com.example.moviesapp.utilities.TimestampConverter;

import org.joda.time.DateTime;

import java.util.ArrayList;

@Entity(tableName = "favourite_movies")
public class FavouriteMovieForDB {
    public int getId_inDB() {
        return id_inDB;
    }

    public void setId_inDB(int id_inDB) {
        this.id_inDB = id_inDB;
    }

    @PrimaryKey(autoGenerate = true)
    private int id_inDB;

    public int getIdFromAPi() {
        return idFromAPi;
    }

    public void setIdFromAPi(int idFromAPi) {
        this.idFromAPi = idFromAPi;
    }

    @ColumnInfo(name = "idFromAPi")
    private int idFromAPi;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @ColumnInfo(name = "posterPath")
    private String posterPath;

//    @ColumnInfo(name = "addedToFavDate")
//    @TypeConverters({TimestampConverter.class})
//    private DateTime addedToFavDate;

    public FavouriteMovieForDB(int idFromAPi, String posterPath) {
//        this.addedToFavDate = addedToFavDate;
        this.idFromAPi = idFromAPi;
        this.posterPath = posterPath;
    }


//    public DateTime getAddedToFavDate() {
//        return addedToFavDate;
//    }
//
//    public void setAddedToFavDate(DateTime addedToFavDate) {
//        this.addedToFavDate = addedToFavDate;
//    }

}
