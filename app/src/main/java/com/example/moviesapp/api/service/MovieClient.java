package com.example.moviesapp.api.service;

import com.example.moviesapp.api.model.POJO.ResponseFromJSONPopularityTopRated;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONReviews;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONUpcoming;
import com.example.moviesapp.api.model.POJO.ResponseFromJSONVideos;
import com.example.moviesapp.api.model.Review;
import com.example.moviesapp.api.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieClient {

    //append query params
    @GET("movie/popular")
    Call<ResponseFromJSONPopularityTopRated> getPopularMovie(@Query("api_key") String api_key, @Query("language") String lang);

    @GET("movie/top_rated")
    Call<ResponseFromJSONPopularityTopRated> getTopRatedMovie(@Query("api_key") String api_key, @Query("language") String lang);

    @GET("movie/upcoming")
    Call<ResponseFromJSONUpcoming> getUpcomingMovie(@Query("api_key") String api_key, @Query("language") String lang);


    //what datatype of video?
    @GET("movie/{id}/videos")
    Call<ResponseFromJSONVideos> getVideoByMovieID(@Path("id") int id);

    //what datatype of video?
    @GET("movie/{id}/reviews")
    Call<ResponseFromJSONReviews> getReviewsByMovieID(@Path("id") int id);



}
