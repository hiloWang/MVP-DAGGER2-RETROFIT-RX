package com.github.hilo.util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import java.util.Locale;

public class UIUtils {
	private UIUtils() {
	}

	public static int getDrawerWidth(Resources res) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

			if (res.getConfiguration().smallestScreenWidthDp >= 600 || res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				// device is a tablet
				return (int)(320 * res.getDisplayMetrics().density);
			} else {
				return (int)(res.getDisplayMetrics().widthPixels - (56 * res.getDisplayMetrics().density));
			}
		} else { // for devices without smallestScreenWidthDp reference calculate if device screen is
        // over 600 dp
			if ((res.getDisplayMetrics().widthPixels / res.getDisplayMetrics().density) >= 600 || res
              .getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
				return (int)(320 * res.getDisplayMetrics().density);
			else
				return (int)(res.getDisplayMetrics().widthPixels - (56 * res.getDisplayMetrics().density));
		}
	}

	public static boolean isTablet(Resources res) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			return res.getConfiguration().smallestScreenWidthDp >= 600;
		} else { // for devices without smallestScreenWidthDp reference calculate if device screen is
        // over 600
			return (res.getDisplayMetrics().widthPixels / res.getDisplayMetrics().density) >= 600;

		}
	}

	public static int getScreenHeight(Activity act,Context context) {
		int height;
		WindowManager wm = null;
		if (act == null) {
			if (context != null) wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		} else {
			wm = act.getWindowManager();
		}
		if (wm == null) throw new RuntimeException("Activtiy or context can be null");

		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			height = size.y;
		} else {
			height = display.getHeight();  // deprecated
		}
		return height;
	}

	public static Point getUserPhotoSize(Resources res) {
		int size = (int)(64 * res.getDisplayMetrics().density);

		return new Point(size,size);
	}

	public static Point getBackgroundSize(Resources res) {
		int width = getDrawerWidth(res);

		int height = (9 * width) / 16;

		return new Point(width,height);
	}


	public static Bitmap getCroppedBitmapDrawable(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),
																				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0,0,0,0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2,bitmap.getHeight() / 2,bitmap.getWidth() / 2,paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap,rect,rect,paint);
		//Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		//return _bmp;
		return output;
	}

	public static Bitmap resizeBitmapFromResource(Resources res,int resId,int reqWidth,
					int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res,resId,options);

		// Calculate inSampleSize
		options.inSampleSize = calculateSize(options,reqWidth,reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res,resId,options);
	}

	public static Bitmap resizeBitmap(Bitmap bitmap,int reqWidth,int reqHeight) {
		return Bitmap.createScaledBitmap(bitmap,reqWidth,reqHeight,true);

	}

	public static int calculateSize(BitmapFactory.Options options,int reqWidth,int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static void recycleDrawable(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
			bitmapDrawable.getBitmap().recycle();
		}
	}

	public static boolean isRTL() {
		Locale defLocale = Locale.getDefault();
		final int directionality = Character.getDirectionality(defLocale.getDisplayName().charAt(0));
		return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT || directionality == Character
            .DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
	}

	public static void setAlpha(View v,float alpha) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			v.setAlpha(alpha);
		} else {
			AlphaAnimation animation = new AlphaAnimation(alpha,alpha);
			animation.setDuration(0);
			animation.setFillAfter(true);
			v.startAnimation(animation);
		}
	}

	/**
	 * Convert Dp to Pixel
	 */
	public static int dpToPx(float dp,Resources resources) {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,
																				 resources.getDisplayMetrics());
		return (int)px;
	}

	public static int dpToPx(int dp) {
		return (int)(dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static int dpToPx(float dp,Activity resources) {
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,
																				 resources.getResources().getDisplayMetrics());
		return (int)px;
	}

	public static int getRelativeTop(View myView) {
		//	    if (myView.getParent() == myView.getRootView())
		if (myView.getId() == android.R.id.content) return myView.getTop();
		else return myView.getTop() + getRelativeTop((View)myView.getParent());
	}

	public static int getRelativeLeft(View myView) {
		//	    if (myView.getParent() == myView.getRootView())
		if (myView.getId() == android.R.id.content) return myView.getLeft();
		else return myView.getLeft() + getRelativeLeft((View)myView.getParent());
	}

	private static Animator setRepeatableAnim(Activity activity,View target,final int duration,
					int animRes) {
		final Animator anim = AnimatorInflater.loadAnimator(activity,animRes);
		anim.setDuration(duration);
		anim.setTarget(target);
		return anim;
	}

	private static void setListeners(final View target,Animator anim,final Animator animator,
					final int duration) {
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

	public static Drawable cornerDrawable(final int bgColor,float cornerradius) {
		final GradientDrawable bg = new GradientDrawable();
		bg.setCornerRadius(cornerradius);
		bg.setColor(bgColor);

		return bg;
	}

	public static Drawable cornerDrawable(final int bgColor,float[] cornerradius) {
		final GradientDrawable bg = new GradientDrawable();
		bg.setCornerRadii(cornerradius);
		bg.setColor(bgColor);

		return bg;
	}

	public static Drawable cornerDrawable(final int bgColor,float[] cornerradius,int borderwidth,
					int bordercolor) {
		final GradientDrawable bg = new GradientDrawable();
		bg.setCornerRadii(cornerradius);
		bg.setStroke(borderwidth,bordercolor);
		bg.setColor(bgColor);

		return bg;
	}

	/**
	 * set btn selector with corner drawable for special position
	 */
	public static StateListDrawable btnSelector(float radius,int normalColor,int pressColor,
					int postion) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = null;
		Drawable pressed = null;

		if (postion == 0) {// left btn
			normal = cornerDrawable(normalColor,new float[] {0,0,0,0,0,0,radius,radius});
			pressed = cornerDrawable(pressColor,new float[] {0,0,0,0,0,0,radius,radius});
		} else if (postion == 1) {// right btn
			normal = cornerDrawable(normalColor,new float[] {0,0,0,0,radius,radius,0,0});
			pressed = cornerDrawable(pressColor,new float[] {0,0,0,0,radius,radius,0,0});
		} else if (postion == -1) {// only one btn
			normal = cornerDrawable(normalColor,new float[] {0,0,0,0,radius,radius,radius,radius});
			pressed = cornerDrawable(pressColor,new float[] {0,0,0,0,radius,radius,radius,radius});
		} else if (postion == -2) {// for material dialog
			normal = cornerDrawable(normalColor,radius);
			pressed = cornerDrawable(pressColor,radius);
		}

		bg.addState(new int[] {-android.R.attr.state_pressed},normal);
		bg.addState(new int[] {android.R.attr.state_pressed},pressed);
		return bg;
	}

	public static int getHeight(Context context) {
		int statusBarHeight = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height","dimen","android");
		if (resourceId > 0) {
			statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
		}
		//        Log.d(UIUtils.class.getSimpleName(), "statusBarHeight--->" + statusBarHeight);
		if (isFlymeOs4x()) {
			return 2 * statusBarHeight;
		}

		return statusBarHeight;
	}

	public static boolean isFlymeOs4x() {
		String sysVersion = Build.VERSION.RELEASE;
		if ("4.4.4".equals(sysVersion)) {
			String sysIncrement = Build.VERSION.INCREMENTAL;
			String displayId = Build.DISPLAY;
			if (!TextUtils.isEmpty(sysIncrement)) {
				return sysIncrement.contains("Flyme_OS_4");
			} else {
				return displayId.contains("Flyme OS 4");
			}
		}
		return false;
	}

	/**
	 * set ListView item selector with corner drawable for the last position
	 * (ListView的item点击效果,只处理最后一项圆角处理)
	 */
	public static StateListDrawable listItemSelector(float radius,int normalColor,int pressColor,
					boolean isLastPostion) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = null;
		Drawable pressed = null;

		if (!isLastPostion) {
			normal = new ColorDrawable(normalColor);
			pressed = new ColorDrawable(pressColor);
		} else {
			normal = cornerDrawable(normalColor,new float[] {0,0,0,0,radius,radius,radius,radius});
			pressed = cornerDrawable(pressColor,new float[] {0,0,0,0,radius,radius,radius,radius});
		}

		bg.addState(new int[] {-android.R.attr.state_pressed},normal);
		bg.addState(new int[] {android.R.attr.state_pressed},pressed);
		return bg;
	}

	/**
	 * set ListView item selector with corner drawable for the first and the last position
	 * (ListView的item点击效果,第一项和最后一项圆角处理)
	 */
	public static StateListDrawable listItemSelector(float radius,int normalColor,int pressColor,
					int itemTotalSize,int itemPosition) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = null;
		Drawable pressed = null;

		if (itemPosition == 0 && itemPosition == itemTotalSize - 1) {// 只有一项
			normal = cornerDrawable(normalColor,
															new float[] {radius,radius,radius,radius,radius,radius,radius,
                                      radius});
			pressed = cornerDrawable(pressColor,
															 new float[] {radius,radius,radius,radius,radius,radius,radius,
                                       radius});
		} else if (itemPosition == 0) {
			normal = cornerDrawable(normalColor,new float[] {radius,radius,radius,radius,0,0,0,0,});
			pressed = cornerDrawable(pressColor,new float[] {radius,radius,radius,radius,0,0,0,0});
		} else if (itemPosition == itemTotalSize - 1) {
			normal = cornerDrawable(normalColor,new float[] {0,0,0,0,radius,radius,radius,radius});
			pressed = cornerDrawable(pressColor,new float[] {0,0,0,0,radius,radius,radius,radius});
		} else {
			normal = new ColorDrawable(normalColor);
			pressed = new ColorDrawable(pressColor);
		}

		bg.addState(new int[] {-android.R.attr.state_pressed},normal);
		bg.addState(new int[] {android.R.attr.state_pressed},pressed);
		return bg;
	}

	public static boolean isAndroid5() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}
}
