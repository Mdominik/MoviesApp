package com.example.moviesapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.background.OnClickLangListener;


import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageAdapterViewHolder>  {
    public interface LanguageAdapterOnClickHandler {
        void onClick(int index);
    }
    List<String> mLanguages;
    LanguageAdapterOnClickHandler mClickHandler;
    TextView mLanguage;

    public LanguageAdapter(List<String> mLanguages, LanguageAdapterOnClickHandler mClickHandler, OnClickLangListener listener) {
        this.mLanguages = mLanguages;
        this.mClickHandler = mClickHandler;
        this.listener = listener;
    }

    private OnClickLangListener listener;


    @NonNull
    @Override
    public LanguageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.language_recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new LanguageAdapterViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(LanguageAdapterViewHolder holder, int position) {
        String lang = mLanguages.get(position);
        holder.mLanguage.setText(lang);
    }

    @Override
    public int getItemCount(){
        return mLanguages.size();
    }
    public class LanguageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnClickLangListener listener;
        public TextView mLanguage;

        //ImageView to store the poster picture
        public LanguageAdapterViewHolder(View itemView, OnClickLangListener listener) {
            super(itemView);
            this.listener = listener;
            this.mLanguage =  itemView.findViewById(R.id.language_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }


    }
}
