package com.github.hilo.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.hilo.App;
import com.github.hilo.R;
import com.github.hilo.di.components.ApplicationComponent;
import com.github.hilo.di.modules.ActivityModule;

import butterknife.ButterKnife;

public abstract class BaseAppCompatActivity extends AppCompatActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(this.getLayoutId());
		ButterKnife.bind(this);

		this.getApplicationComponent().inject(this);
		this.initToolbarOnCreate(savedInstanceState);
		this.initInjector();
		this.initViews(savedInstanceState);
		this.initListeners();
		this.initData();
	}

	/**
	 * Fill in layout id
	 *
	 * @return layout id
	 */
	protected abstract int getLayoutId();

	/**
	 * Initialize the toolbar in the layout
	 *
	 * @param savedInstanceState savedInstanceState
	 */
	protected abstract void initToolbarOnCreate(Bundle savedInstanceState);

	/**
	 * Initialize the Injector to Activity
	 */
	protected abstract void initInjector();

	/**
	 * Initialize the view in the layout
	 *
	 * @param savedInstanceState savedInstanceState
	 */
	protected abstract void initViews(Bundle savedInstanceState);

	/**
	 * Initialize the Activity data
	 */
	protected abstract void initData();

	/**
	 * Initialize the View of the listener
	 */
	protected abstract void initListeners();

	/**
	 * Call this when your activity is done and should be closed.  The
	 * ActivityResult is propagated back to whoever launched you via
	 * onActivityResult().
	 */
	@Override public void finish() {
		super.finish();
	}

	@Override protected void onDestroy() {
		super.onDestroy();
	}

	protected void showToast(String msg) {
		getApplicationComponent().toast().makeText(msg);
	}

	protected void showToast(String msg,int duration) {
		if (msg == null) return;
		if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
			getApplicationComponent().toast().makeText(msg,duration);
		} else {
			showToast(msg);
		}
	}

	/**
	 * Adds a {@link Fragment} to this activity's layout.
	 *
	 * @param containerViewId The container view to where add the fragment.
	 * @param fragment        The fragment to be added.
	 */
	protected void addFragment(int containerViewId,Fragment fragment) {
		FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
		fragmentTransaction.add(containerViewId,fragment);
		fragmentTransaction.commit();
	}

	/**
	 * Get the Main Application component for dependency injection.
	 *
	 * @return ApplicationComponent
	 */
	public ApplicationComponent getApplicationComponent() {
		return ((App)getApplication()).getApplicationComponent();
	}

	/**
	 * Get an Activity module for dependency injection.
	 *
	 * @return Activity Module
	 */
	protected ActivityModule getActivityModule() {
		return new ActivityModule(this);
	}

	protected void overridePendingTransition(boolean backActivity) {
		if (backActivity) overridePendingTransition(0,R.anim.activity_swipeback_ac_right_out);
		else overridePendingTransition(R.anim.activity_swipeback_ac_right_in,
																	 R.anim.activity_swipeback_ac_right_remain);
	}

	@Override public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(true);
	}

	@Override public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(false);
	}
}
