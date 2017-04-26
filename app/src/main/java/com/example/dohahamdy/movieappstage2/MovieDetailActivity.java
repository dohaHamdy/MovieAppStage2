package com.example.dohahamdy.movieappstage2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        MovieDetailFragment fragment=new MovieDetailFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_movie_details, fragment)
                .commit();
    }
}
