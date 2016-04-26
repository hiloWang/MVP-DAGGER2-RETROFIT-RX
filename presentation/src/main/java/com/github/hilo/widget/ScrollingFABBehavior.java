package com.github.hilo.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * 随可滑动内容隐藏显示的 FAB
 */
public class ScrollingFABBehavior extends FloatingActionButton.Behavior {

    private static final String TAG = "ReverseScrollingFABBehavior";

    private int mStartAppBarBottom;
    private AppBarLayout mAppBarLayout;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super();
    }


    public boolean onStartNestedScroll(CoordinatorLayout parent,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            mAppBarLayout = (AppBarLayout) dependency;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child, View target, int dxConsumed,
                               int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        maybeInitProperties();
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed);
        if (mStartAppBarBottom != mAppBarLayout.getBottom()) {
            child.hide();
        } else {
            child.show();
        }
    }

    private void maybeInitProperties() {
        if (mStartAppBarBottom == 0) {
            mStartAppBarBottom = mAppBarLayout.getBottom();
        }
    }
}