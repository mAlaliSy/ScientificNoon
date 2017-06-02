package org.n_scientific.scientificnoon.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mohammad on 01/06/17.
 */

@SuppressLint("AppCompatCustomView")
public class NoonTextView extends TextView {
    public NoonTextView(Context context) {
        super(context);

        init();
    }

    public NoonTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/jf_flat.ttf"));
    }

}
