package com.an.trailers.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.an.trailers.R;
import com.an.trailers.activity.BaseActivity;
import com.an.trailers.activity.DetailActivity;
import com.an.trailers.activity.VideoActivity;
import com.an.trailers.model.Movie;
import com.an.trailers.utils.BaseUtils;
import com.an.trailers.views.DragLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;


public class CommonFragment extends Fragment implements DragLayout.GotoDetailListener, View.OnClickListener {
    private final double CONTAINER_ASPECT_RATIO = 54.05;

    private ImageView imageView;
    private TextView movieTitle, movieDesc;
    private ImageView playBtn;
    private RatingBar ratingBar;
    private String imageUrl;
    private Movie movie;

    private Context context;
    private BaseActivity activity;

    private View frameContainer;
    public DragLayout dragLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (BaseActivity) context;
    }

    public static CommonFragment newInstance(Movie movie) {
        CommonFragment fragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.movie = (Movie) getArguments().getSerializable("movie");
            this.imageUrl =  movie.getPosterPath();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, null);
        dragLayout = (DragLayout) rootView.findViewById(R.id.drag_layout);

        imageView = (ImageView) dragLayout.findViewById(R.id.image);
        Glide.with(context).load(imageUrl).into(imageView);

        frameContainer = dragLayout.findViewById(R.id.frame_container);
        movieTitle = (TextView) dragLayout.findViewById(R.id.movie_title);
        movieDesc = (TextView) dragLayout.findViewById(R.id.movie_desc);
        ratingBar = (RatingBar) dragLayout.findViewById(R.id.rating);
        playBtn = (ImageView) dragLayout.findViewById(R.id.btn_play);
        playBtn.setOnClickListener(this);
        if(movie != null) {
            movieTitle.setText(movie.getTitle());
            movieDesc.setText(movie.getDescription());
            if(movie.getVoteAverage() != null) {
                Float value = (movie.getVoteAverage() / 10) * 5;
                ratingBar.setRating(value);
            }
        }

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) frameContainer.getLayoutParams();
        Double height = Math.ceil(((CONTAINER_ASPECT_RATIO * BaseUtils.getScreenHeight(activity))/100));
        lp.height = height.intValue();
        frameContainer.setLayoutParams(lp);

        dragLayout.setGotoDetailListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(imageView.getDrawable() != null) {
                    Bitmap bm = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
                    activity.onBindImage(bm);
                }
            }
        }, 5);
    }

    @Override
    public void gotoDetail() {
        Activity activity = (Activity) getContext();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(imageView, DetailActivity.IMAGE_TRANSITION_NAME),
                new Pair(movieTitle, DetailActivity.TITLE_TRANSITION_NAME),
                new Pair(movieDesc, DetailActivity.DESC_TRANSITION_NAME),
                new Pair(ratingBar, DetailActivity.RATINGBAR_TRANSITION_NAME)
        );
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, imageUrl);
        intent.putExtra(DetailActivity.EXTRA_MAP, movie);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    public void onClick(View view) {
        if(view == playBtn) {
            Intent intent = new Intent(activity, VideoActivity.class);
            if(movie.getTrailerCode() != null) intent.putExtra("video_key", movie.getTrailerCode());
            intent.putExtra("movieId", String.valueOf(movie.getId()));
            startActivity(intent);
        }
    }
}
