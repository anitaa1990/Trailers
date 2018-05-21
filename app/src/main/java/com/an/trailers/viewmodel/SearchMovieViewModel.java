package com.an.trailers.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.an.trailers.AppController;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.rest.RestApi;
import com.an.trailers.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.an.trailers.Constants.TMDB_API_KEY;

public class SearchMovieViewModel extends AbstractViewModel {

    private int page;
    private Context context;
    private long totalPages;
    private String queryText;
    private List<Movie> movies;
    public SearchMovieViewModel(@NonNull Context context) {
        this.page = 1;
        this.context = context;
        this.totalPages = page;
        this.movies = new ArrayList<>();
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void fetchData() {
        AppController appController = AppController.create(context);
        RestApi restApi = appController.getRestApi();

        Disposable disposable = restApi.searchMovies(TMDB_API_KEY, queryText, String.valueOf(page))
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessResponse, this::onFailureResponse);
        compositeDisposable.add(disposable);
    }

    @Override
    public void onSuccessResponse(Object object) {
        if(object instanceof MovieResponse) {
            MovieResponse movieResponse = (MovieResponse) object;
            this.page = movieResponse.getPage()+1;
            this.totalPages = movieResponse.getTotalPages();
            this.movies.addAll(movieResponse.getResults());
            refreshView(BaseUtils.getFragments(movies));
            this.movies.clear();
        }
    }

    @Override
    public void onFailureResponse(Throwable t) {

    }


    public void updateData(int currentViewPagerPosition,
                           int adapterCount) {
        if(currentViewPagerPosition+2 == adapterCount && page <= totalPages) {
            fetchData();
        }
    }

    public void searchMovie(String queryText) {
        this.queryText = queryText;
        this.page = 1;
        this.totalPages = page;
        this.movies.clear();
        fetchData();
    }

    public boolean isPaginating() {
        return page > 2;
    }
}
