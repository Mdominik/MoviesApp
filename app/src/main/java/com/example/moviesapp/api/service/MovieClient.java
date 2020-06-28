package com.example.moviesapp.api.service;

import com.example.moviesapp.api.model.ResponseFromJSON;
import com.example.moviesapp.api.model.Review;
import com.example.moviesapp.api.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieClient {

    //append query params
    @GET("movie/popular?api_key=cad019638a028a2ef5d2aa2ddf283278")
    Call<ResponseFromJSON> getPopularMovie();

    @GET("movie/top_rated?api_key=cad019638a028a2ef5d2aa2ddf283278")
    Call<ResponseFromJSON> getTopRatedMovie();


    //what datatype of video?
    @GET("movie/{id}/videos")
    Call<List<Video>> getVideoByMovieID(@Path("id") int id);

    //what datatype of video?
    @GET("movie/{id}/review")
    Call<List<Review>> getReviewsByMovieID(@Path("id") int id);



}
