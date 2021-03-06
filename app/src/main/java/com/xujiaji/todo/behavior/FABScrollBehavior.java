package com.xujiaji.todo.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * author: xujiaji
 * created on: 2018/8/8 0:13
 * description:
 */
public class FABScrollBehavior extends FloatingActionButton.Behavior {
    public FABScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            this.updateSnackbar(child, (Snackbar.SnackbarLayout) dependency);
        }
        return super.layoutDependsOn(parent, child, dependency);

    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // ensure vertical scroll
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    boolean hide = false;

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
//        Log.e("onNestedPreScroll", "dy = " + dy);
//        child.setTranslationY(Math.max(0.0f, Math.min(child.getHeight() * 2, child.getTranslationY() + dy)));
        if (dy > 0) { // down
            if (hide) return;
            hideFab(child);
            hide = true;
        } else {
            if (!hide) return;
            showFab(child);
            hide = false;
        }
    }

    private void updateSnackbar(FloatingActionButton child, Snackbar.SnackbarLayout snackbarLayout) {
        if (snackbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            android.view.ViewGroup.LayoutParams layoutParams = snackbarLayout.getLayoutParams();
            if (layoutParams == null) {
                throw new RuntimeException("null cannot be cast to non-null type android.support.design.widget.CoordinatorLayout.LayoutParams");
            }

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) layoutParams;
            params.setAnchorId(child.getId());
            params.anchorGravity = Gravity.TOP;
            params.gravity = Gravity.TOP;
            snackbarLayout.setLayoutParams(params);
        }

    }



    private void hideFab(View fab) {
        int moveY = 2 * fab.getHeight();
        fab.animate()
                .translationY(moveY)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    private void showFab(View fab) {
        fab.animate()
                .translationY(0)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }
}
