package com.an.trailers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;
import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.callback.RESTListener;
import com.an.trailers.model.Movie;
import com.an.trailers.model.Video;
import com.an.trailers.service.RESTExecutorService;
import com.an.trailers.service.VolleyTask;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, Constants, RESTListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private String videoId;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Constants.YOUTUBE_API_KEY, this);

        videoId = getIntent().getStringExtra("video_key");
        if(videoId == null) {
            String movieId = getIntent().getExtras().getString("movieId");
            RESTExecutorService.submit(new VolleyTask(this, METHOD_VIDEO, movieId, this));
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setFullscreen(true);
        this.youTubePlayer = player;
        if (!wasRestored && videoId != null) {
            player.loadVideo(videoId); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public void onVideoResponse(List<Video> videos) {
        if(youTubePlayer != null) {
            youTubePlayer.loadVideo(videos.get(0).getKey());
        }
    }

    @Override
    public void onMovieDetailResponse(Movie movie) {

    }

    @Override
    public void onCreditsResponse(Pair<String, String> creditPair) {

    }
}
