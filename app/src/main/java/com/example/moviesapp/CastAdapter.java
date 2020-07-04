package com.example.moviesapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.api.model.Cast;
import com.example.moviesapp.background.OnClickCastListener;
import com.example.moviesapp.background.OnClickLangListener;
import com.example.moviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastAdapterViewHolder>  {
    public interface CastAdapterOnClickHandler {
        void onClick(int index);
    }
    List<Cast> mCasts;
    CastAdapterOnClickHandler mClickHandler;
    CardView mCastCard;
    Activity context;
    public CastAdapter(Activity context) {
        this.mCasts = new ArrayList<Cast>();
        this.context = context;
    }

    private OnClickCastListener listener;

    public void setCasts(List<Cast> castList) {
        mCasts.clear();
        mCasts.addAll(castList);
        this.notifyItemRangeInserted(0, mCasts.size() - 1);
        Log.i("IN cast adpter castlist", ""+mCasts);
    }
    @NonNull
    @Override
    public CastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.cast_recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new CastAdapterViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(CastAdapterViewHolder holder, int position) {
        TextView name = holder.mCast.findViewById(R.id.tv_actorName);
        name.setText(mCasts.get(position).getName());

        TextView character = holder.mCast.findViewById(R.id.tv_characterName);
        character.setText("as " + mCasts.get(position).getCharacter());

        final ImageView image = holder.mCast.findViewById(R.id.iv_actorImage);
        Picasso.get().load(NetworkUtils.URL_BASE_FOR_POSTER+
                                NetworkUtils.URL_SIZE_CAST_THUMBNAIL+
                                mCasts.get(position).getProfilePath())
                .into(image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(
                                "http://image.tmdb.org/t/p/w342/cckcYc2v0yh1tc9QjRelptcOBko.jpg")
                                .into(image);
                    }
                });
        Log.i("OnBindVIewHolder Cast", holder.mCast+"");
    }

    @Override
    public int getItemCount(){
        Log.i("Cast item count", ""+mCasts.size());
        return mCasts.size();

    }
    public class CastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnClickCastListener listener;
        public CardView mCast;

        //CardView to store the actors data
        public CastAdapterViewHolder(View itemView, OnClickCastListener listener) {
            super(itemView);
            this.listener = listener;
            this.mCast =  itemView.findViewById(R.id.cv_cast);
            Log.i("CastAdapter", "created");
        }

        @Override
        public void onClick(View v) {
            //int adapterPosition = getAdapterPosition();
            //mClickHandler.onClick(adapterPosition);
        }


    }
}
