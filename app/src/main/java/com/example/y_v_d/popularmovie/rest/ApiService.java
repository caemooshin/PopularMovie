package com.example.y_v_d.popularmovie.rest;

import com.example.y_v_d.popularmovie.models.MoviesResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    //endpoint
    @GET("movie/top_rated")
    Call<MoviesResult> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResult> getPopularMovies(@Query("api_key") String apiKey);

}
