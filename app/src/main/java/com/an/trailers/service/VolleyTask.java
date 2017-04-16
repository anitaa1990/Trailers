package com.an.trailers.service;

import android.content.Context;

import com.an.trailers.Constants;
import com.an.trailers.callback.MovieResponseListener;
import com.an.trailers.callback.RESTListener;

public class VolleyTask implements Runnable, Constants {

    private String method;
    private String movieId;
    private Context context;
    private RESTListener listener;
    public VolleyTask(Context context, String method, String movieId, RESTListener listener) {
        this.context = context;
        this.method = method;
        this.movieId = movieId;
        this.listener = listener;
    }

    private String pageId;
    private MovieResponseListener movieResponseListener;
    public VolleyTask(Context context, String pageId, MovieResponseListener movieResponseListener) {
        this.context = context;
        this.pageId = pageId;
        this.movieResponseListener = movieResponseListener;
    }

    public VolleyTask(Context context, String method, String query, MovieResponseListener movieResponseListener) {
        this.context = context;
        this.pageId = query;
        this.method = method;
        this.movieResponseListener = movieResponseListener;
    }

    @Override
    public void run() {
        if(method == null) {
            VolleyService.getInstance().getMoviesList(context, pageId, movieResponseListener);
        }
        else if(METHOD_MOVIE.equalsIgnoreCase(method)) {
            VolleyService.getInstance().getMovieDetails(context, movieId, listener);
        } else if(METHOD_VIDEO.equalsIgnoreCase(method)) {
            VolleyService.getInstance().getVideoDetails(context, movieId, listener);
        } else if(METHOD_CAST.equalsIgnoreCase(method)) {
            VolleyService.getInstance().getCastDetails(context, movieId, listener);
        } else if(METHOD_SEARCH.equalsIgnoreCase(method)) {
            VolleyService.getInstance().getSearchList(context, pageId, movieResponseListener);
        } else if(METHOD_RATING.equalsIgnoreCase(method)) {
            VolleyService.getInstance().getRatingForMovie(context, movieId, listener);
        }
    }
}
