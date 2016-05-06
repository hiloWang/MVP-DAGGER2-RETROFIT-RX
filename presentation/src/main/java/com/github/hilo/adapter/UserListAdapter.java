package com.github.hilo.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.hilo.R;
import com.github.hilo.model.UserModel;
import com.github.hilo.util.DateUtils;
import com.github.hilo.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.github.hilo.util.Preconditions.checkNotNull;

public class UserListAdapter extends BaseRecyclerViewAdapter {

	public static final int VIEW_TYPE_DEFAULT = 1;
	public static final int VIEW_TYPE_FOOTERVIEW = 2;
	public static final int VIEW_TYPE_ERROR = 3;
	private Context context;
	private final int[] drawableIcons = new int[] {R.drawable.center_1,R.drawable.center_2,R.drawable.center_3,R.drawable
					.center_4,R.drawable.center_5};
	@Inject ToastUtils toastUtils;

	public UserListAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override public int[] getItemLayouts() {
		return new int[] {R.layout.item_userlist_fragment,R.layout.view_rv_footer};
	}

	@Override public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder,int position) {
		int itemViewType = getRecycleViewItemType(position);
		switch (itemViewType) {
		case VIEW_TYPE_DEFAULT:
			bindDefaultView(viewHolder,position);
			break;
		case VIEW_TYPE_FOOTERVIEW:
			bindFooterView(viewHolder,position);
			break;
		}
	}

	@Override public int getRecycleViewItemType(int position) {
		if (position == getListSize()) return VIEW_TYPE_FOOTERVIEW;
		return VIEW_TYPE_DEFAULT;
	}

	@SuppressLint("SetTextI18n") private void bindDefaultView(BaseRecyclerViewHolder viewHolder,int position) {
		UserModel data = getItemByPosition(position);
		checkNotNull(data,"data == null");
		TextView mDailyTitleTv = viewHolder.findViewById(R.id.daily_title_tv);
		TextView mDailyDateTv = viewHolder.findViewById(R.id.daily_date_tv);
		ImageView mDailyIv = viewHolder.findViewById(R.id.daily_iv);

		mDailyTitleTv.setText("测试内容" + position + ": 这脑洞太大我无力承受。[笑cry]");
		mDailyDateTv.setText(DateUtils.date2yyyyMMdd(new Date()));

		mDailyIv.setScaleX(0.f);
		mDailyIv.setScaleY(0.f);

		/*Log.e("HILO","当前内存： " + Glide.getPhotoCacheDir(context).getAbsolutePath() + "::" + Glide.getPhotoCacheDir(context)
																																														.getFreeSpace());*/
		Glide.with(context)
				 .load(drawableIcons[(int)(Math.random() * 5)])
				 .diskCacheStrategy(DiskCacheStrategy.ALL)
				 .error(R.mipmap.ic_launcher)
				 .into(mDailyIv);

		mDailyIv.animate().scaleX(1.f).scaleY(1.f).setInterpolator(new OvershootInterpolator()).setDuration(1000);

		// ImageView onClicked event
		RxView.clicks(mDailyIv)
					.throttleFirst(1,TimeUnit.SECONDS)
					.observeOn(Schedulers.newThread())
					.map((Func1<Void, Void>)aVoid -> {

						Glide.get(context).clearDiskCache();
						/*Log.e("HILO","清理后disk： 总空间(" + Glide.getPhotoCacheDir(context).getTotalSpace() + ")" +
										":: 可用空间（" + Glide.getPhotoCacheDir(context).getUsableSpace() + ")");*/
						return null;
					})
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(aVoid -> {

						Glide.get(context).clearMemory();
						/*Log.e("HILO",
									"清理后内存： " + Glide.getPhotoCacheDir(context).getAbsolutePath() + "::" +
													Glide.getPhotoCacheDir(context).getFreeSpace());*/
					});
	}

	private FrameLayout footerViewContainer;
	private LinearLayout errorFooterView;

	private void bindFooterView(BaseRecyclerViewHolder viewHolder,int position) {
		errorFooterView = viewHolder.findViewById(R.id.errorFooterView);
		footerViewContainer = viewHolder.findViewById(R.id.footerViewContainer);
		ImageView footerViewIcon = viewHolder.findViewById(R.id.footerViewIcon);

		errorFooterView.setVisibility(View.GONE);
		if (position != 0 && footerViewContainer.getVisibility() == View.GONE) {
			footerViewContainer.setVisibility(View.VISIBLE);
		}

		footerViewContainer.setTranslationY(0f);
		startLoadingAnimation(footerViewIcon);
	}

	private void startLoadingAnimation(View view) {
		Animation loadingAnimation = AnimationUtils.loadAnimation(context,R.anim.rotate_view_footer);
		// 保持匀速旋转的interpolator
		LinearInterpolator interpolator = new LinearInterpolator();
		loadingAnimation.setInterpolator(interpolator);
		view.startAnimation(loadingAnimation);
	}

	public FrameLayout getFooterView() {
		return footerViewContainer;
	}

	public void setFooterViewDismiss() {
		if (footerViewContainer != null) {
			footerViewContainer.animate().translationY(400f).setDuration(350).setListener(new AnimatorListenerAdapter() {
				@Override public void onAnimationEnd(Animator animation) {
					notifyItemChanged(getItemCount());
				}
			});
		}
	}

	public void setFooterViewError() {
		if (errorFooterView != null) {
			errorFooterView.setVisibility(View.VISIBLE);
			footerViewContainer.setVisibility(View.GONE);
		}
	}
}
