package com.github.hilo.view.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;

import com.github.hilo.R;
import com.github.hilo.di.components.DaggerUserComponent;
import com.github.hilo.di.components.UserComponent;
import com.github.hilo.di.interfaces.HasComponent;
import com.github.hilo.util.UIUtils;
import com.github.hilo.view.fragment.BaseFragment;
import com.github.hilo.view.fragment.UserListFragment;
import com.github.hilo.widget.FeedContextMenu;
import com.github.hilo.widget.FeedContextMenuManager;

import butterknife.OnClick;

public class MainActivity extends BaseDrawerLayoutActivity implements
				HasComponent<UserComponent>, BaseFragment.Callbacks {

	private UserComponent userComponent;

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
	}

	@Override protected void initData() {}

	@Override protected void initListeners() {

	}

	@Override protected void onResume() {
		super.onResume();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	@Override protected NavigationView.OnNavigationItemSelectedListener
	getNavigationItemSelectedListener() {
		return item -> MainActivity.this.menuItemChecked(item.getItemId());
	}

	@Override protected int[] getMenuItemIds() {
		return new int[] {R.id.nav_home,R.id.nav_slidelist,R.id.nav_share,R.id.nav_send};
	}

	@Override protected void onMenuItemOnClick(MenuItem now) {

	}

	@Override public UserComponent getComponent() {
		return userComponent;
	}

	@Override public void onFragmentItemSelectedCallback(int position,String text) {}

	@Override public void controlFabBehaviorCallback(boolean scrollingDown) {

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

	public void showToastApplication(String msg) {
		showToast(msg);
	}

	/**
	 * 点击了后退键,主动关闭当前页面时，onSaveInstanceState() 并不会被调用.
	 * onSaveInstanceState() 只有在系统即将要自动清理销毁Activity（Fragment）前才会被触发回调；
	 * 以下几种情况必触发：
	 * 1, 由于重力感应 手机从竖屏变为横屏
	 * 2, 手机点击Home键和长按Home键
	 * 3, 点击电源键锁屏时
	 * 4, 从当前Activity跳到另一个Activity
	 * 5, 应用内存不足即将自动销毁时等情况
	 * <p>
	 * 综上所述，这个方法适合临时保存一些非永久性的数据.如果要持久化保存数据,就要将操作放在onStop(), onDestroy()方法里，并调用onSaveInstanceState()
	 * <p>
	 * *去掉super，不保存回收前状态， 防止被系统回收时，重新走onCreate生命周期时，savedInstanceBundle不为null，引发的crash问题。*
	 *
	 * @param outState savedInstanceBundile
	 */
	@Override protected void onSaveInstanceState(Bundle outState) {}

	@OnClick(R.id.fab) public void onClick(View view) {
		FeedContextMenuManager.getInstance()
													.toggleContextMenuFromView(view,-1,
																										 new FeedContextMenu
																														 .OnFeedContextMenuClickListener() {
																											 @Override public void onReportClick(
																															 int position) {
																												 showToast("Report");
																												 FeedContextMenuManager.getInstance()
																																							 .hideContextMenu();
																											 }

																											 @Override public void onSharePhotoClick(
																															 int position) {
																												 showToast("Share");
																												 FeedContextMenuManager.getInstance()
																																							 .hideContextMenu();
																											 }

																											 @Override public void onCopyShareUrlClick(
																															 int position) {
																												 showToast("CopyShareUrl");
																												 FeedContextMenuManager.getInstance()
																																							 .hideContextMenu();
																											 }

																											 @Override public void onCancelClick(
																															 int position) {
																												 showToast("nCancel");
																												 FeedContextMenuManager.getInstance()
																																							 .hideContextMenu();
																											 }
																										 });
	}
}
