package com.github.hilo.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.hilo.R;

import butterknife.Bind;

/**
 * Description：BaseToolbarActivity
 * Created by：CaMnter
 * Time：2016-01-05 00:32
 */
public abstract class BaseToolbarActivity extends BaseAppCompatActivity {

	@Bind(R.id.toolbar) protected Toolbar toolbar;
	@Bind(R.id.app_bar_layout) protected AppBarLayout appBarLayout;

	protected ActionBarHelper mActionBarHelper;

	/**
	 * Initialize the toolbar in the layout
	 *
	 * @param savedInstanceState savedInstanceState
	 */
	@Override protected void initToolbarOnCreate(Bundle savedInstanceState) {
		this.initToolbarHelper();
	}

	/**
	 * init the toolbar
	 */
	protected void initToolbarHelper() {
		if (this.toolbar == null || this.appBarLayout == null) return;

		toolbar.setTitleTextAppearance(this,R.style.toolbarTitle);
		this.setSupportActionBar(this.toolbar);

		this.mActionBarHelper = this.createActionBarHelper();
		this.mActionBarHelper.init();


		if (Build.VERSION.SDK_INT >= 21) {
			this.appBarLayout.setElevation(6.6f);
		}
	}

	/**
	 * @param item The main item that was selected.
	 * @return boolean Return false to allow normal main processing to
	 * proceed, true to consume it here.
	 * @see #onCreateOptionsMenu
	 */
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	protected void showBack() {
		if (this.mActionBarHelper != null) this.mActionBarHelper.setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * set the AppBarLayout alpha
	 *
	 * @param alpha alpha
	 */
	protected void setAppBarLayoutAlpha(float alpha) {
		this.appBarLayout.setAlpha(alpha);
	}

	/**
	 * set the AppBarLayout visibility
	 *
	 * @param visibility visibility
	 */
	protected void setAppBarLayoutVisibility(boolean visibility) {
		if (visibility) {
			this.appBarLayout.setVisibility(View.VISIBLE);
		} else {
			this.appBarLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * Create a compatible helper that will manipulate the action bar if available.
	 */
	public ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper();
	}

	public class ActionBarHelper {
		private final ActionBar mActionBar;
		public CharSequence mDrawerTitle;
		public CharSequence mTitle;

		public ActionBarHelper() {
			this.mActionBar = getSupportActionBar();
		}

		public void init() {
			if (this.mActionBar == null) return;
			this.mActionBar.setDisplayHomeAsUpEnabled(true);
			this.mActionBar.setDisplayShowHomeEnabled(false);
			this.mTitle = mDrawerTitle = getTitle();
		}

		public void onDrawerClosed() {
			if (this.mActionBar == null) return;
			//            this.mActionBar.setTitle(this.mTitle);
		}

		public void onDrawerOpened() {
			if (this.mActionBar == null) return;
			//            this.mActionBar.setTitle(this.mDrawerTitle);
		}

		public void setTitle(CharSequence title) {
			this.mTitle = title;
			mActionBar.setTitle(title);
		}

		public void setDrawerTitle(CharSequence drawerTitle) {
			this.mDrawerTitle = drawerTitle;
		}

		public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
			if (this.mActionBar == null) return;
			this.mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
		}
	}
}
