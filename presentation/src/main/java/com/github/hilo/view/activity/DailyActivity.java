package com.github.hilo.view.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	@Bind(R.id.coordiNatorContent) CoordinatorLayout coordiNatorContent;
	@Bind(R.id.gmail_fab) FloatingActionButton fab;
	private BottomSheetBehavior bottomSheetBehavior;
	private Animation growAnimation;
	private Animation shrinkAnimation;
	private boolean showFAB;

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
		View bottomSheet = coordiNatorContent.findViewById(R.id.bottom_sheet);
		bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
		growAnimation = AnimationUtils.loadAnimation(DailyActivity.this,R.anim.fab_grow);
		shrinkAnimation = AnimationUtils.loadAnimation(DailyActivity.this,R.anim.fab_shrink);
		ViewCompat.setElevation(appBarLayout,0);
		Bundle UserListFragmentBundle = getIntent().getExtras();
		if (UserListFragmentBundle != null) {
			tvTitle.setText(UserListFragmentBundle.getString("title"));
		}
	}

	@Override protected void initData() {
		super.setTitle("详情界面");
		nsTitle.setText("虚拟数据 内涵段子");
		dailyIv.setImageResource(R.drawable.center_5);
		dailyDateTv.setText(DateUtils.date2yyyyMMdd(new Date()));
	}

	@Override protected void initListeners() {
		RxView.clicks(detailContainer).throttleFirst(1,TimeUnit.SECONDS).subscribe(this::showAppbarLayout);
		RxView.clicks(dailyIv).throttleFirst(1,TimeUnit.SECONDS).subscribe(this::bringToCardActivity);

		detailContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override public boolean onPreDraw() {
				detailContainer.getViewTreeObserver().removeOnPreDrawListener(this);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					Animator animator = ViewAnimationUtils.createCircularReveal(detailContainer,0,0,0,
																																			(float)Math.hypot(detailContainer.getWidth(),
																																												detailContainer.getHeight()));
					animator.setDuration(1500);
					animator.setInterpolator(new DecelerateInterpolator());
					animator.start();
				}

				return true;
			}
		});

		bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override public void onStateChanged(@NonNull View bottomSheet,int newState) {
				switch (newState) {
				// 被拖拽状态
				case BottomSheetBehavior.STATE_DRAGGING:
					fab.startAnimation(shrinkAnimation);
					fab.setVisibility(View.GONE);
					break;
				// 隐藏状态。默认是false，可通过app:behavior_hideable属性设置。
				case BottomSheetBehavior.STATE_HIDDEN:

					break;

				// 完全展开的状态
				case BottomSheetBehavior.STATE_EXPANDED:
					showFAB = true;
					if (fab.getVisibility() == View.GONE) {
						fab.setVisibility(View.VISIBLE);
						fab.startAnimation(growAnimation);
					}

					break;

				// 拖拽松开之后到达终点位置（collapsed or expanded）前的状态
				case BottomSheetBehavior.STATE_SETTLING:

					break;

				// 折叠状态。可通过app:behavior_peekHeight来设置默认显示的高度
				case BottomSheetBehavior.STATE_COLLAPSED:
					if (showFAB) {
						showFAB = false;
						fab.setVisibility(View.GONE);
					}
					break;
				}
			}

			@Override public void onSlide(@NonNull View bottomSheet,float slideOffset) {

			}
		});
	}

	@OnClick(R.id.tvTitle) public void onClick() {
		if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
		else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

	private void bringToCardActivity(Void aVoid) {
		startActivity(new Intent(DailyActivity.this,CardActivity.class));
	}

	private void showAppbarLayout(Void aVoid) {
		if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

		if (appBarLayout.getY() < 0 && nestedScrollView.getScrollY() > 550) {
			appBarLayout.setExpanded(true,false);
			appBarLayout.setTranslationY(-UIUtils.dpToPx(56,getResources()));
			appBarLayout.animate().setDuration(500).translationY(0).setInterpolator(new DecelerateInterpolator()).start();
		}
	}
}