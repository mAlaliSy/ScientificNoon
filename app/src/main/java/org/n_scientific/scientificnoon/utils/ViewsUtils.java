package org.n_scientific.scientificnoon.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import org.n_scientific.scientificnoon.R;

/**
 * Created by mohammad on 16/07/17.
 */

public final class ViewsUtils {

    private ViewsUtils() {
    }

    public static Snackbar getErrorSnackBar(View view, String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(ResourcesUtils.getColor(view.getContext(), R.color.red_accent_200));

        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.setActionTextColor(ResourcesUtils.getColor(view.getContext(), R.color.material_red_900));


        return snackbar;

    }

    public static Snackbar getSuccessSnackBar(View view, String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(ResourcesUtils.getColor(view.getContext(), R.color.green_accent_700));

        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.setActionTextColor(ResourcesUtils.getColor(view.getContext(), R.color.material_green_900));


        return snackbar;
    }

    public static Snackbar getInfoSnackBar(View view, String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(ResourcesUtils.getColor(view.getContext(), R.color.light_blue_accent_400));

        snackbar.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.setActionTextColor(ResourcesUtils.getColor(view.getContext(), R.color.material_blue_900));


        return snackbar;
    }
}
