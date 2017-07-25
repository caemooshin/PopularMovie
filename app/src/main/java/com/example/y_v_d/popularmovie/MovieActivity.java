package com.example.y_v_d.popularmovie;

import android.content.Context; 
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.y_v_d.popularmovie.adapters.MovieAdapter;
import com.example.y_v_d.popularmovie.data.MovieContract;
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

public class MovieActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private final static String API_KEY = "";
    private final static String MOST_POPULAR = "popular";
    private final static String TOP_RATED = "top_rated";
    private final static String FAVORITE = "favorite";

    private String mSortBy = MOST_POPULAR;

    private RecyclerView rvListmovie;
    private MovieAdapter mMovieAdapter;
    ArrayList<Movie> movies;
    ArrayList<Movie> favorite;

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
        // i change shared preference

        //mSortBy = sharedPref.getString(getString(R.string.preference_sortby_key), defaultValue);

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
    //add




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
            case FAVORITE:
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
        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        switch (item.getItemId()) {
            case R.id.sort_by_top_rated:
                mSortBy = TOP_RATED;
                //editor.putString(getString(R.string.preference_sortby_key), mSortBy);
                //editor.commit();
                fetchMovie(mSortBy);
                item.setChecked(true);
                break;
            case R.id.sort_by_favorite:
                mSortBy = FAVORITE;
                //editor.putString(getString(R.string.preference_sortby_key), mSortBy);
                //editor.commit();
                fetchFavorite();
                item.setChecked(true);
                break;
            case R.id.sort_by_most_popular:
                mSortBy = MOST_POPULAR;
                //editor.putString(getString(R.string.preference_sortby_key), mSortBy);
                //editor.commit();
                fetchMovie(mSortBy);
                item.setChecked(true);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID);

                } catch (Exception e) {

                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;

                if (mTaskData != null)// If Cursordepot is null then do
                // nothing
                {
                    if (mTaskData.moveToFirst()) {


                        do {
                            // Set agents information in model.
                            Movie Agents = new Movie();

                            Agents.setmId(mTaskData.getInt(mTaskData
                                    .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                            Agents.setmTitle(mTaskData.getString(mTaskData
                                    .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
                            Agents.setmPoster(mTaskData.getString(mTaskData
                                    .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)));
                            Agents.setmOverview(mTaskData.getString(mTaskData
                                    .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
                            Agents.setmUserRating(mTaskData.getString(mTaskData
                                    .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)));
                            Agents.setmReleaseDate(mTaskData.getString(mTaskData
                                    .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
                            Agents.setmBackdrop(mTaskData.getString(mTaskData
                                    .getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH)));

                            favorite.add(Agents);
                        } while (mTaskData.moveToNext());
                    }
                    mTaskData.close();
                }

                //super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       // mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mAdapter.swapCursor(null);

    }

    private void fetchFavorite(){

        mMovieAdapter = new MovieAdapter(favorite, R.layout.movie_item, getApplicationContext());
        rvListmovie.setAdapter(mMovieAdapter);

    }


}
