package com.github.hilo.view.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hilo.R;
import com.github.hilo.di.components.DaggerDetailsComponent;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;

public class DailyActivity extends BaseToolbarActivity {

	@Bind(R.id.tvTitle) TextView tvTitle;
	@Bind(R.id.detailContainer) LinearLayout detailContainer;

	@Override protected int getLayoutId() {
		return R.layout.activity_daily;
	}

	@Override protected void initInjector() {
		DaggerDetailsComponent.builder()
													.activityModule(getActivityModule())
													.applicationComponent(getApplicationComponent())
													.build();
	}

	@Override protected void initViews(Bundle savedInstanceState) {
		setTitle("内涵段子");
		ViewCompat.setElevation(appBarLayout, 0);
		Bundle UserListFragmentBundle = getIntent().getExtras();
		if (UserListFragmentBundle != null) {
			tvTitle.setText(UserListFragmentBundle.getString("title"));
		}
	}

	@Override protected void initData() {

	}

	@Override protected void initListeners() {
		RxView.clicks(detailContainer).throttleFirst(1,TimeUnit.SECONDS).subscribe(this::startActivity);

		detailContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override public boolean onPreDraw() {
				detailContainer.getViewTreeObserver().removeOnPreDrawListener(this);
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
					Animator animator = ViewAnimationUtils.createCircularReveal(detailContainer,0,0,0,(float)Math.hypot(detailContainer.getWidth(),
																																																							detailContainer.getHeight()));
					animator.setDuration(1000);
					animator.setInterpolator(new DecelerateInterpolator());
					animator.start();
				}
				return true;
			}
		});
	}

	@OnClick(R.id.tvTitle) public void onClick() {
		getApplicationComponent().toast().makeText("dont click me plz :(");
	}

	@Override public void onBackPressed() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			tvTitle.setText("");
			this.finishAfterTransition();
		} else {
			super.onBackPressed();
		}
	}

	private void startActivity(Void aVoid) {
		startActivity(new Intent(DailyActivity.this,DetailsActivity.class));
	}
}