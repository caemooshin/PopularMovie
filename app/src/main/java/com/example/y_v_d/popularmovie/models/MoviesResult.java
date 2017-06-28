package com.example.y_v_d.popularmovie.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Y-V-D on 2017-06-28.
 */

public class MoviesResult {
    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }
}
