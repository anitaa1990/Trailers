package com.an.trailers;

import java.util.Arrays;
import java.util.List;

public interface Constants {

    String IMAGE_URL = "https://image.tmdb.org/t/p/w500%s";
    String IMDB_MOVIE_LINK = "http://www.imdb.com/title/%s";
    String LOCALE_CACHE_PATH = "/data/data/com.an.trailers/trailers.dat";

    String YOUTUBE_API_KEY = "AIzaSyCZY8Vnw_6GcJcESL-NilTZDMSvg9ViLt8";
    String TMDB_API_KEY = "5e74ee79280d770dc8ed5a2fbdda955a";

    String MOVIE_STATUS_RELEASED = "Released";
    String METHOD_MOVIE = "movie_detail";
    String METHOD_VIDEO = "movie_video";
    String METHOD_CAST = "movie_cast";
    String METHOD_SEARCH = "movie_search";
    String METHOD_RATING = "movie_rating";
    String METHOD_MOVIE_SIMILAR = "movie_similar";

    String CREDIT_CAST = "cast";
    String CREDIT_CREW = "crew";

    String SOURCE_IMDB = "Internet Movie Database";
    String SOURCE_RT = "Rotten Tomatoes";
    String SOURCE_MC = "Metacritic";

    String YOUTUBE_VIDEO_PATH = "https://www.youtube.com/watch?v=%s";

    String BASE_URL = "https://api.themoviedb.org/3/";

    String MOVIES_LIST_PATH = "movie/upcoming?api_key=%s&language=en-US&page=%s";
    String MOVIE_DETAIL_PATH = "movie/%s?api_key=%s";
    String MOVIE_VIDEOS_PATH = "movie/%s/videos?api_key=%s";
    String MOVIE_CAST_PATH = "movie/%s/credits?api_key=%s";
    String MOVIE_SEARCH_PATH = "search/movie?api_key=%s&query=%s";
    String MOVIE_SIMILAR_PATH = "movie/%s/similar?api_key=%s&page=1";
    String MOVIE_RATING_PATH = "http://www.omdbapi.com/?i=%s&tomatoes=true&r=json";

    List<String> genresList = Arrays.asList("#febf9b", "#f47f87", "#6ac68d", "#fbe0a5");
}
