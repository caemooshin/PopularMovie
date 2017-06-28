package com.example.y_v_d.popularmovie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.y_v_d.popularmovie.adapters.MovieAdapter;
import com.example.y_v_d.popularmovie.models.Movie;
import com.example.y_v_d.popularmovie.models.MovieService;
import com.example.y_v_d.popularmovie.models.MoviesResult;
import com.example.y_v_d.popularmovie.rest.ApiClient;
import com.example.y_v_d.popularmovie.rest.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {

    private final static String API_KEY = "API_KEY";
    public final static String MOST_POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";

    private String mSortBy = MOST_POPULAR;

    private RecyclerView rvListmovie;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        rvListmovie = (RecyclerView) findViewById(R.id.list_movie);
        rvListmovie.setLayoutManager(new GridLayoutManager(this, 2));

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<MoviesResult> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                List<Movie> movies = response.body().getResults();
                rvListmovie.setAdapter(new MovieAdapter(movies, R.layout.movie_item, getApplicationContext()));

            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        switch (mSortBy) {
            case MOST_POPULAR:
                menu.findItem(R.id.sort_by_most_popular).setChecked(true);
                break;
            case TOP_RATED:
                menu.findItem(R.id.sort_by_top_rated).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:

                mSortBy = TOP_RATED;
                item.setChecked(true);
                break;
            case R.id.sort_by_most_popular:
                mSortBy = MOST_POPULAR;
                rvListmovie = (RecyclerView) findViewById(R.id.list_movie);
                rvListmovie.setLayoutManager(new GridLayoutManager(this, 2));

                ApiService apiService =
                        ApiClient.getClient().create(ApiService.class);

                Call<MoviesResult> call = apiService.getTopRatedMovies(API_KEY);
                call.enqueue(new Callback<MoviesResult>() {
                    @Override
                    public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                        List<Movie> movies = response.body().getResults();
                        rvListmovie.setAdapter(new MovieAdapter(movies, R.layout.movie_item, getApplicationContext()));

                    }

                    @Override
                    public void onFailure(Call<MoviesResult> call, Throwable t) {

                    }
                });

                item.setChecked(true);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
