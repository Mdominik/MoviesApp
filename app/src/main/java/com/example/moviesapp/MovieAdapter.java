package com.example.moviesapp;

import android.content.Context;

import com.example.moviesapp.api.model.Movie;
import com.example.moviesapp.background.OnClickPosterListener;
import com.example.moviesapp.database.FavouriteMovieForDB;
import com.example.moviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<?> mMoviesList;
    private static final int ITEM_FAV_MOVIE = 0;
    private static final int ITEM_NORMAL_MOVIE = 1;

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
    public List<?> getmMoviesList() {
        return mMoviesList;
    }


    public void setmMoviesList(List<?> mMoviesList) {
        this.mMoviesList = mMoviesList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMoviesList.get(position) instanceof Movie) {
            return ITEM_NORMAL_MOVIE;
        } else {
            return ITEM_FAV_MOVIE;
        }
    }

    private OnClickPosterListener listener;
    public MovieAdapter(ArrayList<?> movies, MovieAdapterOnClickHandler mClickHandler, OnClickPosterListener listener) {
        this.mMoviesList = movies;
        this.mClickHandler = mClickHandler;
        this.listener = listener;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        if(viewType == ITEM_NORMAL_MOVIE) {
            return new MovieAdapterViewHolder(view, listener);
        } else {
            return new FavMovieAdapterViewHolder(view, listener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == ITEM_NORMAL_MOVIE) {
            Movie movie = (Movie) mMoviesList.get(position);
            String posterPath = movie.getPosterPath();
            Picasso.get().load(NetworkUtils.buildUrlForPoster(posterPath)).into(((MovieAdapterViewHolder)holder).mMovieImageView);

        } else if(holder.getItemViewType() == ITEM_FAV_MOVIE){
            FavouriteMovieForDB movie = (FavouriteMovieForDB)mMoviesList.get(position);
            String posterPath = movie.getPosterPath();
            Picasso.get().load(NetworkUtils.buildUrlForPoster(posterPath)).into(((FavMovieAdapterViewHolder)holder).mMovieImageView);
        }

    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public void setMoviesData(List<?> movieList) {
        mMoviesList = movieList;
        notifyDataSetChanged();
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ImageView to store the poster picture
        public ImageView mMovieImageView;
        private OnClickPosterListener listener;
        public MovieAdapterViewHolder(View itemView, OnClickPosterListener listener) {
            super(itemView);
            this.listener = listener;

            mMovieImageView = (ImageView) itemView.findViewById(R.id.movieItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.onClick(adapterPosition);
        }
    }

    public class FavMovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ImageView to store the poster picture
        public ImageView mMovieImageView;
        private OnClickPosterListener listener;
        public FavMovieAdapterViewHolder(View itemView, OnClickPosterListener listener) {
            super(itemView);
            this.listener = listener;

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
