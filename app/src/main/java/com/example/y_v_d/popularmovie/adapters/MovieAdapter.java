package com.example.y_v_d.popularmovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.y_v_d.popularmovie.MovieDetailActivity;
import com.example.y_v_d.popularmovie.R;
import com.example.y_v_d.popularmovie.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private ArrayList<Movie> movies;
    private int mLayoutItem;
    private Context mContext;
    public static final String ARG_MOVIE = "ARG_MOVIE";

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.mTitle.setText(movies.get(position).getTitle());

        Picasso.with(mContext)
                .load(movies.get(position).getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .into(holder.mThumbnailView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra(ARG_MOVIE, movies.get(position));
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout moviesLayout;
        TextView mTitle;
        public final View mView;
        ImageView mThumbnailView;



        public MyViewHolder(View v) {
            super(v);

            mView = v;
            mThumbnailView = (ImageView) itemView.findViewById(R.id.thumbnail);
            mTitle = (TextView) itemView.findViewById(R.id.title);
        }
    }
    public MovieAdapter(ArrayList<Movie> movies, int mLayoutItem, Context mContext) {
        this.movies = movies;
        this.mLayoutItem = mLayoutItem;
        this.mContext = mContext;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}