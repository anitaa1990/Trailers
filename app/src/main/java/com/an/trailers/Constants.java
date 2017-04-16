package com.an.trailers;

public interface Constants {

    String IMAGE_URL = "https://image.tmdb.org/t/p/w500%s";
    String IMDB_MOVIE_LINK = "http://www.imdb.com/title/%s";

    String YOUTUBE_API_KEY = "AIzaSyCZY8Vnw_6GcJcESL-NilTZDMSvg9ViLt8";
    String TMDB_API_KEY = "5e74ee79280d770dc8ed5a2fbdda955a";

    String MOVIE_STATUS_RELEASED = "Released";
    String METHOD_MOVIE = "movie_detail";
    String METHOD_VIDEO = "movie_video";
    String METHOD_CAST = "movie_cast";
    String METHOD_SEARCH = "movie_search";

    String CREDIT_CAST = "cast";
    String CREDIT_CREW = "crew";

    String YOUTUBE_VIDEO_PATH = "https://www.youtube.com/watch?v=%s";

    String BASE_URL = "https://api.themoviedb.org/3/";

    String MOVIES_LIST_PATH = "movie/upcoming?api_key=%s&language=en-US&page=%s";
    String MOVIE_DETAIL_PATH = "movie/%s?api_key=%s";
    String MOVIE_VIDEOS_PATH = "movie/%s/videos?api_key=%s";
    String MOVIE_CAST_PATH = "movie/%s/credits?api_key=%s";
    String MOVIE_SEARCH_PATH = "search/movie?api_key=%s&query=%s";
}
