package com.an.trailers.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.an.trailers.R;
import com.an.trailers.databinding.FragmentCommonBinding;
import com.an.trailers.model.Movie;
import com.an.trailers.utils.BaseUtils;
import com.an.trailers.utils.NavigationUtils;
import com.an.trailers.views.DragLayout;
import com.bumptech.glide.Glide;

public class CommonFragment extends BaseFragment implements DragLayout.GotoDetailListener, View.OnClickListener {


    private Movie movie;
    private FragmentCommonBinding fragmentCommonBinding;
    private final double CONTAINER_ASPECT_RATIO = 54.05;

    public static CommonFragment newInstance(Movie movie) {
        CommonFragment fragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putSerializable(INTENT_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.movie = (Movie) getArguments().getSerializable(INTENT_MOVIE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCommonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_common, container, false);

        View view = fragmentCommonBinding.getRoot();
        initializeViews();

        return view;
    }


    private void initializeViews() {
        fragmentCommonBinding.setMovie(movie);

        Glide.with(getContext()).load(movie.getPosterPath()).into(fragmentCommonBinding.image);
        fragmentCommonBinding.dragLayout.setGotoDetailListener(this);
        fragmentCommonBinding.btnPlay.setOnClickListener(this);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fragmentCommonBinding.frameContainer.getLayoutParams();
        Double height = Math.ceil(((CONTAINER_ASPECT_RATIO * BaseUtils.getScreenHeight(getContext())) / 100));
        lp.height = height.intValue();
        fragmentCommonBinding.frameContainer.setLayoutParams(lp);
    }


    @Override
    public void gotoDetail() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                new Pair(fragmentCommonBinding.image, TRANSITION_IMAGE_NAME),
                new Pair(fragmentCommonBinding.movieTitle, TRANSITION_TITLE_NAME),
                new Pair(fragmentCommonBinding.movieDesc, TRANSITION_DESC_NAME)
        );
        NavigationUtils.redirectToDetailScreen(mActivity, getMovie(), options);
    }


    @Override
    public void onClick(View view) {
        NavigationUtils.redirectToVideoScreen(mActivity, getMovie());
    }


    public void expandLayout() {
        if(fragmentCommonBinding!= null && fragmentCommonBinding.dragLayout != null) {
            fragmentCommonBinding.dragLayout.setStateExpanded();
        }
    }


    public void collapseLayout() {
        if(fragmentCommonBinding!= null && fragmentCommonBinding.dragLayout != null) {
            fragmentCommonBinding.dragLayout.setStateClose();
        }
    }

    public Movie getMovie() {
        return movie;
    }
}
