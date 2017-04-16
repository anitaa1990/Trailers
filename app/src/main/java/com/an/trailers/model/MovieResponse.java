package com.an.trailers.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MovieResponse implements Serializable {

    private int page;
    private List<Movie> results;
    private Map<String, String> dates;

    @SerializedName("total_pages")
    private long totalPages;

    @SerializedName("total_results")
    private long totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public Map<String, String> getDates() {
        return dates;
    }

    public void setDates(Map<String, String> dates) {
        this.dates = dates;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }
}
