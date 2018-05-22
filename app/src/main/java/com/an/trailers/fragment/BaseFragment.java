package com.an.trailers.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import com.an.trailers.Constants;
import com.an.trailers.views.menu.ViewAnimator;

public class BaseFragment extends Fragment implements ViewAnimator.ScreenShotable, Constants {

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

    @Override
    public void bindImage(Bitmap loadedImage) {

    }
}
