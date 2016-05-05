package com.github.hilo.view.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
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

	@Bind(R.id.fab) FloatingActionButton fab;
	@Bind(R.id.cloud) TextView tvCloud;
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
		setupAnimations();
		setupFragment();
		setupFloatingActionButton();
	}

	@Override protected void initData() {
		getApplicationComponent().rxBus().toObserverable().subscribe(o -> {
			if (o instanceof UserModel) {
				// do something
			}
		});
	}

	@Override protected void initListeners() {
		RxView.clicks(fab).subscribe(this::onFabClicked);
		tvCloud.setOnClickListener(v -> {if (fabOpened) closeMenu(fab);});
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

	@Override protected void onMenuItemOnClick(MenuItem now) {

	}

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

	private void setupFloatingActionButton() {
		ImageView icon = new ImageView(this);
		icon.setImageDrawable(getResources().getDrawable(R.drawable.fab_add));

		FloatingActionButton actionButton = new FloatingActionButton(this);
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

	public void onFabClicked(Void view) {
		if (!fabOpened) openMenu(fab);
		else closeMenu(fab);
	}

	private void openMenu(View view) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
		objectAnimator.setDuration(300);
		objectAnimator.setInterpolator(new DecelerateInterpolator());
		objectAnimator.start();
		tvCloud.setVisibility(View.VISIBLE);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0,0.85f);
		alphaAnimation.setDuration(250);
		alphaAnimation.setFillAfter(true);
		tvCloud.startAnimation(alphaAnimation);
		fabOpened = true;
	}

	private void closeMenu(View view) {
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",-135,-175,0);
		objectAnimator.setDuration(300);
		objectAnimator.setInterpolator(new DecelerateInterpolator());
		objectAnimator.start();
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.85f,0);
		alphaAnimation.setDuration(250);
		tvCloud.startAnimation(alphaAnimation);
		tvCloud.setVisibility(View.GONE);
		fabOpened = false;
	}
}
