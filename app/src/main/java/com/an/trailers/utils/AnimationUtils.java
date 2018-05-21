package com.an.trailers.utils;

import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageSwitcher;

public class AnimationUtils {


    public static void animateView(View view) {
        long animationDelay = 200;

        view.setScaleY(0);
        view.setScaleX(0);
        view.animate()
                .scaleY(1)
                .scaleX(1)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(null)
                .setStartDelay(animationDelay)
                .start();
    }


    public static void fadeIn(ImageSwitcher view) {

//        TransitionSet set = new TransitionSet();
//        set.addTransition(new Fade())
//                .addTransition(new Slide(Gravity.BOTTOM))
//        .setInterpolator(new BounceInterpolator()).setDuration(6000);
//        TransitionManager.beginDelayedTransition((ViewGroup) view, set);

//        long animationDelay = 200;
//        view.setScaleX(1f);
//        view.setScaleY(1f);
//        view.animate()
//                .scaleX(1.1f)
//                .scaleY(1.1f)
//                .translationX(10)
//                .setInterpolator(new DecelerateInterpolator())
//                .setListener(null)
//                .setStartDelay(animationDelay)
//                .start();


        Animation bgImageInLeftAnimation = createBgImageInAnimation(12, 0, 500, 400);
        Animation bgImageOutLeftAnimation = createBgImageOutAnimation(0, -12, 500);
        view.setInAnimation(bgImageInLeftAnimation);
        view.setOutAnimation(bgImageOutLeftAnimation);
    }


    private static Animation createBgImageInAnimation(int fromX, int toX, int transitionDuration, int alphaDuration) {
        TranslateAnimation translate = new TranslateAnimation(fromX, toX, 0, 0);
        translate.setDuration(transitionDuration);

        AlphaAnimation alpha = new AlphaAnimation(0F, 1F);
        alpha.setDuration(alphaDuration);

        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(translate);
        set.addAnimation(alpha);
        return set;
    }

    private static Animation createBgImageOutAnimation(int fromX, int toX, int transitionDuration) {
        TranslateAnimation ta = new TranslateAnimation(fromX, toX, 0, 0);
        ta.setDuration(transitionDuration);
        ta.setInterpolator(new DecelerateInterpolator());
        return ta;
    }
}
