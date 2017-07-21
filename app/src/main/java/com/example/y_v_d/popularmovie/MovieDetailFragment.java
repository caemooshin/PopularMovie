package com.example.y_v_d.popularmovie;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y_v_d.popularmovie.data.MovieContract;
import com.example.y_v_d.popularmovie.models.Movie;
import com.squareup.picasso.Picasso;

import static com.example.y_v_d.popularmovie.adapters.MovieAdapter.ARG_MOVIE;


public class MovieDetailFragment extends Fragment implements View.OnClickListener {
    private Movie movie;
    TextView mTitle;
    TextView mReleaseDate;
    TextView mDesc;
    TextView mRating;
    ImageView mPoster;
    Button favorite;
    Button unfavorite;
    ImageView mBackdrop;
    long mID;
    boolean Star = false;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_MOVIE)) {
            movie = getArguments().getParcelable(ARG_MOVIE);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mDesc = (TextView) rootView.findViewById(R.id.overview);
        mRating = (TextView) rootView.findViewById(R.id.user_rating);
        mPoster = (ImageView) rootView.findViewById(R.id.poster);
        favorite = (Button) rootView.findViewById(R.id.button_mark_as_favorite);
        favorite.setOnClickListener(this);
        unfavorite = (Button) rootView.findViewById(R.id.button_mark_as_unfavorite);
        unfavorite.setOnClickListener(this);
        mBackdrop= ((ImageView) rootView.findViewById(R.id.movie_backdrop));

        Picasso.with(rootView.getContext())
                .load(movie.getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .into(mPoster);

        mTitle.setText(movie.getTitle());
        mReleaseDate.setText(movie.getReleaseDate());
        mRating.setText("Rating : " + movie.getUserRating().toString());
        mDesc.setText(movie.getOverview());
        mID = movie.getId();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof MovieDetailActivity) {
            appBarLayout.setTitle(movie.getTitle());
        }

        // ImageView mBackdrop= ((ImageView) Activity.findViewById(R.id.movie_backdrop));
        if (mBackdrop != null) {
            Picasso.with(activity)
                    .load(movie.getBackdrop())
                    .config(Bitmap.Config.RGB_565)
                    .into(mBackdrop);
        }

        final FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        if (isFavorite()) {
            fab.setImageResource(R.drawable.ic_star_black_24dp);
            Star = true;
        }else{
            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
            Star = false;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Star == true){//remove from DB
                    fab.setImageResource(R.drawable.ic_star_border_black_24dp);
                    Star = false;
                    if (isFavorite()) {

                        mID = movie.getId();
                        String stringId = Long.toString(mID);

                        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                        uri = uri.buildUpon().appendPath(stringId).build();

                        // COMPLETED (2) Delete a single row of data using a ContentResolver
                        getActivity().getContentResolver().delete(uri, null, null);
                         Toast.makeText(getContext(), "yes", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getContext(), "no", Toast.LENGTH_LONG).show();
                }
                else {
                    fab.setImageResource(R.drawable.ic_star_black_24dp);
                    Star = true;
                    if (!isFavorite()) {

                        String Poster = movie.getPosterPath();
                        String Title = movie.getTitle();
                        String  ReleaseDate = movie.getReleaseDate();
                        String  Rating = movie.getUserRating().toString();
                        String  Desc = movie.getOverview();
                        String  Backdrop = movie.getBackdrop();
                        mID = movie.getId();

                        ContentValues contentValues = new ContentValues();
                        // Put the task description and selected mPriority into the ContentValues
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, Desc);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mID);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, Title);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, Poster);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, Backdrop);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, Rating);
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, ReleaseDate);
                        // Insert the content values via a ContentResolver
                        Uri uri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                        if (uri != null) {
                            Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // add to favorite
    public void onClickAddMovie(View view) {

        Toast.makeText(getContext(), "hi", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_mark_as_favorite:
                if (!isFavorite()) {
                    Toast.makeText(getContext(), "add", Toast.LENGTH_LONG).show();
                }


                if (!isFavorite()) {

                    String Poster = movie.getPosterPath();
                    String Title = movie.getTitle();
                    String  ReleaseDate = movie.getReleaseDate();
                    String  Rating = movie.getUserRating().toString();
                    String  Desc = movie.getOverview();
                    String  Backdrop = movie.getBackdrop();
                    mID = movie.getId();

                    ContentValues contentValues = new ContentValues();
                    // Put the task description and selected mPriority into the ContentValues
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, Desc);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mID);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, Title);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, Poster);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, Backdrop);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, Rating);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, ReleaseDate);
                    // Insert the content values via a ContentResolver
                    Uri uri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                    if (uri != null) {
                        Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case R.id.button_mark_as_unfavorite:

                if (isFavorite()) {

                       mID = movie.getId();
                    String stringId = Long.toString(mID);

                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();

                    // COMPLETED (2) Delete a single row of data using a ContentResolver
                    getActivity().getContentResolver().delete(uri, null, null);


                    Toast.makeText(getContext(), "yes", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(), "no", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private boolean isFavorite() {
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mID,
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }
}
