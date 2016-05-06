package com.github.hilo.util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;

import java.util.List;

public class AnimUtils {

	/**
	 * 获取一个旋转动画
	 */
	public static RotateAnimation getRotateAnim() {
		RotateAnimation animation = new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animation.setRepeatCount(-1);
		animation.setDuration(1000);
		animation.setInterpolator(new LinearInterpolator());
		return animation;
	}

	/**
	 * 淡进动画
	 */

	public static AlphaAnimation getInAlphaAnim() {
		AlphaAnimation animation = new AlphaAnimation(0,1);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		animation.setRepeatCount(1);
		return animation;
	}

	/**
	 * 淡出动画
	 */

	public static AlphaAnimation getOutAlphaAnim() {
		AlphaAnimation animation = new AlphaAnimation(1,0);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		return animation;
	}

	/**
	 * 显示并放大的动画，基于自身中间顶部的位置, 动画播放完View不会消失
	 */
	public static void toBigAnim(View view) {
		ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,
																									Animation.RELATIVE_TO_SELF,0.0f);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		view.setAnimation(animation);
		animation.start();
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示并放大的动画，基于自身中间顶部的位置, 动画播放完会消失
	 */
	public static void toBigAnim2(View view) {
		ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,
																									Animation.RELATIVE_TO_SELF,0.0f);
		animation.setDuration(1000);
		animation.setFillAfter(false);
		view.setAnimation(animation);
		animation.start();
	}

	/**
	 * 缩小并隐藏的动画，基于自身中间顶部的位置
	 */
	public static void toSmallAnim(View view) {
		ScaleAnimation animation = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,Animation.RELATIVE_TO_SELF,0.5f,
																									Animation.RELATIVE_TO_SELF,0.0f);
		animation.setDuration(1000);
		animation.setStartOffset(1000);
		animation.setFillAfter(true);
		view.setAnimation(animation);
		animation.start();
		view.setVisibility(View.GONE);
	}

	/**
	 * 给一个控件设置旋转动画
	 **/
	public static void setRotateAnim(View view) {
		RotateAnimation anim = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		LinearInterpolator lir = new LinearInterpolator();
		anim.setInterpolator(lir);
		anim.setDuration(2000);
		anim.setRepeatCount(Animation.INFINITE);
		view.setAnimation(anim);
		anim.start();

	}

	/**
	 * 清除动画,然后让控件隐藏
	 **/
	public static void clearAnimation(View view) {
		view.clearAnimation();
		view.setVisibility(View.GONE);
	}

	public static int dpToPx(int i,Context mContext) {
		DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
		return (int)((i * displayMetrics.density) + 0.5);

	}

	private static Animator setRepeatableAnim(Activity activity,View target,final int duration,int animRes) {
		final Animator anim = AnimatorInflater.loadAnimator(activity,animRes);
		anim.setDuration(duration);
		anim.setTarget(target);
		return anim;
	}

	private static void setListeners(final View target,Animator anim,final Animator animator,final int duration) {
		anim.addListener(new Animator.AnimatorListener() {
			@Override public void onAnimationStart(Animator animat) {
				if (target.getVisibility() == View.INVISIBLE) {
					target.setVisibility(View.VISIBLE);
				}
				new Handler().postDelayed(new Runnable() {
					@Override public void run() {
						animator.start();
					}
				},duration - 100);
			}

			@Override public void onAnimationEnd(Animator animator) {

			}

			@Override public void onAnimationCancel(Animator animator) {

			}

			@Override public void onAnimationRepeat(Animator animator) {

			}
		});
	}

	/**
	 * 适用于ListView等 控件的Item 删除时的动画。
	 */
	public static void deletePattern(final View view,final int position,final List dataList,final BaseAdapter adapter) {

		Animation.AnimationListener al = new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {

			}

			@Override public void onAnimationEnd(Animation animation) {
				dataList.remove(position);
				adapter.notifyDataSetChanged();
			}

			@Override public void onAnimationRepeat(Animation animation) {

			}
		};

		collapse(view,al);

	}

	/**
	 * 适用于ListView等 控件的Item 删除时的动画。
	 * （从下往上消失）
	 */
	public static void collapse(final View view,Animation.AnimationListener al) {
		final int originHeight = view.getMeasuredHeight();

		Animation animation = new Animation() {
			@Override protected void applyTransformation(float interpolatedTime,Transformation t) {
				if (interpolatedTime == 1.0f) {
					view.setVisibility(View.GONE);
				} else {
					view.getLayoutParams().height = originHeight - (int)(originHeight * interpolatedTime);
					view.requestLayout();
				}
			}

			@Override public boolean willChangeBounds() {
				return true;
			}
		};
		if (al != null) {
			animation.setAnimationListener(al);
		}
		animation.setDuration(300);
		view.startAnimation(animation);
	}

	/**
	 * 烧纸
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP) public static Animator attrCreateCircularReveal(View v,int duration) {
		Animator animator = ViewAnimationUtils.createCircularReveal(v,0,0,0,(float)Math.hypot(v.getWidth(),v.getHeight()));
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(duration);
		animator.start();
		return animator;
	}
}
