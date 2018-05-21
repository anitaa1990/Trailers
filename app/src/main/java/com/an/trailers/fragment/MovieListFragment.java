package com.an.trailers.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.an.trailers.R;
import com.an.trailers.activity.MovieListActivity;
import com.an.trailers.activity.NavigationBarActivity;
import com.an.trailers.adapter.CommonPagerAdapter;
import com.an.trailers.callback.OnPageSelectedListener;
import com.an.trailers.databinding.HomeFragmentBinding;
import com.an.trailers.views.CustomerPagerTransformer;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MovieListFragment extends BaseFragment implements Observer, OnPageSelectedListener {

    private int position;
    private HomeFragmentBinding homeFragmentBinding;

    public static MovieListFragment newInstance(int position) {
        MovieListFragment contentFragment = new MovieListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(String.class.getName(), position);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            position = getArguments().getInt(String.class.getName());
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = homeFragmentBinding.getRoot();

        setUpViewPager();
        displayLoader();
        fetchData();

        return view;
    }


    private void fetchData() {
        ((MovieListActivity)mActivity).initializeViewModel();
        setUpObserver(((MovieListActivity)mActivity).getMovieListViewModel());
        ((MovieListActivity)mActivity).getMovieListViewModel().fetchData(position);
    }


    private void setUpViewPager() {
        homeFragmentBinding.includedSimilarLayout.viewpager.setPageTransformer(false, new CustomerPagerTransformer(mActivity));
    }


    private void displayLoader() {
        ((NavigationBarActivity)mActivity).displayLoader();
        homeFragmentBinding.includedSimilarLayout.progressView.setVisibility(View.VISIBLE);
    }


    private void hideLoader() {
        ((NavigationBarActivity)mActivity).hideLoader();
        homeFragmentBinding.includedSimilarLayout.progressView.setVisibility(View.GONE);
    }


    private void setUpObserver(Observable observable) {
        observable.addObserver(this);
    }


    @Override
    public void onPageSelected(int position) {
        ((MovieListActivity)mActivity).getMovieListViewModel().updateData(homeFragmentBinding.includedSimilarLayout.viewpager.getCurrentItem(),
                homeFragmentBinding.includedSimilarLayout.viewpager.getAdapter().getCount());
    }


    @Override
    public void update(Observable observable, Object o) {
        hideLoader();
        if(o instanceof List<?>) {
            List<CommonFragment> fragments = (List<CommonFragment>) o;
            CommonPagerAdapter commonPagerAdapter = (CommonPagerAdapter) homeFragmentBinding.includedSimilarLayout.viewpager.getAdapter();
            if(commonPagerAdapter == null) {
                commonPagerAdapter = new CommonPagerAdapter(getChildFragmentManager(), fragments);
                homeFragmentBinding.includedSimilarLayout.viewpager.setAdapter(commonPagerAdapter);
                homeFragmentBinding.includedSimilarLayout.viewpager.addOnPageSelectedListener(this);
                homeFragmentBinding.includedSimilarLayout.viewpager.addOnBackgroundSwitchView(homeFragmentBinding.includedSimilarLayout.overlay);

            } else if(fragments.size() == 0 && commonPagerAdapter.getCount() == 0) {
                homeFragmentBinding.includedSimilarLayout.emptyContainer.setVisibility(View.VISIBLE);
                homeFragmentBinding.includedSimilarLayout.viewpager.setVisibility(View.GONE);

            } else {
                commonPagerAdapter.setFragments(fragments);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mActivity != null) {
            ((MovieListActivity) mActivity).getMovieListViewModel().reset();
        }
    }
}
