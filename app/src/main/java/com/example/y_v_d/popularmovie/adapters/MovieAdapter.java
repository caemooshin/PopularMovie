package com.example.y_v_d.popularmovie.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private final static String LOG_TAG = MovieListAdapter.class.getSimpleName();

    public MovieAdapter() {
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder{

            public MovieAdapterViewHolder(View itemView) {
                super(itemView);
            }

    }
}
