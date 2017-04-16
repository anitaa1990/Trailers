/*
 * Copyright (C) 2013 Mobs and Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file 
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the 
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.an.trailers.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.an.trailers.R;


public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
        setTypeFace(1);
    }

    public CustomTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);
        final int fontValue = a.getInt(R.styleable.CustomTextView_font, 0);
        a.recycle();
        setTypeFace(fontValue);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);
        final int fontValue = a.getInt(R.styleable.CustomTextView_font, 0);
        a.recycle();
        setTypeFace(fontValue);
    }

    public void setTypeFace(int fontValue) {
        Typeface myTypeFace;

        if (fontValue == 1) {
            myTypeFace = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/circular_medium.ttf");
            this.setTypeface(myTypeFace);
        } else if (fontValue == 2) {
            myTypeFace = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/circular_bold.ttf");
            this.setTypeface(myTypeFace);
        } else if (fontValue == 3) {
            myTypeFace = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/circular_book.ttf");
            this.setTypeface(myTypeFace);
        } else if (fontValue == 4) {
            myTypeFace = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/gt_medium.otf");
            this.setTypeface(myTypeFace);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        // Replace the original background drawable (e.g. image) with a LayerDrawable that
        // contains the original drawable.
        SAutoBgTextViewBackgroundDrawable layer = new SAutoBgTextViewBackgroundDrawable(d);
        super.setBackgroundDrawable(layer);
    }

    /**
     * The stateful LayerDrawable used by this button.
     */
    protected class SAutoBgTextViewBackgroundDrawable extends LayerDrawable {

        // The color filter to apply when the button is pressed
        protected ColorFilter _pressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
        // Alpha value when the button is disabled
        protected int _disabledAlpha = 100;

        public SAutoBgTextViewBackgroundDrawable(Drawable d) {
            super(new Drawable[] { d });
        }

        @Override
        protected boolean onStateChange(int[] states) {
            boolean enabled = false;
            boolean pressed = false;

            for (int state : states) {
                if (state == android.R.attr.state_enabled)
                    enabled = true;
                else if (state == android.R.attr.state_pressed)
                    pressed = true;
            }

            mutate();
            if (enabled && pressed) {
                setColorFilter(_pressedFilter);
            } else if (!enabled) {
                setColorFilter(null);
                setAlpha(_disabledAlpha);
            } else {
                setColorFilter(null);
            }

            invalidateSelf();

            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }



//    private int mLineY;
//    private int mViewWidth;
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        TextPaint paint = getPaint();
//        paint.setColor(getCurrentTextColor());
//        paint.drawableState = getDrawableState();
//        mViewWidth = getMeasuredWidth();
//        String text = (String) getText();
//        mLineY = 0;
//        mLineY += getTextSize();
//        Layout layout = getLayout();
//        for (int i = 0; i < layout.getLineCount(); i++) {
//            int lineStart = layout.getLineStart(i);
//            int lineEnd = layout.getLineEnd(i);
//            String line = text.substring(lineStart, lineEnd);
//
//            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
//            if (needScale(line)) {
//                drawScaledText(canvas, lineStart, line, width);
//            } else {
//                canvas.drawText(line, 0, mLineY, paint);
//            }
//
//            mLineY += getLineHeight();
//        }
//    }
//
//    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
//        float x = 0;
//        if (isFirstLineOfParagraph(lineStart, line)) {
//            String blanks = "  ";
//            canvas.drawText(blanks, x, mLineY, getPaint());
//            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
//            x += bw;
//
//            line = line.substring(3);
//        }
//
//        float d = (mViewWidth - lineWidth) / line.length() - 1;
//        for (int i = 0; i < line.length(); i++) {
//            String c = String.valueOf(line.charAt(i));
//            float cw = StaticLayout.getDesiredWidth(c, getPaint());
//            canvas.drawText(c, x, mLineY, getPaint());
//            x += cw + d;
//        }
//    }
//
//    private boolean isFirstLineOfParagraph(int lineStart, String line) {
//        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
//    }
//
//    private boolean needScale(String line) {
//        if (line.length() == 0) {
//            return false;
//        } else {
//            return line.charAt(line.length() - 1) != '\n';
//        }
//    }
}