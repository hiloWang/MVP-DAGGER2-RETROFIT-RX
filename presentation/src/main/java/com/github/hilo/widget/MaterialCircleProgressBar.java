package com.github.hilo.widget;
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.github.hilo.R;

public class MaterialCircleProgressBar extends ImageView {

	private static final int KEY_SHADOW_COLOR = 0x1E000000;
	private static final int FILL_SHADOW_COLOR = 0x3D000000;
	// PX
	private static final float X_OFFSET = 0f;
	private static final float Y_OFFSET = 1.75f;
	private static final float SHADOW_RADIUS = 3.5f;
	private static final float SHADOW_ELEVATION = 0f;

	public static final int DEFAULT_CIRCLE_BG_LIGHT = 0xFFFAFAFA;
	private static final int DEFAULT_CIRCLE_DIAMETER = 40;
	private static final int STROKE_WIDTH_LARGE = 2;

	private int mShadowRadius;
	private int mCircleBackGroundColor;
	private int mArrowColor;
	private int mArrowStokeWidth;
	private int mArrowWidth;
	private int mArrowHeight;
	private int mDiameter;
	private int mInnerRadius;
	private boolean mShowArrow;
	public MaterialArrowProgressDrawable mProgressDrawable;
	private ShapeDrawable mBgCircle;
	private boolean mCircleBackgroundEnabled;
	private int[] mColors = new int[] {Color.BLACK};

	public MaterialCircleProgressBar(Context context) {
		super(context);
		init(context,null,0);
	}

	public MaterialCircleProgressBar(Context context,AttributeSet attrs) {
		super(context,attrs);
		init(context,attrs,0);
	}

	public MaterialCircleProgressBar(Context context,AttributeSet attrs,int defStyleAttr) {
		super(context,attrs,defStyleAttr);
		init(context,attrs,defStyleAttr);
	}

	private void init(Context context,AttributeSet attrs,int defStyleAttr) {
		final TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MaterialCircleProgressBar,defStyleAttr,0);

		final float density = getContext().getResources().getDisplayMetrics().density;

		mCircleBackGroundColor = a.getColor(R.styleable.MaterialCircleProgressBar_circle_background_color,
																				DEFAULT_CIRCLE_BG_LIGHT);
		mCircleBackgroundEnabled = a.getBoolean(R.styleable.MaterialCircleProgressBar_enable_circle_background,true);
		mArrowColor = a.getColor(R.styleable.MaterialCircleProgressBar_arrow_color,DEFAULT_CIRCLE_BG_LIGHT);
		mColors = new int[] {mArrowColor};
		mArrowStokeWidth = a.getDimensionPixelOffset(R.styleable.MaterialCircleProgressBar_circle_progress_stoke_width,
																								 (int)(STROKE_WIDTH_LARGE * density));

		mShowArrow = a.getBoolean(R.styleable.MaterialCircleProgressBar_show_arrow,false);

		// if there are not set will have default values in onLayout method
		mInnerRadius = a.getDimensionPixelOffset(R.styleable.MaterialCircleProgressBar_circle_inner_radius,-1);
		mArrowWidth = a.getDimensionPixelOffset(R.styleable.MaterialCircleProgressBar_arrow_width,-1);
		mArrowHeight = a.getDimensionPixelOffset(R.styleable.MaterialCircleProgressBar_arrow_height,-1);

