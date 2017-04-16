package com.an.trailers.callback;


import com.an.trailers.model.Movie;

import java.util.List;

public interface MovieResponseListener {
    void onMoviesResponse(List<Movie> movies, int currentPage, long totalPages);
}
