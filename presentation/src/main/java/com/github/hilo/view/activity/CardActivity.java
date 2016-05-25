package com.github.hilo.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hilo.R;
import com.github.hilo.interfaces.MenuAnimation;
import com.github.hilo.widget.MaterialMenuDrawable;
import com.github.hilo.widget.circlebotton.CircularSplashView;
import com.github.hilo.widget.circlebotton.TransitionHelper;
import com.github.hilo.widget.interpolator.ExpoIn;
import com.github.hilo.widget.interpolator.QuintOut;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardActivity extends Activity implements MenuAnimation {

	public static final int TRANSFORM_DURATION = 900;

	private View rootView;
	@Bind(R.id.menu) ImageView menu;
	private ViewGroup menuContainer;
	private MaterialMenuDrawable menuIcon;

	private boolean isMenuVisible;
	private int curretMenuIndex = 0;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		ButterKnife.bind(this);

		rootView = getWindow().getDecorView();
		this.makeAppFullscreen();
		this.setupMenuContainer();
		this.setupMenuButton();

		RxView.clicks(menu).throttleFirst(1,TimeUnit.SECONDS).subscribe(this::onMenuClicked);

	}

	@Override public void startActivity(Intent intent) {
		super.startActivity(intent);
		this.overridePendingTransition(false);
	}

	@Override public void animateToMenu() {
		TransitionHelper.animateToMenuState(rootView,new AnimatorListenerAdapter() {
			@Override public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				//				waterScene.setPause(false);
			}
		});
		menuIcon.animateIconState(MaterialMenuDrawable.IconState.ARROW);
	}

	@Override public void revertFromMenu() {
		TransitionHelper.startRevertFromMenu(rootView,showShadowListener);
		menuIcon.animateIconState(MaterialMenuDrawable.IconState.BURGER);
		//		waterScene.setPause(true);
	}

	@Override public void exitFromMenu() {
		TransitionHelper.animateMenuOut(rootView);
		//		waterScene.setPause(true);
	}

	@Override public void onBackPressed() {
		this.overridePendingTransition(true);
		if (isMenuVisible) {
			hideMenu();
		}
		super.onBackPressed();
	}

	protected void overridePendingTransition(boolean backActivity) {
		if (backActivity) overridePendingTransition(0,R.anim.activity_swipeback_ac_right_out);
		else overridePendingTransition(R.anim.activity_swipeback_ac_right_in,R.anim.activity_swipeback_ac_right_remain);
	}

	private void setupMenuButton() {
		menuIcon = new MaterialMenuDrawable(CardActivity.this,Color.WHITE,MaterialMenuDrawable.Stroke.THIN,TRANSFORM_DURATION);
		menu.setImageDrawable(menuIcon);
	}

	private void makeAppFullscreen() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

	private void setupMenuContainer() {
		menuContainer = (ViewGroup)findViewById(R.id.menu_container);
		int color = getResources().getColor(R.color.splash1);
		addMenuItem(menuContainer,"Water And Noise",R.drawable.splash1,color,R.drawable.menu_btn,0);
		addMenuItem(menuContainer,"Two Bears",R.drawable.splash2,getResources().getColor(R.color.splash2),R.drawable.menu_btn2,
								1);
		addMenuItem(menuContainer,"Depth Playground",R.drawable.splash3,getResources().getColor(R.color.splash3),
								R.drawable.menu_btn3,2);
		addMenuItem(menuContainer,"About",R.drawable.splash4,getResources().getColor(R.color.splash4),R.drawable.menu_btn4,3);
		selectMenuItem(curretMenuIndex,color);
		menuContainer.setTranslationY(20000);
	}


	private void selectMenuItem(int menuIndex,int color) {
		for (int i = 0; i < menuContainer.getChildCount(); i++) {
			View menuItem = menuContainer.getChildAt(i);
			if (i == menuIndex) select(menuItem,color);
			else unselect(menuItem);
		}
		curretMenuIndex = menuIndex;
	}

	private void select(View menuItem,int color) {
		final CircularSplashView circularSplashView = (CircularSplashView)menuItem.findViewById(R.id.circleView);
		circularSplashView.setScaleX(1.f);
		circularSplashView.setScaleY(1.f);
		circularSplashView.setVisibility(View.VISIBLE);
		circularSplashView.introAnimate();
		fadeColoTo(color,(TextView)menuItem.findViewById(R.id.item_text));
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void unselect(View menuItem) {
		final View circularSplashView = menuItem.findViewById(R.id.circleView);
		circularSplashView.animate()
											.scaleX(0)
											.scaleY(0)
											.setDuration(150)
											.withEndAction(() -> circularSplashView.setVisibility(View.INVISIBLE))
											.start();
		fadeColoTo(Color.BLACK,(TextView)menuItem.findViewById(R.id.item_text));
	}

	private void fadeColoTo(int newColor,TextView view) {
		ObjectAnimator color = ObjectAnimator.ofObject(view,"TextColor",new ArgbEvaluator(),view.getCurrentTextColor(),
																									 newColor);
		color.setDuration(200);
		color.start();
	}

	private void addMenuItem(ViewGroup container,String text,int drawableResource,int splashColor,int containerStyle,
					int menuIndex) {
		ViewGroup item = (ViewGroup)LayoutInflater.from(CardActivity.this).inflate(R.layout.item_menu_bottom,container,false);
		((TextView)item.findViewById(R.id.item_text)).setText(text);
		CircularSplashView circluarSplashView = (CircularSplashView)item.findViewById(R.id.circleView);
		circluarSplashView.setSplash(BitmapFactory.decodeResource(getResources(),drawableResource));
		circluarSplashView.setSplashColor(splashColor);
		item.setOnClickListener(getMenuItemClick(menuIndex,splashColor));
		if (menuIndex == 0) {
			int padding = (int)getResources().getDimension(R.dimen.menu_item_height_padding);
			menuContainer.addView(item,new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
																																		 (int)(getResources().getDimension(
																																						 R.dimen.menu_item_height) + padding)));
			item.setPadding(0,padding,0,0);
		} else if (menuIndex == 3) {
			int padding = (int)getResources().getDimension(R.dimen.menu_item_height_padding);
			menuContainer.addView(item,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
																															 (int)getResources().getDimension(
																																			 R.dimen.menu_item_height) + padding));
			item.setPadding(0,0,0,padding);
		} else menuContainer.addView(item,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
																																		(int)getResources().getDimension(
																																						R.dimen.menu_item_height)));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			item.setBackground(getResources().getDrawable(containerStyle,null));
		}
	}

	AnimatorListenerAdapter showShadowListener = new AnimatorListenerAdapter() {
		@Override public void onAnimationEnd(Animator animation) {
			super.onAnimationEnd(animation);
			showShadow();
		}
	};

	private void showShadow() {
		View actionbarShadow = findViewById(R.id.actionbar_shadow);
		actionbarShadow.setVisibility(View.VISIBLE);
		ObjectAnimator.ofFloat(actionbarShadow,View.ALPHA,0,0.8f).setDuration(400).start();
	}

	private View.OnClickListener getMenuItemClick(int menuIndex,int splashColor) {
		return v -> {
			if (menuIndex == curretMenuIndex) onBackPressed();
			else if (menuIndex == 0) {
				hideMenu();
				selectMenuItem(menuIndex,splashColor);
			} else if (menuIndex == 1) {
				hideMenu();
				selectMenuItem(menuIndex,splashColor);
			}
			hideMenu();
			TransitionHelper.animateMenuOut(rootView);
			TransitionHelper.startIntroAnim(rootView,showShadowListener);
			selectMenuItem(menuIndex,splashColor);
		};
	}

	private void onMenuClicked(Void view) {
		if (!isMenuVisible) showMenu();
		else /*onBackPressed();*/hideMenu();
	}

	private void showMenu() {
		isMenuVisible = true;
		ObjectAnimator translationY = ObjectAnimator.ofFloat(menuContainer,View.TRANSLATION_Y,menuContainer.getHeight(),0);
		translationY.setDuration(700);
		translationY.setInterpolator(new QuintOut());
		translationY.setStartDelay(150);
		translationY.start();
		this.selectMenuItem(curretMenuIndex,((TextView)menuContainer.getChildAt(curretMenuIndex)
																																.findViewById(R.id.item_text)).getCurrentTextColor());
		this.animateToMenu();
	}

	private void hideMenu() {
		isMenuVisible = false;
		ObjectAnimator translationY = ObjectAnimator.ofFloat(menuContainer,View.TRANSLATION_Y,menuContainer.getHeight());
		translationY.setDuration(700);
		translationY.setInterpolator(new ExpoIn());
		translationY.start();

		this.exitFromMenu();
	}
}
