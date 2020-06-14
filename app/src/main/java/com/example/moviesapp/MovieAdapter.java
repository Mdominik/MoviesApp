package com.example.moviesapp;

import android.content.Context;
import com.example.moviesapp.Movie;
import com.squareup.picasso.Picasso;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    List<Movie> mMoviesList;
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
        Movie movie = mMoviesList.get(position);

//        String originalTitle = movie.getOriginalTitle();
          String posterPath = movie.getPosterPath();
//        String overview = movie.getOverview();
//        String releaseDate = movie.getReleaseDate();
//        Double voteAverage = movie.getVoteAverage();

        Picasso.get().load(posterPath).fit();

        // set views:
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
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
