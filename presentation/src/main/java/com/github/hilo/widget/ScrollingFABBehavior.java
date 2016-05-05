package com.github.hilo.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * 随可滑动内容隐藏显示的 FAB
 */
public class ScrollingFABBehavior extends /*FloatingActionButton*/CoordinatorLayout.Behavior<LinearLayout> {

	private AppBarLayout mAppBarLayout;
	private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
	private boolean startAnimation;

	public ScrollingFABBehavior(Context context,AttributeSet attrs) {
		super();
	}

	public boolean onStartNestedScroll(CoordinatorLayout parent,FloatingActionButton child,View directTargetChild,View target,
					int nestedScrollAxes) {
		return true;
	}

	@Override public boolean layoutDependsOn(CoordinatorLayout parent,LinearLayout child,View dependency) {
		if (dependency instanceof AppBarLayout) {
			mAppBarLayout = (AppBarLayout)dependency;
			return true;
		} else {
			return false;
		}
	}

	@Override public boolean onDependentViewChanged(CoordinatorLayout parent,LinearLayout child,View dependency) {
		// 植入判断条件，当已经处于hide状态的fab，就不需要再走隐藏动画了；
		if (mAppBarLayout.getY() < 0) { // hide
			if (startAnimation) {
				startAnimation = !startAnimation;
				child.animate().translationY(350f).setDuration(400).setInterpolator(DECELERATE_INTERPOLATOR).start();
			}
		} else { // show 第一次一定会先走这里逻辑
			if (!startAnimation) {
				startAnimation = !startAnimation;
				child.animate().translationY(0f).setDuration(400).setInterpolator(DECELERATE_INTERPOLATOR).start();
			}
		}

		return super.onDependentViewChanged(parent,child,dependency);
	}

	/**
	 * extends FloatingActionButton.Behavior
	 *//*
	@Override public void onNestedScroll(CoordinatorLayout coordinatorLayout,
					FloatingActionButton child,View target,int dxConsumed,int dyConsumed,int dxUnconsumed,
					int dyUnconsumed) {
		super.onNestedScroll(coordinatorLayout,child,target,dxConsumed,dyConsumed,dxUnconsumed,
												 dyUnconsumed);
		if (mAppBarLayout.getY() < 0) {
			child.hide();
		} else {
			child.show();
		}
	}*/

}