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
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.an.trailers.R;


public class CustomEditText extends android.widget.EditText {

    public CustomEditText(Context context) {
        super(context);
        setTypeFace(1);
    }

    public CustomEditText(Context context, AttributeSet attrs) {

        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomEditText);
        final int fontValue = a.getInt(R.styleable.CustomEditText_font, 0);
        a.recycle();
        setTypeFace(fontValue);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomEditText);
        final int fontValue = a.getInt(R.styleable.CustomEditText_font, 0);
        a.recycle();
        setTypeFace(fontValue);
    }

    public void setTypeFace(int fontValue) {
        Typeface myTypeFace;

        if (fontValue == 4) {
            myTypeFace = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/gt_medium.otf");
            this.setTypeface(myTypeFace);
        }
    }
}