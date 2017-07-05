package com.example.y_v_d.popularmovie;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y_v_d.popularmovie.data.MovieContract;
import com.example.y_v_d.popularmovie.models.Movie;
import com.squareup.picasso.Picasso;

import static com.example.y_v_d.popularmovie.adapters.MovieAdapter.ARG_MOVIE;


public class MovieDetailFragment extends Fragment {
    private Movie movie;
    TextView movieTitle;
    TextView release_date;
    TextView movieDescription;
    TextView rating;
    ImageView poster;

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

        movieTitle = (TextView) rootView.findViewById(R.id.title);
        release_date = (TextView) rootView.findViewById(R.id.release_date);
        movieDescription = (TextView) rootView.findViewById(R.id.overview);
        rating = (TextView) rootView.findViewById(R.id.user_rating);
        poster = (ImageView) rootView.findViewById(R.id.poster);

        Picasso.with(rootView.getContext())
                .load(movie.getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .into(poster);

        movieTitle.setText(movie.getTitle());
        release_date.setText(movie.getReleaseDate());
        rating.setText("Rating : " + movie.getUserRating().toString());
        movieDescription.setText(movie.getOverview());

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

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Picasso.with(activity)
                    .load(movie.getBackdrop())
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // add to favorite
    public void onClickAddMovie(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                movie.getPoster());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                movie.getUserRating());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                movie.getBackdrop());
        getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                contentValues);



    }
}
