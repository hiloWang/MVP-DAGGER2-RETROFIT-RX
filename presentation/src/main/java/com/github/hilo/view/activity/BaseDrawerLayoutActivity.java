package com.github.hilo.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.github.hilo.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public abstract class BaseDrawerLayoutActivity extends BaseToolbarActivity {

	private final int DRAWER_CLOSED_DELAY_MILLIS = 20;

	@Bind(R.id.drawer_layout) protected DrawerLayout mDrawerLayout;
	@Bind(R.id.nav_view) protected NavigationView mNavigationView;

	private ActionBarDrawerToggle mDrawerToggle;

	protected HashMap<Integer, MenuItem> mMenuItems;
	private DelayHandler delayHandler;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (delayHandler == null) delayHandler = new DelayHandler(this);
		if (this.getNavigationItemSelectedListener() != null)
			this.mNavigationView.setNavigationItemSelectedListener(this.getNavigationItemSelectedListener());

		this.mDrawerLayout.setDrawerListener(new DrawerListener());

		// 初始化MenuItems
		this.mMenuItems = new HashMap<>();
		int[] menuItemIds = this.getMenuItemIds();
		if (menuItemIds.length > 0) {
			for (int id : menuItemIds) {
				MenuItem menuItem = this.mNavigationView.getMenu().findItem(id);
				if (menuItem != null) this.mMenuItems.put(id,menuItem);
			}
		}

		this.mDrawerToggle = new ActionBarDrawerToggle(this,this.mDrawerLayout,R.string.navigation_drawer_open,
																									 R.string.navigation_drawer_close);
	}

	/**
	 * Fill in NavigationView.OnNavigationItemSelectedListener
	 *
	 * @return NavigationView.OnNavigationItemSelectedListener
	 */
	protected abstract NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener();

	/**
	 * Fill in NavigationView menu ids
	 *
	 * @return int[]
	 */
	protected abstract int[] getMenuItemIds();

	/**
	 * Fill in your main operation on click
	 * <p>
	 * 走到这，就不会有两次点击都一样的情况
	 * Come to this, there would be no two clicks are all the same
	 *
	 * @param now Now you choose the item
	 */
	protected abstract void onMenuItemOnClick(MenuItem now);

	/**
	 * set main item check status
	 *
	 * @param itemId itemId
	 * @return true to display the item as the selected item
	 */
	protected boolean menuItemChecked(int itemId) {
		MenuItem old = null;
		MenuItem now;
		if (this.mMenuItems.containsKey(itemId)) {
			for (Map.Entry<Integer, MenuItem> entry : this.mMenuItems.entrySet()) {
				MenuItem menuItem = entry.getValue();
								/*
                 * 先找这个item是否之前被选过
                 * 不影响下面的设置选中
                 */
				if (menuItem.isChecked()) {
					old = menuItem;
				}

                /*
                 * 如果之前找到了之前选中过的
                 * 那别玩了，前后两次选了一样的
                 */
				if (old != null && old.getItemId() == itemId) break;

                /*
                 * 到这了，前后两次的选择不会一样的
                 */
				if (menuItem.getItemId() == itemId) {
					now = menuItem;
					menuItem.setChecked(true);
					this.onMenuItemOnClick(now);
				} else {
					menuItem.setChecked(false);
				}
			}
			this.mDrawerLayout.closeDrawer(this.mNavigationView);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Take care of calling onBackPressed() for pre-Eclair platforms.
	 *
	 * @param keyCode keyCode
	 * @param event   event
	 */
	@Override public boolean onKeyDown(int keyCode,KeyEvent event) {
		// 如果抽屉打开了
		if (keyCode == KeyEvent.KEYCODE_BACK && this.mDrawerLayout.isDrawerOpen(this.mNavigationView)) {
			this.mDrawerLayout.closeDrawer(this.mNavigationView);
			return true;
		}
		return super.onKeyDown(keyCode,event);
	}

	/**
	 * @param item The main item that was selected.
	 * @return boolean Return false to allow normal main processing to
	 * proceed, true to consume it here.
	 * @see #onCreateOptionsMenu
	 */
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.mDrawerLayout.openDrawer(GravityCompat.START);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public interface OnDrawerClosedCallback {
		void onDrawerClosedCallback();
	}

	private OnDrawerClosedCallback onDrawerClosedCallback;

	public void setOnDrawerClosedCallback(OnDrawerClosedCallback onDrawerClosedCallback) {
		this.onDrawerClosedCallback = onDrawerClosedCallback;
	}

	/**
	 * When using ActionBarDrawerToggle, all DrawerLayout listener methods should be forwarded
	 * if the ActionBarDrawerToggle is not used as the DrawerLayout listener directly.
	 */
	private class DrawerListener implements DrawerLayout.DrawerListener {
		@Override public void onDrawerOpened(View drawerView) {
			BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerOpened(drawerView);
			if (BaseDrawerLayoutActivity.this.mActionBarHelper != null) {
				BaseDrawerLayoutActivity.this.mActionBarHelper.onDrawerOpened();
			}
		}

		@Override public void onDrawerClosed(View drawerView) {
			BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerClosed(drawerView);
			BaseDrawerLayoutActivity.this.mActionBarHelper.onDrawerClosed();
			if (onDrawerClosedCallback != null) {
				delayHandler.postDelayed(() -> {
					onDrawerClosedCallback.onDrawerClosedCallback();
					onDrawerClosedCallback = null;
				},DRAWER_CLOSED_DELAY_MILLIS);
			}
		}

		@Override public void onDrawerSlide(View drawerView,float slideOffset) {
			BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerSlide(drawerView,slideOffset);
		}

		@Override public void onDrawerStateChanged(int newState) {
			BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	@Override protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		if (delayHandler != null) {
			delayHandler.removeCallbacksAndMessages(null);
			delayHandler = null;
		}
	}

	private static class DelayHandler extends Handler {

		private WeakReference<BaseDrawerLayoutActivity> mWeakReference;

		public DelayHandler(BaseDrawerLayoutActivity clazz) {
			mWeakReference = new WeakReference<>(clazz);
		}

		@Override public void handleMessage(Message msg) {
			BaseDrawerLayoutActivity clazz = mWeakReference.get();
			if (clazz != null) {

			}
		}
	}
}
