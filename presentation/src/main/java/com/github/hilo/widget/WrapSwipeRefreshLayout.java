package com.github.hilo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.github.hilo.R;

public class WrapSwipeRefreshLayout extends SwipeRefreshLayout {

	private CanChildScrollUpCallback mCanChildScrollUpCallback;
	private Drawable mForegroundDrawable;

	public WrapSwipeRefreshLayout(Context context) {
		this(context,null);
	}

	public WrapSwipeRefreshLayout(Context context,AttributeSet attrs) {
		super(context,attrs);
		final TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.WrapSwipeRefreshLayout,0,0);

		this.mForegroundDrawable = a.getDrawable(R.styleable.WrapSwipeRefreshLayout_foreground);
		if (this.mForegroundDrawable != null) {
			this.mForegroundDrawable.setCallback(this);
			setWillNotDraw(false);
		}

		a.recycle();
	}

	@Override protected void onSizeChanged(int w,int h,int oldw,int oldh) {
		super.onSizeChanged(w,h,oldw,oldh);
		if (this.mForegroundDrawable != null) {
			this.mForegroundDrawable.setBounds(0,0,w,h);
		}
	}

	@Override public void draw(@NonNull Canvas canvas) {
		super.draw(canvas);
		if (this.mForegroundDrawable != null) {
			this.mForegroundDrawable.draw(canvas);
		}
	}

	public void setCanChildScrollUpCallback(CanChildScrollUpCallback canChildScrollUpCallback) {
		this.mCanChildScrollUpCallback = canChildScrollUpCallback;
	}

	public interface CanChildScrollUpCallback {
		boolean canSwipeRefreshChildScrollUp();
	}

	@Override public boolean canChildScrollUp() {
		if (mCanChildScrollUpCallback != null) {
			return mCanChildScrollUpCallback.canSwipeRefreshChildScrollUp();
		}
		return super.canChildScrollUp();
	}
}
