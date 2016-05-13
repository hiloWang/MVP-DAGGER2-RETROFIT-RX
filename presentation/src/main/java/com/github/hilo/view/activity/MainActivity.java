package com.github.hilo.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hilo.R;
import com.github.hilo.di.components.DaggerUserComponent;
import com.github.hilo.di.components.UserComponent;
import com.github.hilo.di.interfaces.HasComponent;
import com.github.hilo.model.UserModel;
import com.github.hilo.util.UIUtils;
import com.github.hilo.view.fragment.UserListFragment;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.Bind;

public class MainActivity extends BaseDrawerLayoutActivity implements HasComponent<UserComponent> {

	private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4.0f);
	private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
	@Bind(R.id.fab) FloatingActionButton fab;
	@Bind(R.id.cloud) TextView tvCloud;
	@Bind(R.id.llFabMenuContainerFirst) LinearLayout llFabMenuContainerFirst;
	@Bind(R.id.tvFabMenuActionFirst) CardView tvFabMenuActionFirst;
	@Bind(R.id.ivFabMenuActionFirst) ImageView ivFabMenuActionFirst;
	private UserComponent userComponent;
	private boolean fabOpened;

	@Override protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override protected void initInjector() {
		userComponent = DaggerUserComponent.builder()
																			 .activityModule(getActivityModule())
																			 .applicationComponent(getApplicationComponent())
																			 .build();
	}

	@Override protected void initViews(Bundle savedInstanceState) {
		this.setupExitActivityAnimation();
		this.setupAnimations();
		this.setupFragment();
	}

	@Override protected void initData() {
		getApplicationComponent().rxBus().toObserverable().subscribe(o -> {
			if (o instanceof UserModel) {
				// fragment callback with this rxBus, may be you can do something in this
			}
		});
	}

	@Override protected void initListeners() {
		RxView.clicks(fab).subscribe(this::onFabClicked);
		RxView.clicks(llFabMenuContainerFirst).subscribe(this::onFabMenuClicked);
		tvCloud.setOnClickListener(v -> {
			if (fabOpened) closeMenu();
		});
	}

	@Override protected void onResume() {
		super.onResume();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	@Override protected NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
		return item -> MainActivity.this.menuItemChecked(item.getItemId());
	}

	@Override protected int[] getMenuItemIds() {
		return new int[] {R.id.nav_home,R.id.nav_explore,R.id.nav_follow,R.id.nav_collect,R.id.nav_register,R.id.nav_settings};
	}

	@Override protected void onMenuItemOnClick(MenuItem now) {}

	@Override public UserComponent getComponent() {
		return userComponent;
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupAnimations() {
		int actionBarSize = UIUtils.dpToPx(56,getResources());
		toolbar.setTranslationY(-actionBarSize);
		toolbar.animate().translationY(0).setDuration(400).setStartDelay(500);
	}

	private void setupFragment() {
		UserListFragment userListFragment = new UserListFragment();
		userListFragment.setArguments(new Bundle());
		addFragment(R.id.fragmentContainer,userListFragment);
	}

	/**
	 * 点击了后退键,主动关闭当前页面时，onSaveInstanceState()
	 * 并不会被调用.
	 * onSaveInstanceState()
	 * 只有在系统即将要自动清理销毁Activity（Fragment）前才会被触发回调；
	 * 以下几种情况必触发：
	 * 1, 由于重力感应 手机从竖屏变为横屏
	 * 2, 手机点击Home键和长按Home键
	 * 3, 点击电源键锁屏时
	 * 4, 从当前Activity跳到另一个Activity
	 * 5, 应用内存不足即将自动销毁时等情况
	 * <p>
	 * 综上所述，这个方法适合临时保存一些非永久性的数据.如果要持久化保存数据,
	 * 就要将操作放在onStop(), onDestroy()
	 * 方法里，并调用onSaveInstanceState()
	 * <p>
	 * 去掉super，不保存回收前状态，
	 * 防止被系统回收时，重新走onCreate
	 * 生命周期时，savedInstanceBundle不为null，引发的crash问题。
	 *
	 * @param outState savedInstanceBundile
	 */
	@Override protected void onSaveInstanceState(Bundle outState) {}

	private void onFabClicked(Void view) {
		if (!fabOpened) openMenu();
		else closeMenu();
	}

	private void onFabMenuClicked(Void view) {
		showSnackbar();
		closeMenu();
	}

	private void openMenu() {
		// the menu is opening about fab
		fabOpened = true;
		fab.setEnabled(false);
		// fab animation
		OpenFabAnimation(fab);
		// background cloud animation
		OpenFogEffectAnimation();
		// fabMenu action animation
		OpenFabMenuAnimation();
	}

	private void closeMenu() {
		// the menu is closing about fab
		fabOpened = false;
		fab.setEnabled(false);

		// fab animation
		CloseFabAnimation(fab);
		// background cloud animation
		CloseFogEffectAnimation();
		// fabMenu action animation
		CloseFabMenuAnimation();
	}

	private void CloseFabMenuAnimation() {
		AnimatorSet animatorSet = new AnimatorSet();
		ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(ivFabMenuActionFirst,"scaleX",1.0f,0.8f);
		bounceAnimX.setDuration(300);
		bounceAnimX.setInterpolator(DECELERATE_INTERPOLATOR);
		ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(ivFabMenuActionFirst,"scaleY",1.0f,0.3f);
		bounceAnimY.setDuration(300);
		bounceAnimY.setInterpolator(DECELERATE_INTERPOLATOR);
		ObjectAnimator alphaBounce = ObjectAnimator.ofFloat(ivFabMenuActionFirst,"alpha",1.f,0.f);
		alphaBounce.setDuration(300);

		ObjectAnimator translationX = ObjectAnimator.ofFloat(tvFabMenuActionFirst,"translationX",0f,90f);
		translationX.setDuration(200);
		ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(tvFabMenuActionFirst,"alpha",1.f,0.f);
		alphaAnim.setInterpolator(DECELERATE_INTERPOLATOR);
		alphaAnim.setDuration(300);

		animatorSet.playTogether(translationX,alphaAnim,bounceAnimX,bounceAnimY,alphaBounce);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override public void onAnimationEnd(Animator animation) {
				tvFabMenuActionFirst.setVisibility(View.INVISIBLE);
				ivFabMenuActionFirst.setVisibility(View.INVISIBLE);
				fab.setEnabled(true);
			}
		});
		animatorSet.setDuration(200);
		animatorSet.start();
	}

	private void CloseFogEffectAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.85f,0);
		alphaAnimation.setDuration(250);
		tvCloud.startAnimation(alphaAnimation);
		tvCloud.setVisibility(View.GONE);
	}

	private void CloseFabAnimation(View fab) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab,"rotation",-135,-175,0);
		objectAnimator.setDuration(300);
		objectAnimator.setInterpolator(DECELERATE_INTERPOLATOR);
		objectAnimator.start();
	}

	private void OpenFabMenuAnimation() {
		AnimatorSet animatorSet = new AnimatorSet();
		tvFabMenuActionFirst.setVisibility(View.VISIBLE);
		ivFabMenuActionFirst.setVisibility(View.VISIBLE);
		tvFabMenuActionFirst.setAlpha(1.f);
		ivFabMenuActionFirst.setAlpha(1.f);

		ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(ivFabMenuActionFirst,"scaleX",0.8f,1.f);
		bounceAnimX.setDuration(300);
		bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);
		ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(ivFabMenuActionFirst,"scaleY",0.8f,1.f);
		bounceAnimY.setDuration(300);
		bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);

		ObjectAnimator translateAnimX = ObjectAnimator.ofFloat(tvFabMenuActionFirst,"translationX",170f,-15f,0f);
		translateAnimX.setDuration(300);

		animatorSet.playTogether(bounceAnimX,bounceAnimY,translateAnimX);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override public void onAnimationEnd(Animator animation) {
				fab.setEnabled(true);
			}
		});
		animatorSet.setDuration(200);
		animatorSet.start();
	}

	private void OpenFogEffectAnimation() {
		tvCloud.setVisibility(View.VISIBLE);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0,0.85f);
		alphaAnimation.setDuration(250);
		alphaAnimation.setFillAfter(true);
		tvCloud.startAnimation(alphaAnimation);
	}

	private void OpenFabAnimation(View fab) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(fab,"rotation",0,-155,-135);
		objectAnimator.setDuration(300);
		objectAnimator.setInterpolator(new DecelerateInterpolator());
		objectAnimator.start();
	}

	private void showSnackbar() {
		Snackbar snackbar = Snackbar.make(fab,"the fab menu clicked :)",Snackbar.LENGTH_SHORT);
		Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout)snackbar.getView();
		snackbarLayout.setBackgroundColor(getResources().getColor(R.color.background_layout));
		((TextView)snackbarLayout.findViewById(R.id.snackbar_text)).setTextColor(
						getResources().getColor(R.color.design_black_text));
		snackbar.show();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP) private void setupExitActivityAnimation() {

	}
}
