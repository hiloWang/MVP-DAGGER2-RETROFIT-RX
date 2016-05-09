package com.github.hilo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.github.hilo.R;
import com.github.hilo.util.UIUtils;

public class MaterialFooterView extends FrameLayout {

	private MaterialCircleProgressBar circleProgressBar;
	private int arrowStokeWidth = 3;
	private boolean isShowArrow = true, isShowProgressBg = true;
	private int progressBg = 0x123456;
	private int arrowSize = 45;
	private int arrowColor = getResources().getColor(R.color.refresh_progress_1);


	public MaterialFooterView(Context context) {
		this(context,null);
		init();
	}

	public MaterialFooterView(Context context,AttributeSet attrs) {
		this(context,attrs,0);
		init();
	}

	public MaterialFooterView(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
		init();
	}


	protected void init() {
		if (isInEditMode()) return;
		setClipToPadding(false);
		setWillNotDraw(false);
	}

	public void setArrowSize(int arrowSize) {
		this.arrowSize = arrowSize;
	}

	public void setProgressBg(int progressBg) {
		this.progressBg = progressBg;
	}

	public void setIsProgressBg(boolean isShowProgressBg) {
		this.isShowProgressBg = isShowProgressBg;
	}

	public void setArrowStokeWidth(int w) {
		this.arrowStokeWidth = w;
	}

	public void showProgressArrow(boolean isShowArrow) {
		this.isShowArrow = isShowArrow;
	}

	public void setArrowColor(int color) {
		this.arrowColor = color;
	}

	@Override protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		circleProgressBar = new MaterialCircleProgressBar(getContext());
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(UIUtils.dpToPx(arrowSize),
																																				 UIUtils.dpToPx(arrowSize));
		layoutParams.gravity = Gravity.CENTER;
		circleProgressBar.setLayoutParams(layoutParams);
		circleProgressBar.setShowArrow(isShowArrow);
		circleProgressBar.setArrowStokeWidth(arrowStokeWidth);
		circleProgressBar.setColorSchemeColors(arrowColor);
		circleProgressBar.setCircleBackgroundEnabled(isShowProgressBg);
		circleProgressBar.setProgressBackGroundColor(progressBg);
		this.addView(circleProgressBar);
	}
}
