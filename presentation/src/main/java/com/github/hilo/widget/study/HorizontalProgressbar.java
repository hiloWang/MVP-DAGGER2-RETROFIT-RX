package com.github.hilo.widget.study;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.github.hilo.R;

public class HorizontalProgressbar extends ProgressBar {

	private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
	private static final int DEFAULT_TEXT_SIZE = 10; // SP
	private static final int DEFAULT_TEXT_OFFSET = 10; // DP
	private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
	private static final int DEFAULT_UNREACH_COLOR = DEFAULT_TEXT_COLOR;
	private static final int DEFAULT_REACH_HEIGHT = 2; // DP
	private static final int DEFAULT_UNREACH_HEIGHT = 2; // DP

	private int textColor = dp2px(DEFAULT_TEXT_COLOR);
	private int textSize = sp2px(DEFAULT_TEXT_SIZE);
	private int textOffset = dp2px(DEFAULT_TEXT_OFFSET);
	private int reachColor = DEFAULT_REACH_COLOR;
	private int unReachColor = DEFAULT_UNREACH_COLOR;
	private int reachHeight = dp2px(DEFAULT_REACH_HEIGHT);
	private int unReachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);

	private Paint paint = new Paint();
	private int realWidth;

	public HorizontalProgressbar(Context context) {
		this(context,null);
	}

	public HorizontalProgressbar(Context context,AttributeSet attrs) {
		this(context,attrs,0);
	}

	public HorizontalProgressbar(Context context,AttributeSet attrs,int defStyleAttr) {
		super(context,attrs,defStyleAttr);
		this.obtainStyledAttrs(attrs);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP) public HorizontalProgressbar(Context context,AttributeSet attrs,int defStyleAttr,
					int defStyleRes) {
		super(context,attrs,defStyleAttr,defStyleRes);
	}

	private int dp2px(int dpVal) {
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
	}

	private int sp2px(int spVal) {
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
	}

	/**
	 * 获取自定义属性
	 */
	private void obtainStyledAttrs(AttributeSet attrs) {
		TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.HorizontalProgressbar);
		textColor = ta.getColor(R.styleable.HorizontalProgressbar_progress_text_color,textColor);
		textSize = (int)ta.getDimension(R.styleable.HorizontalProgressbar_progress_text_size,textSize);
		textOffset = (int)ta.getDimension(R.styleable.HorizontalProgressbar_progress_text_offset,textOffset);
		reachColor = ta.getColor(R.styleable.HorizontalProgressbar_progress_reach_color,reachColor);
		unReachColor = ta.getColor(R.styleable.HorizontalProgressbar_progress_unreach_color,unReachColor);
		reachHeight = (int)ta.getDimension(R.styleable.HorizontalProgressbar_progress_reach_height,reachHeight);
		unReachHeight = (int)ta.getDimension(R.styleable.HorizontalProgressbar_progress_unreach_height,unReachHeight);
		ta.recycle();
		paint.setTextSize(textSize);
	}

	/**
	 * 测量
	 */
	@Override protected synchronized void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
		// 拿到宽度模式和值，不过宽度不需要测量，因为用户必须给定一个明确的宽度值，所以这里只需要测量高度即可；
		// int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthVal = MeasureSpec.getSize(widthMeasureSpec);

		// 测量高度
		int heightVal = this.measureHeight(heightMeasureSpec);
		setMeasuredDimension(widthVal,heightVal);

		realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
	}

	@Override protected synchronized void onDraw(Canvas canvas) {
		canvas.save();
		// 横坐标移动到getPaddingLeft处，纵坐标移动到控件高度的一半位置，开始画;
		canvas.translate(getPaddingLeft(),getHeight() / 2);

		// 进度条是否为100%，默认false
		boolean reachedMaximumValue = false;

		String text = getProgress() + "%";
		int textWidth = (int)paint.measureText(text);

		// 1、绘制reach progressbar
		float drawReachBarRadio = getProgress() * 1.f / getMax();
		float theCurrentXpoint = drawReachBarRadio * realWidth;
		// 则表示绘制到100%了
		if (theCurrentXpoint + textWidth > realWidth) {
			reachedMaximumValue = true;
			theCurrentXpoint = realWidth - textWidth;
		}

		float stopX = theCurrentXpoint - textOffset / 2;
		if (stopX > 0) {
			paint.setColor(reachColor);
			paint.setStrokeWidth(reachHeight);
			canvas.drawLine(0,0,stopX,0,paint);
		}

		// 2、绘制文字：像下移动文字高度的一半距离开始绘制
		paint.setColor(textColor);
		int drawTextY = (int)-((paint.descent() + paint.ascent()) / 2);
		canvas.drawText(text,theCurrentXpoint,drawTextY,paint);

		// 3、绘制unreach progressbar
		if (!reachedMaximumValue) {
			paint.setColor(unReachColor);
			paint.setStrokeWidth(unReachHeight);
			float startX = theCurrentXpoint + textOffset / 2 + textWidth; // theCurrentXpoint(包含了字体间距的一半) + textOffSet + 字体宽度
			canvas.drawLine(startX,0,realWidth,0,paint);
		}

		canvas.restore();
	}

	private int measureHeight(int heightMeasureSpec) {
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightVal = MeasureSpec.getSize(heightMeasureSpec);
		int measureHeight;

		// 如果用户在xml里精确的定义了控件的高度,例如200dp或match_parent
		if (heightMode == MeasureSpec.EXACTLY) {
			measureHeight = heightVal;
		} else {
			// 文字的高度，前提paint必须设置文字的大小，paint.setTextSize(textSize);
			int textHeight = (int)(paint.descent() + paint.ascent());
			// 如果没有定义, 则取result值
			int padding = getPaddingTop() + getPaddingBottom();// 必要的条件, 固定写法
			measureHeight = padding + Math.max(Math.max(reachHeight,unReachHeight),Math.abs(textHeight)); // 三者取最大的

			// 如果是该模式AT_MOST，则不能超过result最大的值
			if (heightMode == MeasureSpec.AT_MOST) {
				measureHeight = Math.min(measureHeight,heightVal);
			}
		}
		return measureHeight;
	}
}
