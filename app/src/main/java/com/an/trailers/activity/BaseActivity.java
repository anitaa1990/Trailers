package com.an.trailers.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public abstract class BaseActivity extends FragmentActivity {

    public abstract void onBindImage(Bitmap bm);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void dealStatusBar(View positionView) {
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

    protected void bindImage(Bitmap loadedImage, View containerView, final View overlayView) {
        containerView.setBackground(new BitmapDrawable(getResources(), loadedImage));
        Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                overlayView.setAlpha(0.7f);
                overlayView.setBackgroundColor(p.getDominantColor(Color.parseColor("#B07986CB")));
            }
        });
    }
}
