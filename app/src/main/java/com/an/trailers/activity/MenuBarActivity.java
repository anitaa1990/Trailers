package com.an.trailers.activity;

import android.view.View;
import com.an.trailers.utils.BaseUtils;
import com.an.trailers.views.menu.SlideMenuItem;
import com.an.trailers.views.menu.ViewAnimator;

import java.util.List;


public class MenuBarActivity extends ContentFragmentActivity implements ViewAnimator.ViewAnimatorListener {

    private ViewAnimator viewAnimator;
    protected void setUpMenu() {
        List<SlideMenuItem> slideMenuItems = BaseUtils.getMenuList(getApplicationContext());
        viewAnimator = new ViewAnimator(this, slideMenuItems, getCurrentFragment(), getMainActivityBinding().drawerLayout, this);
    }


    @Override
    public ViewAnimator.ScreenShotable onSwitch(int selectedPosition, ViewAnimator.ScreenShotable screenShotable, int position) {
        if(selectedPosition != 4) return replaceFragment(selectedPosition, position);
        return screenShotable;
    }


    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void addViewToContainer(View view) {
        getMainActivityBinding().leftDrawer.addView(view);
    }


    protected void showMenuContent() {
        viewAnimator.showMenuContent();
    }
}
