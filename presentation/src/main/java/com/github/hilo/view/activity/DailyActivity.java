package com.github.hilo.view.activity;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hilo.R;
import com.github.hilo.di.components.DaggerDetailsComponent;
import com.github.hilo.util.DateUtils;
import com.github.hilo.util.UIUtils;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DailyActivity extends BaseToolbarActivity {

	@Bind(R.id.tvTitle) TextView tvTitle;
	@Bind(R.id.detailContainer) LinearLayout detailContainer;
	@Bind(R.id.nsTitle) TextView nsTitle;
	@Bind(R.id.daily_iv) CircleImageView dailyIv;
	@Bind(R.id.daily_date_tv) TextView dailyDateTv;
	@Bind(R.id.nestedScrollView) NestedScrollView nestedScrollView;

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
		ViewCompat.setElevation(appBarLayout,0);
		Bundle UserListFragmentBundle = getIntent().getExtras();
		if (UserListFragmentBundle != null) {
			tvTitle.setText(UserListFragmentBundle.getString("title"));
		}
	}

	@Override protected void initData() {
		setTitle("详情界面");
		nsTitle.setText("虚拟数据 内涵段子");
		dailyIv.setImageResource(R.drawable.center_5);
		dailyDateTv.setText(DateUtils.date2yyyyMMdd(new Date()));
	}

	@Override protected void initListeners() {
		RxView.clicks(detailContainer).throttleFirst(1,TimeUnit.SECONDS).subscribe(this::showAppbarLayout);

		detailContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override public boolean onPreDraw() {
				detailContainer.getViewTreeObserver().removeOnPreDrawListener(this);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					Animator animator = ViewAnimationUtils.createCircularReveal(detailContainer,0,0,0,
																																			(float)Math.hypot(detailContainer.getWidth(),
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
			tvTitle.setVisibility(View.GONE);
			dailyIv.setVisibility(View.GONE);
			dailyDateTv.setVisibility(View.GONE);
			this.finishAfterTransition();
		} else {
			super.onBackPressed();
		}
	}

	private void showAppbarLayout(Void aVoid) {
		if (appBarLayout.getY() < 0 && nestedScrollView.getScrollY() > 550) {
			appBarLayout.setExpanded(true,false);
			appBarLayout.setTranslationY(-UIUtils.dpToPx(56,getResources()));
			appBarLayout.animate().setDuration(500).translationY(0).setInterpolator(new DecelerateInterpolator()).start();
		}
	}
}