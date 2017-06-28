package com.example.y_v_d.popularmovie;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.y_v_d.popularmovie.models.Movie;
import com.squareup.picasso.Picasso;

import static com.example.y_v_d.popularmovie.adapters.MovieAdapter.ARG_MOVIE;

public class MovieDetailActivity extends AppCompatActivity {

    TextView movieTitle;
    TextView release_date;
    TextView movieDescription;
    TextView rating;
    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Movie movie = getIntent().getParcelableExtra(ARG_MOVIE);

        movieTitle = (TextView) findViewById(R.id.title);
        release_date = (TextView) findViewById(R.id.release_date);
        movieDescription = (TextView) findViewById(R.id.overview);
        rating = (TextView) findViewById(R.id.user_rating);
        poster = (ImageView) findViewById(R.id.poster);

        Picasso.with(this)
                .load(movie.getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .into(poster);

        movieTitle.setText(movie.getTitle());
        release_date.setText(movie.getReleaseDate());
        rating.setText("Rating : " + movie.getUserRating().toString());
        movieDescription.setText(movie.getOverview());

    }

}
