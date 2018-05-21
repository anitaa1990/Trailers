package com.an.trailers.views;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.an.trailers.adapter.CommonPagerAdapter;
import com.an.trailers.callback.OnPageSelectedListener;
import com.an.trailers.fragment.CommonFragment;

import java.util.List;

public class CustomViewPager extends ViewPager {

    private int currentPosition;
    private OnPageChangeListener onPageChangeListener;
    private OnPageSelectedListener onPageSelectedListener;
    private BackgroundSwitcherView backgroundSwitcherView;

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }


    public CustomViewPager(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);

        initiateView();
    }


    private void initiateView() {
        this.setCurrentPosition(0);
        addOnPageChangeListener();
    }


    public void addOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
        updateData(0);
    }

    public void addOnBackgroundSwitchView(BackgroundSwitcherView backgroundSwitcherView) {
        this.backgroundSwitcherView = backgroundSwitcherView;
        updateData(0);
    }


    private void addOnPageChangeListener() {
        onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animatePager(position);
                updateBackgroundImage(position);
                onPageSelectedListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        this.addOnPageChangeListener(onPageChangeListener);
    }


    private void animatePager(int currentPosition) {

        List<CommonFragment> commonFragments = ((CommonPagerAdapter) this.getAdapter()).getFragments();
        CommonFragment currentFragment = commonFragments.get(currentPosition);
        currentFragment.expandLayout();

        if (currentPosition > 0) {
            int previousPosition = currentPosition - 1;
            CommonFragment leftFragment = commonFragments.get(previousPosition);
            leftFragment.collapseLayout();
        }

        if ((currentPosition + 1) != commonFragments.size()) {
            CommonFragment rightFragment = commonFragments.get(currentPosition + 1);
            rightFragment.collapseLayout();
        }
    }


    private void updateBackgroundImage(int position) {

        int oldPosition = this.getCurrentPosition();
        this.setCurrentPosition(position);

        BackgroundSwitcherView.AnimationDirection direction = null;
        if (oldPosition <= position) {
            direction = BackgroundSwitcherView.AnimationDirection.LEFT;

        } else if (oldPosition > position) {
            direction = BackgroundSwitcherView.AnimationDirection.RIGHT;
        }

        backgroundSwitcherView.updateCurrentBackground(position, this, direction);
    }


    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }


    private void updateData(int position) {
        if(onPageSelectedListener == null || backgroundSwitcherView == null || getAdapter() == null || getAdapter().getCount() == 0) {
            return;
        }

        int finalPosition = position;
        new Handler().postDelayed(() -> {
            setCurrentItem(finalPosition);
            onPageChangeListener.onPageSelected(finalPosition);
        },500);
    }

    public void clearBackground() {
        backgroundSwitcherView.clearImage();
    }
}
