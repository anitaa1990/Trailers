package com.an.trailers.service;

import android.content.Context;
import android.util.Pair;
import com.an.trailers.Constants;
import com.an.trailers.MyVolley;
import com.an.trailers.callback.MovieResponseListener;
import com.an.trailers.callback.RESTListener;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.model.Video;
import com.an.trailers.utils.BaseUtils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class VolleyService implements Constants {

    private static VolleyService instance;
    public static VolleyService getInstance() {
        if(instance == null) instance = new VolleyService();
        return instance;
    }

    public void getMovieDetails(Context context, String movieId, final RESTListener listener) {
        CacheRequest stringRequest = new CacheRequest(Request.Method.GET,
                String.format(BASE_URL + MOVIE_DETAIL_PATH, movieId, TMDB_API_KEY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Movie movie = BaseUtils.getMovieDetails(response);
                        listener.onMovieDetailResponse(movie);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MyVolley.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getVideoDetails(Context context, String movieId, final RESTListener listener) {
        CacheRequest stringRequest = new CacheRequest(Request.Method.GET,
                String.format(BASE_URL + MOVIE_VIDEOS_PATH, movieId, TMDB_API_KEY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Video> videos = BaseUtils.getMovieVideos(response);
                        listener.onVideoResponse(videos);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MyVolley.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void getCastDetails(Context context, String movieId, final RESTListener listener) {
        CacheRequest stringRequest = new CacheRequest(Request.Method.GET,
                String.format(BASE_URL + MOVIE_CAST_PATH, movieId, TMDB_API_KEY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Pair<String, String> castDetails = BaseUtils.getMovieCast(response);
                        listener.onCreditsResponse(castDetails);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MyVolley.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void getMoviesList(Context context, String pageId, final MovieResponseListener listener) {
        CacheRequest stringRequest = new CacheRequest(Request.Method.GET,
                String.format(BASE_URL + MOVIES_LIST_PATH, TMDB_API_KEY, pageId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MovieResponse movieResponse = BaseUtils.getMovieList(response);
                        List<Movie> movies = movieResponse.getResults();
                        listener.onMoviesResponse(movies, movieResponse.getPage(), movieResponse.getTotalPages());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MyVolley.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getSearchList(Context context, String searchTxt, final MovieResponseListener listener) {
        try {
            String finalQueryTxt = URLEncoder.encode(searchTxt, "UTF-8");
            CacheRequest stringRequest = new CacheRequest(Request.Method.GET,
                    String.format(BASE_URL + MOVIE_SEARCH_PATH, TMDB_API_KEY, finalQueryTxt),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            MovieResponse movieResponse = BaseUtils.getMovieList(response);
                            List<Movie> movies = movieResponse.getResults();
                            listener.onMoviesResponse(movies, movieResponse.getPage(), movieResponse.getTotalPages());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            MyVolley.getInstance(context).addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
