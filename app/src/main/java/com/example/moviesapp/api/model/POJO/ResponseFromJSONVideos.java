package com.example.moviesapp.api.model.POJO;

import java.util.List;

import com.example.moviesapp.api.model.Video;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFromJSONVideos {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Video> videos = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}