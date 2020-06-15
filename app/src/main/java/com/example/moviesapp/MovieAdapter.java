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

    public int getNumberColumnsVertical() {
        return numberColumnsVertical;
    }

    public void setNumberColumnsVertical(int numberColumnsVertical) {
        this.numberColumnsVertical = numberColumnsVertical;
    }

    public int getNumberColumnsHorizontal() {
        return numberColumnsHorizontal;
    }

    public void setNumberColumnsHorizontal(int numberColumnsHorizontal) {
        this.numberColumnsHorizontal = numberColumnsHorizontal;
    }

    private int numberColumnsVertical = 2;
    private int numberColumnsHorizontal = 3;
    public List<Movie> getmMoviesList() {
        return mMoviesList;
    }

    public void setmMoviesList(List<Movie> mMoviesList) {
        this.mMoviesList = mMoviesList;
    }

    public MovieAdapter(List<Movie> movies, MovieAdapterOnClickHandler mClickHandler) {
        this.mMoviesList = movies;
        this.mClickHandler = mClickHandler;
    }

    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(int index);
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
        Movie movie = mMoviesList.get(position);
        String posterPath = movie.getPosterPath();
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

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ImageView to store the poster picture
        public ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.movieItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }


}
