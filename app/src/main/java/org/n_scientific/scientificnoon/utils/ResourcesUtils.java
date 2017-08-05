package org.n_scientific.scientificnoon.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.TypedValue;

/**
 * Created by mohammad on 02/06/17.
 */

public class ResourcesUtils {

    public static int getColor(Context context, @ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resId, context.getTheme());
        } else {
            return context.getResources().getColor(resId);
        }
    }


    public static int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


}
