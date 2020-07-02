package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.moviesapp.background.OnClickLangListener;
import com.example.moviesapp.utilities.PreferencesUtils;

import java.util.List;

public class LanguagePrefActivity extends AppCompatActivity  implements OnClickLangListener, LanguageAdapter.LanguageAdapterOnClickHandler {
    private RecyclerView mLanguageRecyclerView;
    private LanguageAdapter mLanguageAdapter;
    private List<String> mLanguagesList;
    public static TextView mLanguage;
    OnClickLangListener onClickLangListener;
    private TextView mCurrent_language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_pref);
        mLanguageRecyclerView = findViewById(R.id.rv_languagespref);
        mLanguagesList = PreferencesUtils.getLanguagesAsList();
        mLanguageAdapter = new LanguageAdapter(mLanguagesList, this, onClickLangListener);
        mCurrent_language = findViewById(R.id.tv_current_lang);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);

        mLanguageRecyclerView.setLayoutManager(layoutManager);
        mLanguageRecyclerView.setHasFixedSize(true);
        mLanguageRecyclerView.setAdapter(mLanguageAdapter);


        SharedPreferences sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
        mCurrent_language.setText("Current language: " + sharedPreferences.getString("language_name", "English"));
        //retrieving data from API


    }

    @Override
    public void onClick(int index) {
        SharedPreferences.Editor editor;
        editor = getSharedPreferences("language", MODE_PRIVATE).edit();
        editor.putString("language_name", mLanguagesList.get(index));
        mCurrent_language.setText("Current language: " + mLanguagesList.get(index));
        editor.apply();
    }

    @Override
    public void onClick() {

    }
}