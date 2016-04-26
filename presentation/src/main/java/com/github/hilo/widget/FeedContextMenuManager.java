package com.github.hilo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.github.hilo.util.UIUtils;

public class FeedContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener {

    private FeedContextMenu mFeedContextMenu;
    private static FeedContextMenuManager instance;
    private FeedContextMenuManager() {}

    private boolean isContextMenuShowing;
    private boolean isContextMenuDismissing;

    public static FeedContextMenuManager getInstance() {
        if (instance == null) {
            synchronized (FeedContextMenuManager.class) {
                if (instance == null) {
                    instance = new FeedContextMenuManager();
                }
            }
        }
        return instance;
    }

    public void toggleContextMenuFromView(View openingView, int position, FeedContextMenu.OnFeedContextMenuClickListener listener) {
        if (mFeedContextMenu == null) {
            showContextMenuFromView(openingView, position, listener);
        } else {
            hideContextMenu();
        }
    }


    private void showContextMenuFromView(final View openingView, int position, FeedContextMenu.OnFeedContextMenuClickListener listener) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            mFeedContextMenu = new FeedContextMenu(openingView.getContext());
            mFeedContextMenu.bindToItem(position);
            mFeedContextMenu.addOnAttachStateChangeListener(this);
            mFeedContextMenu.setOnFeedContextMenuClickListener(listener);

            ((ViewGroup)openingView.getRootView().findViewById(android.R.id.content)).addView(mFeedContextMenu);
            // 当一个视图树将要绘制时，所要调用的回调函数的接口类
            mFeedContextMenu.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mFeedContextMenu.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitalPostion(openingView);
                    perforShowAnimation();
                    return false;
                }
            });
        }
    }

    private void setupContextMenuInitalPostion(View openingView) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = UIUtils.dpToPx(16);
        mFeedContextMenu.setTranslationX(openingViewLocation[0] - mFeedContextMenu.getWidth() / 3);
        mFeedContextMenu.setTranslationY(openingViewLocation[1] - mFeedContextMenu.getHeight() - additionalBottomMargin);
    }

    private void perforShowAnimation() {
        mFeedContextMenu.setPivotX(mFeedContextMenu.getWidth() / 2);
        mFeedContextMenu.setPivotY(mFeedContextMenu.getHeight());
        mFeedContextMenu.setScaleX(0.5f);
        mFeedContextMenu.setScaleY(0.5f);
        mFeedContextMenu.animate()
                .scaleX(1.0f).scaleY(1.0f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }


    private void perforDismissAnimation() {
        mFeedContextMenu.setPivotX(mFeedContextMenu.getWidth() / 2);
        mFeedContextMenu.setPivotY(mFeedContextMenu.getHeight());
        mFeedContextMenu.animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mFeedContextMenu != null) {
                            mFeedContextMenu.dismiss();
                        }
                        isContextMenuDismissing = false;
                    }
                });
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            perforDismissAnimation();
        }
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mFeedContextMenu != null) {
            hideContextMenu();
            mFeedContextMenu.setTranslationY(mFeedContextMenu.getTranslationY() - dy);
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        mFeedContextMenu = null;
    }
}
