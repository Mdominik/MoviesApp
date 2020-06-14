package com.example.moviesapp;

import android.content.Context;
import com.example.moviesapp.Movie;
import com.example.moviesapp.utilities.MoviesAPIJsonUtils;
import com.example.moviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    List<Movie> mMoviesList;
    public int numberOfColumns = 3;
    public List<Movie> getmMoviesList() {
        return mMoviesList;
    }

    public void setmMoviesList(List<Movie> mMoviesList) {
        this.mMoviesList = mMoviesList;
    }

    public MovieAdapter(List<Movie> movies) {
        this.mMoviesList = movies;
    }

    public MovieAdapter() {

    }


    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Log.i("POSITION BIND HOLDER ", String.valueOf(position));
        Movie movie = mMoviesList.get(position);
        Log.i("MOVIE BIND HOLDER", mMoviesList.get(position).getOriginalTitle());
        String posterPath = movie.getPosterPath();

        Log.i("POSTER PATH", NetworkUtils.buildUrlForPoster(posterPath));
        Picasso.get().load(NetworkUtils.buildUrlForPoster(posterPath)).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public void setMoviesData(ArrayList<Movie> movieList) {
        mMoviesList = movieList;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        //ImageView to store the poster picture
        public ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.movieItem);
        }
    }

}
