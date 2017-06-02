package org.n_scientific.scientificnoon.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;

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


}
