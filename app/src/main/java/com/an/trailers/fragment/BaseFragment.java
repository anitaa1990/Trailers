package com.an.trailers.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.View;

import com.an.trailers.Constants;
import com.an.trailers.activity.MainActivity;
import com.an.trailers.views.menu.ViewAnimator;

public class BaseFragment extends Fragment implements ViewAnimator.ScreenShotable, Constants {

    protected MainActivity activity;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    protected void bindBackgroundImage(Bitmap loadedImage, View containerView, final View overlayView) {
//        containerView.setBackground(new BitmapDrawable(getResources(), loadedImage));
//        Palette.from(loadedImage).generate(p -> {
//            overlayView.setAlpha(0.7f);
//            overlayView.setBackgroundColor(p.getDominantColor(Color.parseColor("#B07986CB")));
//        });
    }

    @Override
    public void bindImage(Bitmap loadedImage) {

    }
}
