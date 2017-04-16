package com.an.trailers.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Rating implements Serializable {


    private String imdbRating;
    private String Metascore;
    private String imdbVotes;
    private String Rated;
    private String Language;
    private String Country;
    private String Awards;
    private List<Map<String, String>> Ratings;

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getMetascore() {
        return Metascore;
    }

    public void setMetascore(String metascore) {
        Metascore = metascore;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }

    public List<Map<String, String>> getRatings() {
        return Ratings;
    }

    public void setRatings(List<Map<String, String>> ratings) {
        Ratings = ratings;
    }
}