		a.recycle();
		mProgressDrawable = new MaterialArrowProgressDrawable(getContext(),this);
		mProgressDrawable.setStartEndTrim(0,(float)0.75);
		super.setImageDrawable(mProgressDrawable);
	}

	public void setProgressBackGroundColor(int color) {
		this.mCircleBackGroundColor = color;
		invalidate();
	}

	private boolean elevationSupported() {
		return android.os.Build.VERSION.SDK_INT >= 21;
	}

	@Override protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		if (!elevationSupported()) {
			setMeasuredDimension(getMeasuredWidth() + mShadowRadius * 2,getMeasuredHeight() + mShadowRadius * 2);
		}
	}

	public void setArrowStokeWidth(int arrowStokeWidth) {
		final float density = getContext().getResources().getDisplayMetrics().density;
		this.mArrowStokeWidth = (int)(arrowStokeWidth * density);
		invalidate();
	}

	@Override protected void onLayout(boolean changed,int left,int top,int right,int bottom) {
		super.onLayout(changed,left,top,right,bottom);
		final float density = getContext().getResources().getDisplayMetrics().density;
		mDiameter = Math.min(getMeasuredWidth(),getMeasuredHeight());
		if (mDiameter <= 0) {
			mDiameter = (int)density * DEFAULT_CIRCLE_DIAMETER;
		}
		if (getBackground() == null && mCircleBackgroundEnabled) {
			final int shadowYOffset = (int)(density * Y_OFFSET);
			final int shadowXOffset = (int)(density * X_OFFSET);
			mShadowRadius = (int)(density * SHADOW_RADIUS);

			if (elevationSupported()) {
				mBgCircle = new ShapeDrawable(new OvalShadow(1,80));
				ViewCompat.setElevation(this,SHADOW_ELEVATION * density);
			} else {
				OvalShape oval = new OvalShadow(mShadowRadius,mDiameter - mShadowRadius * 2);
				mBgCircle = new ShapeDrawable(oval);
				ViewCompat.setLayerType(this,ViewCompat.LAYER_TYPE_SOFTWARE,mBgCircle.getPaint());
				mBgCircle.getPaint().setShadowLayer(mShadowRadius,shadowXOffset,shadowYOffset,KEY_SHADOW_COLOR);
				final int padding = mShadowRadius;
				// set padding so the inner image sits correctly within the shadow.
				setPadding(padding,padding,padding,padding);
			}
			// TODO: mBgCircle.getPaint().setColor(mCircleBackGroundColor）;
			mBgCircle.getPaint().setColor(getResources().getColor(R.color.cpb_white));
			setBackgroundDrawable(mBgCircle);
		}
		mProgressDrawable.setBackgroundColor(mCircleBackGroundColor);
		mProgressDrawable.setColorSchemeColors(mColors);
		mProgressDrawable.setSizeParameters(mDiameter,mDiameter,
																				mInnerRadius <= 0 ? (mDiameter - mArrowStokeWidth * 2) / 4 : mInnerRadius,
																				mArrowStokeWidth,mArrowWidth < 0 ? mArrowStokeWidth * 4 : mArrowWidth,
																				mArrowHeight < 0 ? mArrowStokeWidth * 2 : mArrowHeight);
		if (isShowArrow()) {
			mProgressDrawable.showArrowOnFirstStart(true);
			mProgressDrawable.setArrowScale(1f);
			mProgressDrawable.showArrow(true);
		}
		super.setImageDrawable(null);
		super.setImageDrawable(mProgressDrawable);
		mProgressDrawable.setAlpha(255);
		if (getVisibility() == VISIBLE) {
			mProgressDrawable.setStartEndTrim(0,(float)0.8);
		}
	}

	public boolean isShowArrow() {
		return mShowArrow;
	}

	public void setShowArrow(boolean showArrow) {
		this.mShowArrow = showArrow;
		invalidate();
	}

	@Override final public void setImageURI(Uri uri) {
		super.setImageURI(uri);
	}

	@Override final public void setImageDrawable(Drawable drawable) {
	}

	/**
	 * Set the color resources used in the progress animation from color resources.
	 * The first color will also be the color of the bar that grows in response
	 * to a user swipe gesture.
	 */
	public void setColorSchemeResources(int... colorResIds) {
		final Resources res = getResources();
		int[] colorRes = new int[colorResIds.length];
		for (int i = 0; i < colorResIds.length; i++) {
			colorRes[i] = res.getColor(colorResIds[i]);
		}
		setColorSchemeColors(colorRes);
	}

	/**
	 * Set the colors used in the progress animation. The first
	 * color will also be the color of the bar that grows in response to a user
	 * swipe gesture.
	 */
	public void setColorSchemeColors(int... colors) {
		mColors = colors;
		if (mProgressDrawable != null) {
			mProgressDrawable.setColorSchemeColors(colors);
		}
	}

	public void setCircleBackgroundEnabled(boolean enableCircleBackground) {
		this.mCircleBackgroundEnabled = enableCircleBackground;
		invalidate();
	}

	@Override public int getVisibility() {
		return super.getVisibility();
	}

	@Override public void setVisibility(int visibility) {
		super.setVisibility(visibility);
	}

	@Override protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mProgressDrawable != null) {
			mProgressDrawable.stop();
			mProgressDrawable.setVisible(getVisibility() == VISIBLE,false);
		}
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mProgressDrawable != null) {
			mProgressDrawable.stop();
			mProgressDrawable.setVisible(false,false);
		}
	}

	private class OvalShadow extends OvalShape {
		private RadialGradient mRadialGradient;
		private int mShadowRadius;
		private Paint mShadowPaint;
		private int mCircleDiameter;

		public OvalShadow(int shadowRadius,int circleDiameter) {
			super();
			mShadowPaint = new Paint();
			mShadowRadius = shadowRadius;
			mCircleDiameter = circleDiameter;
			mRadialGradient = new RadialGradient(mCircleDiameter / 2,mCircleDiameter / 2,mShadowRadius,
																					 new int[] {FILL_SHADOW_COLOR,Color.TRANSPARENT},null,Shader.TileMode.CLAMP);
			mShadowPaint.setShader(mRadialGradient);
		}

		@Override public void draw(Canvas canvas,Paint paint) {
			final int viewWidth = MaterialCircleProgressBar.this.getWidth();
			final int viewHeight = MaterialCircleProgressBar.this.getHeight();
			canvas.drawCircle(viewWidth / 2,viewHeight / 2,(mCircleDiameter / 2 + mShadowRadius),mShadowPaint);
			canvas.drawCircle(viewWidth / 2,viewHeight / 2,(mCircleDiameter / 2),paint);
		}
	}
}