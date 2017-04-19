package com.an.trailers.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.WindowManager;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.adapter.CommonPagerAdapter;
import com.an.trailers.fragment.CommonFragment;
import com.an.trailers.model.Movie;
import com.an.trailers.utils.BaseUtils;
import com.an.trailers.views.CustPagerTransformer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends BaseActivity implements Constants, View.OnClickListener {

    private View positionView;
    private ViewPager viewPager;
    private ViewPager viewPagerBackground;
    private View overlayView;
    private View containerView;
    private CommonPagerAdapter adapter;

    private SearchView searchView;
    private View searchIcon;
    private View favIcon;

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
        searchIcon.setOnClickListener(this);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setVisibility(View.GONE);
        favIcon = findViewById(R.id.fav_icon);
        favIcon.setVisibility(View.GONE);

        dealStatusBar(positionView);
        fillViewPager();
    }


    private void fillViewPager() {
        containerView = findViewById(R.id.fr_container);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerBackground = (ViewPager) findViewById(R.id.viewPagerBackground);

        viewPager.setPageTransformer(false, new CustPagerTransformer(this));

        List<CommonFragment> fragments = new ArrayList<>();
        final List<Movie> movies = BaseUtils.getFavMovies();

        for (Movie movie : movies) {
            if(movie.getPosterPath() != null)
                fragments.add(CommonFragment.newInstance(movie));
        }
        adapter = new CommonPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int index = 0;
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                if(movies.isEmpty()) return;
                String imageUrl = String.format(Constants.IMAGE_URL, movies.get(position).getPosterPath());
                Glide.with(FavActivity.this).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap loadedImage, GlideAnimation<? super Bitmap> glideAnimation) {
                        bindImage(loadedImage, containerView, overlayView);
                    }
                });
            }

            @Override
            public void onPageSelected(int position) {
                index = position;
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
    public void onBindImage(Bitmap bm) {
        bindImage(bm, containerView, overlayView);
    }

    @Override
    public void onClick(View view) {
        if(view == searchIcon) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }
    }
}
