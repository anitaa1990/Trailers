package com.an.trailers.views;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import com.an.trailers.adapter.CommonPagerAdapter;
import com.an.trailers.fragment.CommonFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Custom Image Switcher for display and change background images with some pretty animations.
 * Uses different drawing orders for animation purposes.
 */
public class BackgroundSwitcherView extends ImageSwitcher {
    private final int[] REVERSE_ORDER = new int[]{1, 0};
    private final int[] NORMAL_ORDER = new int[]{0, 1};

    private boolean reverseDrawOrder;

    private int position;
    private int bgImageGap;
    private int bgImageWidth;

    private int alphaDuration = 400;
    private int movementDuration = 500;
    private int widthBackgroundImageGapPercent = 12;

    private Animation bgImageInLeftAnimation;
    private Animation bgImageOutLeftAnimation;

    private Animation bgImageInRightAnimation;
    private Animation bgImageOutRightAnimation;

    private AnimationDirection currentAnimationDirection;

    public BackgroundSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateAndInit(context);
    }

    public BackgroundSwitcherView(Context context) {
        super(context);
        inflateAndInit(context);
    }

    private void inflateAndInit(final Context context) {
        setChildrenDrawingOrderEnabled(true);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        bgImageGap = (displayMetrics.widthPixels / 100) * widthBackgroundImageGapPercent;
        bgImageWidth = displayMetrics.widthPixels + bgImageGap * 2;

        this.setFactory(() -> {
            ImageView myView = new ImageView(context);
            myView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            myView.setLayoutParams(new LayoutParams(bgImageWidth, LayoutParams.MATCH_PARENT));
            myView.setTranslationX(-bgImageGap);
            return myView;
        });

        bgImageInLeftAnimation = createBgImageInAnimation(bgImageGap, 0, movementDuration, alphaDuration);
        bgImageOutLeftAnimation = createBgImageOutAnimation(0, -bgImageGap, movementDuration);
        bgImageInRightAnimation = createBgImageInAnimation(-bgImageGap, 0, movementDuration, alphaDuration);
        bgImageOutRightAnimation = createBgImageOutAnimation(0, bgImageGap, movementDuration);
    }


    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (reverseDrawOrder)
            return REVERSE_ORDER[i];
        else
            return NORMAL_ORDER[i];
    }

    public void setReverseDrawOrder(boolean reverseDrawOrder) {
        this.reverseDrawOrder = reverseDrawOrder;
    }

    private synchronized void setImageBitmapWithAnimation(Bitmap newBitmap, AnimationDirection animationDirection) {
        if (animationDirection == AnimationDirection.LEFT) {
            this.setInAnimation(bgImageInLeftAnimation);
            this.setOutAnimation(bgImageOutLeftAnimation);
            this.setImageBitmap(newBitmap);

        } else if (animationDirection == AnimationDirection.RIGHT) {
            this.setInAnimation(bgImageInRightAnimation);
            this.setOutAnimation(bgImageOutRightAnimation);
            this.setImageBitmap(newBitmap);
        }
    }



    public void updateCurrentBackground(final int position,
                                        final CustomViewPager pager,
                                        final AnimationDirection direction) {

        this.position = position;
        this.currentAnimationDirection = direction;
        String imageUrl = getPoster(position, pager);

        ImageView image = (ImageView) this.getNextView();
        image.setImageDrawable(null);
        showNext();

        Picasso.get()
                .load(imageUrl)
                .noFade()
                .noPlaceholder()
                .into(target);
    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setImageBitmapWithAnimation(bitmap, currentAnimationDirection);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            e.printStackTrace();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    private String getPoster(int position,
                             final CustomViewPager pager) {

        List<CommonFragment> commonFragments = ((CommonPagerAdapter)pager.getAdapter()).getFragments();
        CommonFragment currentFragment = commonFragments.get(position);
        return currentFragment.getMovie().getPosterPath();
    }


    private void setImageBitmap(Bitmap bitmap) {
        ImageView image = (ImageView) this.getNextView();
        image.setImageDrawable(null);

        int duration = position == 0 ? 0 : 1000;
        animate().alpha(0.0f).setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                image.setImageBitmap(bitmap);
                new Handler().postDelayed(() -> animate().alpha(0.4f).setDuration(duration), 200);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        showNext();
    }

    private Animation createBgImageInAnimation(int fromX, int toX, int transitionDuration, int alphaDuration) {
        TranslateAnimation translate = new TranslateAnimation(fromX, toX, 0, 0);
        translate.setDuration(transitionDuration);

//        AlphaAnimation alpha = new AlphaAnimation(0F, 0.1F);
//        alpha.setDuration(alphaDuration);

        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(translate);
//        set.addAnimation(alpha);
        return set;
    }

    private Animation createBgImageOutAnimation(int fromX, int toX, int transitionDuration) {
        TranslateAnimation ta = new TranslateAnimation(fromX, toX, 0, 0);
        ta.setDuration(transitionDuration);
        ta.setInterpolator(new DecelerateInterpolator());
        return ta;
    }

    public enum AnimationDirection {
        LEFT, RIGHT
    }
}