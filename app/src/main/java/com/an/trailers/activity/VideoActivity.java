package com.an.trailers.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;
import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.databinding.VideoActivityBinding;
import com.an.trailers.viewmodel.VideoDetailViewModel;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import java.util.Observable;
import java.util.Observer;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, Observer, Constants {

    private YouTubePlayer youTubePlayer;
    private static final int RECOVERY_REQUEST = 1;


    private VideoActivityBinding videoActivityBinding;
    private VideoDetailViewModel videoDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDataBinding();
        initializeViewModel();
    }


    private void initializeDataBinding() {
        videoActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        videoActivityBinding.youtubeView.initialize(Constants.YOUTUBE_API_KEY, this);
    }


    private void initializeViewModel() {
        String videoId = getIntent().getStringExtra(INTENT_VIDEO_KEY);
        String movieId = getIntent().getExtras().getString(INTENT_MOVIE_ID);
        videoDetailViewModel = new VideoDetailViewModel(getApplicationContext(), movieId, videoId);
        videoDetailViewModel.addObserver(this);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setFullscreen(true);
        this.youTubePlayer = player;
        if (!wasRestored && videoDetailViewModel.getVideoKey() != null) {
             player.loadVideo(videoDetailViewModel.getVideoKey()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
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
        return videoActivityBinding.youtubeView;
    }


    @Override
    public void update(Observable observable, Object o) {
        if(youTubePlayer != null) {
            youTubePlayer.loadVideo(videoDetailViewModel.getVideoKey());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoDetailViewModel.reset();
    }
}
