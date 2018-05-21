package com.an.trailers.rest;

import com.an.trailers.model.APIResponse;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.model.Rating;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface RestApi {


    @GET
    Observable<MovieResponse> fetchMovies(@Url String url);


    @GET("/3/movie/{movieId}")
    Observable<Movie> fetchMovieDetails(@Path("movieId") String movieId,
                                        @Query("api_key") String apiKey);


    @GET("/3/movie/{movieId}/videos")
    Observable<APIResponse> fetchMovieVideos(@Path("movieId") String movieId,
                                             @Query("api_key") String apiKey);



    @GET("/3/movie/{movieId}/credits")
    Observable<APIResponse> fetchCastDetails(@Path("movieId") String movieId,
                                             @Query("api_key") String apiKey);



    @GET("/3/movie/{movieId}/similar")
    Observable<MovieResponse> fetchSimilarMovies(@Path("movieId") String movieId,
                                                 @Query("api_key") String apiKey,
                                                 @Query("page") String page);


    @GET
    Observable<Rating> getRatingForMovie(@Url String url);


    @GET
    Observable<MovieResponse> getDvdMovies(@Url String url);
}
