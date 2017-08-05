package org.n_scientific.scientificnoon.utils;

import android.animation.Animator;
import android.view.View;

/**
 * Created by mohammad on 12/07/17.
 */

public class AnimUtils {

    public static void slideDown(final View view, int duration) {
        view.setTranslationY(-view.getHeight());
        view.setAlpha(0);

        view.animate().setDuration(duration).alpha(1).translationY(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setAlpha(1);
                        view.setTranslationY(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    public static void bounce(final View view, final int duration, final int distance) {

        view.animate()
                .translationY(distance)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    int dist = distance;

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dist *= -1;
                        view.animate()
                                .translationY(dist)
                                .setDuration(duration)
                                .setListener(this)
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();


    }

    public static void fadeIn(final View view, final int duration, final AnimationEndListener listener) {
        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setAlpha(1);
                        if (listener != null)
                            listener.onAnimationEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    public static void fadeIn(final View view, final int duration) {
        fadeIn(view, duration, null);
    }

    public static void fadeOut(final View view, final int duration, final AnimationEndListener listener) {
        view.setAlpha(1);
        view.animate()
                .alpha(0)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                        if (listener != null)
                            listener.onAnimationEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    public static void fadeOut(final View view, final int duration) {
        fadeOut(view, duration, null);
    }


    public interface AnimationEndListener {
        void onAnimationEnd();
    }


}
