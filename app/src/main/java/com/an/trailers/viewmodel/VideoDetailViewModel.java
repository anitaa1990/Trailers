package com.an.trailers.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;

import com.an.trailers.AppController;
import com.an.trailers.Constants;
import com.an.trailers.model.APIResponse;
import com.an.trailers.rest.RestApi;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class VideoDetailViewModel extends AbstractViewModel {

    private String movieId;
    private Context context;
    private String videoKey;
    public VideoDetailViewModel(@NonNull Context context, String movieId, String videoKey) {
        this.context = context;
        this.movieId = movieId;
        this.videoKey = videoKey;

        initializeViews();
    }


    @Override
    public void initializeViews() {
        if(videoKey == null) {
            fetchData();
        }
    }


    @Override
    public void fetchData() {
        AppController appController = AppController.create(context);
        RestApi restApi = appController.getRestApi();

        Disposable disposable  = restApi.fetchMovieVideos(movieId, Constants.TMDB_API_KEY)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessResponse, this::onFailureResponse);
        compositeDisposable.add(disposable);
    }


    @Override
    public void onSuccessResponse(Object object) {
        if(object instanceof APIResponse) {
            APIResponse apiResponse = (APIResponse) object;
            if(apiResponse.getResults() != null && !apiResponse.getResults().isEmpty()) {
                setVideoKey(apiResponse.getResults().get(0).getKey());
                refreshView(apiResponse.getResults());
            }
        }
    }


    @Override
    public void onFailureResponse(Throwable t) {

    }

    private void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getVideoKey() {
        return videoKey;
    }
}
