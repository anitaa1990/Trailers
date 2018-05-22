package com.an.trailers.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.an.trailers.R;
import com.an.trailers.viewmodel.MovieListViewModel;

public class MovieListActivity extends NavigationBarActivity {

    private MovieListViewModel movieListViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeDataBinding();
        initializeViews();
        initializeViewModel();
        setUpToolbar();
        setUpNavigationDrawer();
        setUpMenu();
    }


    private void initializeDataBinding() {
        setMainActivityBinding(DataBindingUtil.setContentView(this, R.layout.activity_main));
    }

    private void initializeViews() {
        setUpFirstFragment(0);
    }


    public void initializeViewModel() {
        movieListViewModel = new MovieListViewModel(getApplicationContext());
    }


    public void handleSearchIconClick(View view) {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }


    public MovieListViewModel getMovieListViewModel() {
        return movieListViewModel;
    }
}
