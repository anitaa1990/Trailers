package com.an.trailers.views.menu;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.an.trailers.R;
import com.an.trailers.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;


public class ViewAnimator<T extends ViewAnimator.Resourceable> {
    private final int ANIMATION_DURATION = 175;
    public static final int CIRCULAR_REVEAL_ANIMATION_DURATION = 500;

    private final double ASPECT_RATIO_WIDTH = 11.1;
    private final double ASPECT_RATIO_HEIGHT = 6.756;

    private final double ASPECT_RATIO_CONTAINER_WIDTH = 25.8889;
    private final double ASPECT_RATIO_CONTAINER_HEIGHT = 15.9;

    private AppCompatActivity appCompatActivity;

    private List<T> list;

    private List<View> viewList = new ArrayList<>();
    private ScreenShotable screenShotable;
    private DrawerLayout drawerLayout;
    private ViewAnimatorListener animatorListener;

    private int screenWidth;
    private int screenHeight;

    public ViewAnimator(AppCompatActivity activity,
                        List<T> items,
                        ScreenShotable screenShotable,
                        final DrawerLayout drawerLayout,
                        ViewAnimatorListener animatorListener) {
        this.appCompatActivity = activity;

        this.list = items;
        this.screenShotable = screenShotable;
        this.drawerLayout = drawerLayout;
        this.animatorListener = animatorListener;
        this.screenWidth = BaseUtils.getScreenWidth(activity);
        this.screenHeight = BaseUtils.getScreenHeight(activity);
    }


    public void showMenuContent() {
        setViewsClickable(false);
        viewList.clear();
        double size = list.size();
        for (int i = 0; i < size; i++) {
            final View viewMenu = appCompatActivity.getLayoutInflater().inflate(R.layout.menu_list_item, null);

            final int finalI = i;
            viewMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] location = {0, 0};
                    v.getLocationOnScreen(location);
                    switchItem(list.get(finalI), location[1] + v.getHeight() / 2);
                }
            });

            ImageView iv = ((ImageView) viewMenu.findViewById(R.id.menu_item_image));

            ViewGroup.LayoutParams lp = iv.getLayoutParams();
            Double width = Math.ceil((ASPECT_RATIO_WIDTH * screenWidth)/100);
            Double height = Math.ceil((ASPECT_RATIO_HEIGHT * screenHeight)/100);
            lp.width = width.intValue();
            lp.height = height.intValue();
            iv.setLayoutParams(lp);

            iv.setImageResource(list.get(i).getImageRes());

            View container = viewMenu.findViewById(R.id.menu_item_container);
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            container.setLayoutParams(layoutParams);
//            Double containerWidth = Math.ceil((ASPECT_RATIO_CONTAINER_WIDTH * screenWidth)/100);
            Double containerHeight = Math.ceil((ASPECT_RATIO_CONTAINER_HEIGHT * screenHeight)/100);
//            layoutParams.width = containerWidth.intValue();
            layoutParams.height = containerHeight.intValue();
            container.setLayoutParams(layoutParams);

            viewMenu.setVisibility(View.GONE);
            viewMenu.setEnabled(true);
            viewList.add(viewMenu);
            animatorListener.addViewToContainer(viewMenu);
            final double position = i;
            final double delay = 3 * ANIMATION_DURATION * (position / size);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (position < viewList.size()) {
                        animateView((int) position);
                    }
                }
            }, (long) delay);
        }
    }

    private void hideMenuContent() {
        setViewsClickable(false);
        double size = list.size();
        for (int i = list.size(); i >= 0; i--) {
            final double position = i;
            final double delay = 3 * ANIMATION_DURATION * (position / size);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (position < viewList.size()) {
                        animateHideView((int) position);
                    }
                }
            }, (long) delay);
        }

    }

    private void setViewsClickable(boolean clickable) {
        animatorListener.disableHomeButton();
        for (View view : viewList) {
            view.setEnabled(clickable);
        }
    }

    private void animateView(int position) {
        final View view = viewList.get(position);
        view.setVisibility(View.VISIBLE);
        FlipAnimation rotation =
                new FlipAnimation(90, 0, 0.0f, view.getHeight() / 2.0f);
        rotation.setDuration(ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(rotation);
    }

    private void animateHideView(final int position) {
        final View view = viewList.get(position);
        FlipAnimation rotation =
                new FlipAnimation(0, 90, 0.0f, view.getHeight() / 2.0f);
        rotation.setDuration(ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
                if (position == viewList.size() - 1) {
                    animatorListener.enableHomeButton();
                    drawerLayout.closeDrawers();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(rotation);
    }

    private void switchItem(Resourceable slideMenuItem, int topPosition) {
        this.screenShotable = animatorListener.onSwitch(slideMenuItem, screenShotable, topPosition);
        hideMenuContent();
    }

    public interface ViewAnimatorListener {

        public ScreenShotable onSwitch(Resourceable slideMenuItem, ScreenShotable screenShotable, int position);

        public void disableHomeButton();

        public void enableHomeButton();

        public void addViewToContainer(View view);

    }

    public interface ScreenShotable {
        public void bindImage(Bitmap loadedImage);
    }

    public interface Resourceable {
        public int getImageRes();
        public String getName();
    }
}