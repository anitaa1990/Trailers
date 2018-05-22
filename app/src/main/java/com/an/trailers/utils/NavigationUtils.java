package com.an.trailers.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.an.trailers.Constants;
import com.an.trailers.activity.MovieDetailActivity;
import com.an.trailers.activity.VideoActivity;
import com.an.trailers.model.Movie;

public class NavigationUtils implements Constants {


    public static void redirectToDetailScreen(Activity activity,
                                              Movie movie,
                                              ActivityOptionsCompat options) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MAP, movie);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }


    public static void redirectToDetailScreen(Activity activity,
                                              Movie movie) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MAP, movie);
        activity.startActivity(intent);
    }


    public static void redirectToVideoScreen(Activity activity,
                                             Movie movie) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(INTENT_VIDEO_KEY, movie.getTrailerCode());
        intent.putExtra(INTENT_MOVIE_ID, String.valueOf(movie.getId()));
        activity.startActivity(intent);
    }


    public static void redirectToVideoScreen(Context context,
                                             String videoKey) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(INTENT_VIDEO_KEY, videoKey);
        context.startActivity(intent);
    }
}
