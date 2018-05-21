package com.an.trailers.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.an.trailers.AppController;
import com.an.trailers.Constants;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.rest.RestApi;
import com.an.trailers.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MovieListViewModel extends AbstractViewModel implements Constants {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int page;
    private String url;
    private int position;
    private long totalPages;
    private Context context;
    private List<Movie> movies;
    public MovieListViewModel(@NonNull Context context) {
        this.page = 1;
        this.context = context;
        this.totalPages = page;
        this.movies = new ArrayList<>();
    }


    public void fetchData(int position) {
        this.position = position;
        this.page = 1;
        this.totalPages = page;

        initializeViews();
    }


    @Override
    public void initializeViews() {
        if(page > totalPages) return;

        switch (position) {
            case 0:
                this.url = MOVIES_UPCOMING_PATH;
                fetchData();
                break;

            case 1:
                this.url = MOVIES_NOW_PLAYING_PATH;
                fetchData();
                break;

            case 2:
                this.url = MOVIES_TOP_RATED_PATH;
                fetchData();
                break;

            case 3:
                fetchFavouritesFromLocal();
                break;
        }
    }


    @Override
    public void fetchData() {
        AppController appController = AppController.create(context);
        RestApi restApi = appController.getRestApi();

        Disposable ratingObservable = restApi.fetchMovies(String.format(url, TMDB_API_KEY, String.valueOf(page)))
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessResponse, this::onFailureResponse);
        compositeDisposable.add(ratingObservable);
    }



    @Override
    public void onSuccessResponse(Object object) {
        if(object instanceof MovieResponse) {
            MovieResponse movieResponse = (MovieResponse) object;
            this.page = movieResponse.getPage()+1;
            this.totalPages = movieResponse.getTotalPages();
            this.movies.addAll(movieResponse.getResults());
            refreshView(BaseUtils.getFragments(movies));
            System.out.println("@@##@@" + movies.size());
            this.movies.clear();
        }
    }


    @Override
    public void onFailureResponse(Throwable t) {

    }


    public void updateData(int currentViewPagerPosition,
                           int adapterCount) {
        if(currentViewPagerPosition+2 == adapterCount && position != 3) {
            initializeViews();
        }
    }


    private void fetchFavouritesFromLocal() {
        refreshView(BaseUtils.getFragments(BaseUtils.getFavMovies()));
    }
}
