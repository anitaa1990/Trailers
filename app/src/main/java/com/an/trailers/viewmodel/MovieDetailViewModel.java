package com.an.trailers.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import com.an.trailers.AppController;
import com.an.trailers.Constants;
import com.an.trailers.model.APIResponse;
import com.an.trailers.model.Movie;
import com.an.trailers.model.MovieDb;
import com.an.trailers.model.MovieResponse;
import com.an.trailers.model.Rating;
import com.an.trailers.model.Video;
import com.an.trailers.rest.RestApi;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.an.trailers.Constants.MOVIE_RATING_PATH;
import static com.an.trailers.Constants.MOVIE_STATUS_RELEASED;
import static com.an.trailers.activity.DetailActivity.DESC_TRANSITION_NAME;
import static com.an.trailers.activity.DetailActivity.IMAGE_TRANSITION_NAME;
import static com.an.trailers.activity.DetailActivity.TITLE_TRANSITION_NAME;

public class MovieDetailViewModel extends Observable {


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Movie movie;
    private Context context;
    public MovieDetailViewModel(@NonNull Context context, Movie movie) {
        this.movie = movie;
        this.context = context;

        initializeViews();
    }


    private void initializeViews() {
        List<Video> videos = movie.getTrailers();
        if(!videos.isEmpty()) {
            refreshView(videos);
            fetchRatings();

        } else {
            fetchData();
        }
    }


    private void fetchData() {
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


    private void fetchRatings() {
        AppController appController = AppController.create(context);
        RestApi restApi = appController.getRestApi();

        Disposable ratingObservable = restApi.getRatingForMovie(String.format(MOVIE_RATING_PATH, movie.getImdbId()))
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessResponse, this::onFailureResponse);
        compositeDisposable.add(ratingObservable);
    }


    private void onSuccessResponse(Object object) {
        if(object instanceof Movie) {
            this.movie = (Movie) object;
            fetchRatings();
        }

        refreshView(object);
    }


    private void onFailureResponse(Throwable t) {

    }


    private void refreshView(Object obj) {
        setChanged();
        notifyObservers(obj);
    }


    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    public void updateDb(boolean isFavourite) {
        if(isFavourite) {
            MovieDb.getInstance().addToFavMovies(movie);
        } else {
            MovieDb.getInstance().removeFromFavMovies(movie);
        }
    }


    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
}
