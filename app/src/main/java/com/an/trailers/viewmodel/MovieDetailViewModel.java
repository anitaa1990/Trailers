package com.an.trailers.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.an.trailers.AppController;
import com.an.trailers.Constants;
import com.an.trailers.model.APIResponse;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieDb;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.model.Video;
import com.an.trailers.rest.RestApi;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.an.trailers.Constants.MOVIE_RATING_PATH;

public class MovieDetailViewModel extends AbstractViewModel {

    private Movie movie;
    public MovieDetailViewModel(@NonNull Context context, Movie movie) {
        this.movie = movie;
        this.context = context;

        initializeViews();
    }


    @Override
    public void initializeViews() {
        List<Video> videos = movie.getTrailers();
        if(!videos.isEmpty()) {
            refreshView(videos);
            fetchRatings();

        } else {
            fetchData();
        }
    }

    @Override
    public void fetchData() {
        AppController appController = AppController.create(context);
        RestApi restApi = appController.getRestApi();


        io.reactivex.Observable<Movie> movieDetailObservable = restApi.fetchMovieDetails(String.valueOf(movie.getId()), Constants.TMDB_API_KEY);
        io.reactivex.Observable<APIResponse> videosObservable = restApi.fetchMovieVideos(String.valueOf(movie.getId()), Constants.TMDB_API_KEY);
        io.reactivex.Observable<APIResponse> castObservable = restApi.fetchCastDetails(String.valueOf(movie.getId()), Constants.TMDB_API_KEY);
        io.reactivex.Observable<MovieResponse> similarMoviesObservable = restApi.fetchSimilarMovies(String.valueOf(movie.getId()), Constants.TMDB_API_KEY, "1");

        Disposable disposable = io.reactivex.Observable.concat(movieDetailObservable, videosObservable, castObservable, similarMoviesObservable)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessResponse, this::onFailureResponse);
        compositeDisposable.add(disposable);
    }

    @Override
    public void onSuccessResponse(Object object) {
        if(object instanceof Movie) {
            this.movie = (Movie) object;
            fetchRatings();
        }

        refreshView(object);
    }


    @Override
    public void onFailureResponse(Throwable t) {

    }


    private void fetchRatings() {
        AppController appController = AppController.create(context);
        RestApi restApi = appController.getRestApi();

        Disposable ratingObservable = restApi.getRatingForMovie(String.format(MOVIE_RATING_PATH, movie.getImdbId()))
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessResponse, this::onFailureResponse);
        compositeDisposable.add(ratingObservable);
    }


    public void updateDb(boolean isFavourite) {
        if(isFavourite) {
            MovieDb.getInstance().addToFavMovies(movie);
        } else {
            MovieDb.getInstance().removeFromFavMovies(movie);
        }
    }

    public boolean isFavourite(long id) {
        return MovieDb.getInstance().isFavourite(id);
    }
}
