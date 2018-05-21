package com.an.trailers.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.an.trailers.Constants;
import com.an.trailers.R;
import com.an.trailers.fragment.BaseFragment;
import com.an.trailers.fragment.ContentFragment;
import com.an.trailers.views.menu.SlideMenuItem;
import com.an.trailers.views.menu.ViewAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


public class MainActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener, View.OnClickListener {
    private DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private BaseFragment fragment;
    private ViewAnimator viewAnimator;
    private LinearLayout linearLayout;

    private SearchView searchView;
    public View searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = ContentFragment.newInstance(Constants.MOVIES_UPCOMING_PATH);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        searchIcon = findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(this);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setVisibility(View.GONE);
        slideItem = getString(R.string.upcoming);

        setActionBar();
        createMenuList();

        viewAnimator = new ViewAnimator<>(this, list, fragment, drawerLayout, this);
    }

    private void createMenuList() {
        List<String> menuTitles = Arrays.asList(getResources().getStringArray(R.array.menu_names));
        TypedArray menuIcons = getResources().obtainTypedArray(R.array.menu_icons);

        for(int i = 0; i< menuTitles.size(); i++) {
            SlideMenuItem slideMenuItem = new SlideMenuItem(menuTitles.get(i), menuIcons.getResourceId(i, -1));
            list.add(slideMenuItem);
        }
        menuIcons.recycle();
    }


    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ViewAnimator.ScreenShotable replaceFragment(ViewAnimator.Resourceable slideMenuItem,
                                                        String url, int topPosition) {
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        animator.start();
        fragment = ContentFragment.newInstance(url, slideMenuItem.getName());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        return fragment;
    }


    private String slideItem;

    public ViewAnimator.ScreenShotable onSwitch(ViewAnimator.Resourceable slideMenuItem,
                                                ViewAnimator.ScreenShotable screenShotable,
                                                int position) {
        if(slideMenuItem.getName().equalsIgnoreCase(slideItem)) return screenShotable;
        this.slideItem = slideMenuItem.getName();
        if(slideMenuItem.getName().equalsIgnoreCase(getString(R.string.drawer_close))) {
            return screenShotable;
        } else if(slideMenuItem.getName().equalsIgnoreCase(getString(R.string.upcoming))) {
            return replaceFragment(slideMenuItem, Constants.MOVIES_UPCOMING_PATH, position);
        } else if(slideMenuItem.getName().equalsIgnoreCase(getString(R.string.now_playing))) {
            return replaceFragment(slideMenuItem, Constants.MOVIES_NOW_PLAYING_PATH, position);
        } else if(slideMenuItem.getName().equalsIgnoreCase(getString(R.string.top_rated))) {
            return replaceFragment(slideMenuItem, Constants.MOVIES_TOP_RATED_PATH, position);
        } else if(slideMenuItem.getName().equalsIgnoreCase(getString(R.string.dvd))) {
            return replaceFragment(slideMenuItem, Constants.MOVIE_DVD_PATH, position);
        } else {
            return replaceFragment(slideMenuItem, null, position);
        }
    }

    @Override
    public ViewAnimator.ScreenShotable onSwitch(int selectedPosition, ViewAnimator.ScreenShotable screenShotable, int position) {
        return null;
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    @Override
    public void onClick(View view) {
        if(view == searchIcon) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBindImage(Bitmap bm) {
        fragment.bindImage(bm);
    }
}
