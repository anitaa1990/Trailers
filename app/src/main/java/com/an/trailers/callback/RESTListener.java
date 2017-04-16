package com.an.trailers.callback;

import android.util.Pair;

import com.an.trailers.model.Movie;
import com.an.trailers.model.Video;

import java.util.List;

public interface RESTListener {

    void onVideoResponse(List<Video> videos);
    void onMovieDetailResponse(Movie movie);
    void onCreditsResponse(Pair<String, String> creditPair);
}
