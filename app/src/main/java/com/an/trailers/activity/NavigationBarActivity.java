package com.an.trailers.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.an.trailers.R;

import java.lang.reflect.Field;

public class NavigationBarActivity extends MenuBarActivity {

    private ActionBarDrawerToggle drawerToggle;

    protected void setUpToolbar() {
//        setUpStatusBar(getMainActivityBinding().includedLayout.positionView);
        getMainActivityBinding().includedLayout.search.setVisibility(View.GONE);
        setSupportActionBar(getMainActivityBinding().includedLayout.toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    protected void setUpNavigationDrawer() {
        getMainActivityBinding().drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerToggle = new ActionBarDrawerToggle(
                this, getMainActivityBinding().drawerLayout,
                getMainActivityBinding().includedLayout.toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getMainActivityBinding().leftDrawer.removeAllViews();
                getMainActivityBinding().leftDrawer.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && getMainActivityBinding().leftDrawer.getChildCount() == 0) {
                    showMenuContent();
                }
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        getMainActivityBinding().drawerLayout.addDrawerListener(drawerToggle);
    }



    protected void setUpStatusBar(View positionView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);
        }
    }


    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    public void handleLeftDrawerAction(View view) {
        getMainActivityBinding().drawerLayout.closeDrawers();
    }


    public void displayLoader() {
        drawerToggle.setDrawerIndicatorEnabled(false);
        getMainActivityBinding().includedLayout.searchIcon.setVisibility(View.GONE);
    }


    public void hideLoader() {
        drawerToggle.setDrawerIndicatorEnabled(true);
        getMainActivityBinding().includedLayout.searchIcon.setVisibility(View.VISIBLE);
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
}
