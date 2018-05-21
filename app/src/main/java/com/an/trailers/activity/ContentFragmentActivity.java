package com.an.trailers.activity;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import com.an.trailers.R;
import com.an.trailers.databinding.MainActivityBinding;
import com.an.trailers.fragment.MovieListFragment;
import com.an.trailers.views.menu.ViewAnimator;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class ContentFragmentActivity extends AppCompatActivity implements ViewAnimator.ScreenShotable {

    private MainActivityBinding mainActivityBinding;

    protected void setUpFirstFragment(int position) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, MovieListFragment.newInstance(position))
                .commit();
    }

    protected MovieListFragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);
        return (MovieListFragment) currentFragment;
    }


    protected ViewAnimator.ScreenShotable replaceFragment(int selectedPosition, int topPosition) {
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        animator.start();
        MovieListFragment fragment = MovieListFragment.newInstance(selectedPosition);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        return fragment;
    }


    @Override
    public void bindImage(Bitmap loadedImage) {
//        getCurrentFragment().bindImage(loadedImage);
    }


    protected MainActivityBinding getMainActivityBinding() {
        return mainActivityBinding;
    }

    protected void setMainActivityBinding(MainActivityBinding mainActivityBinding) {
        this.mainActivityBinding = mainActivityBinding;
    }
}
