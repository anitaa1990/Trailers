package com.an.trailers.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.adapter.CommonPagerAdapter;
import com.an.trailers.callback.MovieResponseListener;
import com.an.trailers.fragment.CommonFragment;
import com.an.trailers.model.Movie;
import com.an.trailers.service.RESTExecutorService;
import com.an.trailers.service.VolleyTask;
import com.an.trailers.views.CustPagerTransformer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements MovieResponseListener, Constants {

    private View positionView;
    private ViewPager viewPager;
    private ViewPager viewPagerBackground;
    private View overlayView;
    private View containerView;
    private CommonPagerAdapter adapter;

    private SearchView searchView;
    private View searchIcon;

    private int currentPage = 1;
    private long totalPages;
    private int pos = 0;
    private List<CommonFragment> fragments = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        positionView = findViewById(R.id.position_view);
        overlayView = findViewById(R.id.overlay);
        searchIcon = findViewById(R.id.search_icon);
        searchIcon.setVisibility(View.GONE);
        searchView = (SearchView) findViewById(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setIconifiedByDefault(false);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(android.R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/gt_medium.otf");
        searchEditText.setTypeface(myCustomFont);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!movies.isEmpty()) movies.clear();
                if(adapter != null) adapter.removeFragments();
                pos = 0;
                RESTExecutorService.submit(new VolleyTask(SearchActivity.this, METHOD_SEARCH, query, SearchActivity.this));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }
        });

        dealStatusBar(positionView);
        fillViewPager();
    }


    private void fillViewPager() {
        containerView = findViewById(R.id.fr_container);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerBackground = (ViewPager) findViewById(R.id.viewPagerBackground);

        viewPager.setPageTransformer(false, new CustPagerTransformer(this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int index = 0;

            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                if(movies.isEmpty()) return;
                if(pos > 0) return;
                String imageUrl = String.format(Constants.IMAGE_URL, movies.get(position).getPosterPath());
                Glide.with(SearchActivity.this).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap loadedImage, GlideAnimation<? super Bitmap> glideAnimation) {
                        bindImage(loadedImage, containerView, overlayView);
                        pos++;
                    }
                });
            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                if(currentPage <= totalPages && (position+2) == adapter.getCount()) {
                    RESTExecutorService.submit(new VolleyTask(SearchActivity.this, String.valueOf(currentPage), SearchActivity.this));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    viewPagerBackground.setCurrentItem(index);
                }
            }
        });
    }

    @Override
    public void onMoviesResponse(List<Movie> movies, int currentPage, long totalPages) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        this.movies.addAll(movies);
        this.totalPages = totalPages;
        this.currentPage = currentPage + 1;
        if(this.movies.size() == movies.size()) {
            handleFirstMovieResponse();
        } else handleMovieRepsonse(movies);
    }

    private void handleFirstMovieResponse() {
        for (Movie movie : movies) {
            if(movie.getPosterPath() != null)
                fragments.add(CommonFragment.newInstance(movie));
        }
        adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    private void handleMovieRepsonse(List<Movie> movieList) {
        for (Movie movie : movieList) {
            if(movie.getPosterPath() != null)
                adapter.addFragment(CommonFragment.newInstance(movie));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBindImage(Bitmap bm) {
        bindImage(bm, containerView, overlayView);
    }
}
