package com.an.trailers.fragment;


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

public abstract class BaseFragment extends Fragment implements ViewAnimator.ScreenShotable, Constants {

    protected MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void bindBackgroundImage(Bitmap loadedImage, View containerView, final View overlayView) {
        containerView.setBackground(new BitmapDrawable(getResources(), loadedImage));
        Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                overlayView.setAlpha(0.7f);
                overlayView.setBackgroundColor(p.getDominantColor(Color.parseColor("#B07986CB")));
            }
        });
    }
}
