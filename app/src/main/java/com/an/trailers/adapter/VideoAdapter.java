package com.an.trailers.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.activity.VideoActivity;
import com.an.trailers.model.Video;
import com.an.trailers.utils.BaseUtils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private List<Video> feedItems;
    private Activity ctx;
    private int screenWidth;
    private int screenHeight;
    private final double ASPECT_RATIO_WIDTH = 61.1;
    private final double ASPECT_RATIO_HEIGHT = 20.27;
    public VideoAdapter(Activity context, List<Video> feedItems) {
        this.ctx = context;
        this.feedItems = feedItems;
        this.screenWidth = BaseUtils.getScreenWidth(context);
        this.screenHeight = BaseUtils.getScreenHeight(context);
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
        return new VideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, final int position) {


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.vidFrame.setVisibility(View.VISIBLE);
                holder.playBtn.setImageResource(R.drawable.ic_play);
            }
        };

        holder.youTubeThumbnailView.initialize(Constants.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                final Video item = feedItems.get(position);
                youTubeThumbnailLoader.setVideo(item.getKey());
                youTubeThumbnailView.setImageBitmap(null);
                //new LoadImage(youTubeThumbnailView).execute(item.getSnippet().getThumbnails().getDefault().getUrl());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private YouTubeThumbnailView youTubeThumbnailView;
        private View vidFrame;
        private ImageView playBtn;
        private ImageView shareBtn;

        public VideoHolder(View itemView) {
            super(itemView);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
            youTubeThumbnailView.setOnClickListener(this);
            vidFrame = itemView.findViewById(R.id.vid_frame);
            playBtn = (ImageView) itemView.findViewById(R.id.btnYoutube_player);
            shareBtn = (ImageView) itemView.findViewById(R.id.share_btn);
            shareBtn.setOnClickListener(this);

            ViewGroup.LayoutParams lp = youTubeThumbnailView.getLayoutParams();

            Double width = Math.ceil((ASPECT_RATIO_WIDTH * screenWidth)/100);
            Double height = Math.ceil((ASPECT_RATIO_HEIGHT * screenHeight)/100);
            lp.width = width.intValue();
            lp.height = height.intValue();
            youTubeThumbnailView.setLayoutParams(lp);
        }

        @Override
        public void onClick(View v) {
            Video video = feedItems.get(getLayoutPosition());
            if(v == youTubeThumbnailView) {
                Intent intent = new Intent(ctx, VideoActivity.class);
                intent.putExtra("video_key", video.getKey());
                ctx.startActivity(intent);

            } else if(v == shareBtn) {
                BaseUtils.shareMovie(ctx, video.getKey());
            }
        }
    }
}
