package com.example.y_v_d.popularmovie;

import android.content.Context; 
import android.content.SharedPreferences; 
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.y_v_d.popularmovie.adapters.MovieAdapter;
import com.example.y_v_d.popularmovie.models.Movie;
import com.example.y_v_d.popularmovie.models.MoviesResult;
import com.example.y_v_d.popularmovie.rest.ApiClient;
import com.example.y_v_d.popularmovie.rest.ApiService;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {

    private final static String API_KEY = "";
    private final static String MOST_POPULAR = "popular";
    private final static String TOP_RATED = "top_rated";

    private String mSortBy = MOST_POPULAR;

    private RecyclerView rvListmovie;
    private MovieAdapter mMovieAdapter;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))

                        .build());

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY first from themoviedb.org", Toast.LENGTH_LONG).show();
            return;
        }
        rvListmovie = (RecyclerView) findViewById(R.id.list_movie);
        //rvListmovie.setLayoutManager(new GridLayoutManager(this, 2));
        rvListmovie.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(this)));

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.preference_sortby_key_default);
        mSortBy = sharedPref.getString(getString(R.string.preference_sortby_key), defaultValue);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            fetchMovie(mSortBy);
        }
        else {
            movies = savedInstanceState.getParcelableArrayList("movies");
            mMovieAdapter = new MovieAdapter(movies, R.layout.movie_item, getApplicationContext());
            rvListmovie.setAdapter(mMovieAdapter);

        }

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    private void fetchMovie(String sortBy){

        ApiService apiService =
                ApiClient.getClient().create(ApiService.class);

        Call<MoviesResult> call = apiService.getMovies(sortBy, API_KEY);
        call.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                movies = response.body().getResults();
                mMovieAdapter = new MovieAdapter(movies, R.layout.movie_item, getApplicationContext());
                rvListmovie.setAdapter(mMovieAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        ArrayList<Movie> movies = mMovieAdapter.getMovies();
        if (movies != null && !movies.isEmpty()) {
            outState.putParcelableArrayList("movies", movies);
        }
        outState.putString("EXTRA_SORT_BY", mSortBy);
        super.onSaveInstanceState(outState);
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
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                mSortBy = TOP_RATED;
                editor.putString(getString(R.string.preference_sortby_key), mSortBy);
                editor.commit();
                fetchMovie(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_most_popular:
                mSortBy = MOST_POPULAR;
                editor.putString(getString(R.string.preference_sortby_key), mSortBy);
                editor.commit();
                fetchMovie(mSortBy);
                item.setChecked(true);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
