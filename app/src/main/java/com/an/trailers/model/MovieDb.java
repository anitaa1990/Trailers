package com.an.trailers.model;

import com.an.trailers.utils.BaseUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDb implements Serializable {

    private static MovieDb instance;
    public static MovieDb getInstance() {
        if(instance == null) instance = new MovieDb();
        return instance;
    }

    private MovieDb genieDb;
    public MovieDb() {
        if(genieDb == null) genieDb = (MovieDb) BaseUtils.readObjectFromDisk();
    }

    private void saveToDisk() {
        BaseUtils.writeObjectToDisk(genieDb);
    }

    private Map<Long, Movie> favMovies;

    public Map<Long, Movie> getFavMovies() {
        if(favMovies == null) favMovies = new HashMap<>();
        return favMovies;
    }

    public void setFavMovies(Map<Long, Movie> favMovies) {
        this.favMovies = favMovies;
        saveToDisk();
    }

    public void addToFavMovies(Movie movie) {
        Map<Long, Movie> faves = getFavMovies();
        faves.put(movie.getId(), movie);
    }

    public void removeFromFavMovies(Movie movie) {
        Map<Long, Movie> faves = getFavMovies();
        if(faves.containsKey(movie.getId()))
            faves.remove(movie.getId());
        setFavMovies(faves);
    }

    public boolean isFavourite(long movieId) {
        return getFavMovies().containsKey(movieId);
    }
}
