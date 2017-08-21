package org.n_scientific.scientificnoon.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.TypedValue;

import java.util.Locale;

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

    public static void changeLanguageToArabic(Context context) {
        // Change the language to Arabic => Use RTL instead of LTR..
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, null);

    }

}
