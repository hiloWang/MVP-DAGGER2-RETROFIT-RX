package com.github.hilo.view.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.hilo.R;
import com.github.hilo.adapter.BaseRecyclerViewHolder;
import com.github.hilo.adapter.UserListAdapter;
import com.github.hilo.adapter.decration.BorderDividerItemDecration;
import com.github.hilo.di.components.UserComponent;
import com.github.hilo.model.UserModel;
import com.github.hilo.presenter.UserListPresenter;
import com.github.hilo.view.UserListView;
import com.github.hilo.view.activity.DailyActivity;
import com.github.hilo.view.activity.MainActivity;
import com.github.hilo.widget.WrapSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.design.widget.RxSnackbar;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import rx.schedulers.Schedulers;

import static com.github.hilo.util.Preconditions.checkNotNull;

public class UserListFragment extends BaseFragment implements UserListView, BaseRecyclerViewHolder.OnItemClickListener,
				BaseRecyclerViewHolder.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {

	@Bind(R.id.recyclerView) RecyclerView recyclerView;
	@Bind(R.id.swipe_refresh_layout) WrapSwipeRefreshLayout swipeRefreshLayout;
	@Inject UserListPresenter presenter;
	private UserComponent userComponent;

	private static final String FRAGMENT_SAVED_STATE_KEY = UserListFragment.class.getSimpleName();
	private BorderDividerItemDecration dataDecration;
	private LinearLayoutManager linearLayoutManager;
	private boolean loadingMoreData;
	private UserListAdapter adapter;
	private ArrayList<UserModel> usersLists;
	/** 屏幕旋转,或者其他事件,导致fragment重走生命周期方法,那么recycler的decration将会变形,要重新设置 BorderDividerItemDecration; */
	private boolean resestTheLifeCycle;

	public UserListFragment() {
		setRetainInstance(true);
	}

	@Override protected void initInjector() {
		// 拿到userComponent实例对象后，调用inject方法，绑定该fragment
		userComponent = getComponent(UserComponent.class);
		userComponent.inject(this);
	}

	@Override protected int getLayoutId() {
		return R.layout.fragment_user_list;
	}

	@Override protected void initViews(View rootView,Bundle savedInstanceState) {
		presenter.attachView(this);
		this.setAdapter();
		this.setSwipeRefreshLayout();
	}

	@Override protected void initListeners() {
		checkNotNull(swipeRefreshLayout,"swipeRefreshLayout == null");

		swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1);
		swipeRefreshLayout.setOnRefreshListener(() -> presenter.initialize());

		// footerView error callback
		((MainActivity)context()).getApplicationComponent().rxBus().toObserverable().observeOn(Schedulers.io()).subscribe(o -> {
			if (null == o) loadUserList();
		});

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			private boolean moveToDown = false;

			@Override public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
				if (loadingMoreData) return;
				RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
				if (layoutManager instanceof LinearLayoutManager) {
					LinearLayoutManager manager = (LinearLayoutManager)layoutManager;
					if (newState == RecyclerView.SCROLL_STATE_IDLE) {
						if (moveToDown && manager.findLastCompletelyVisibleItemPosition() == (manager.getItemCount() - 1)) {
							loadingMoreData = true;
							loadUserList();
						} else if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {

						}
					}
				}
			}

			@Override public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
				this.moveToDown = dy > 0;
				// 弹出的ContextMenu 跟随之前的窗体滚动及消失
				//                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
				//				FeedContextMenuManager.getInstance().hideContextMenu();
			}
		});
	}

	@Override protected void initData() {loadUserList();}

	@Override public void onDestroyView() {
		super.onDestroyView();
		presenter.detachView();
	}

	@SuppressWarnings("unchecked") @Override protected void mOnViewStateRestored(Bundle savedInstanceState) {
		usersLists = (ArrayList<UserModel>)savedInstanceState.getSerializable(FRAGMENT_SAVED_STATE_KEY);
		resestTheLifeCycle = true;
		this.renderUserList(usersLists);
	}

	@Override protected void mOnSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable(FRAGMENT_SAVED_STATE_KEY,usersLists);
	}

	@Override public void renderUserList(Collection<UserModel> userModelCollection) {
		feedAdapter(userModelCollection);
	}

	@Override public void viewUser(UserModel userModel) {}

	@Override public void showLoading() {
		setRefreshing(false);
	}

	@Override public void hideLoading() {
		setRefreshing(false);
	}

	@Override public void showError(String message) {
		adapter.setFooterViewErrorVisible();
		adapter.setFooterViewLoadingInvisible();
		Snackbar snackbar = Snackbar.make(recyclerView,"please check out your network is good",Snackbar.LENGTH_INDEFINITE);
		Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout)snackbar.getView();
		snackbarLayout.setBackgroundColor(getResources().getColor(R.color.background_layout));
		((TextView)snackbarLayout.findViewById(R.id.snackbar_text)).setTextColor(
						getResources().getColor(R.color.design_black_text));
		snackbar.setAction("WELL",v -> {
			RxSnackbar.dismisses(snackbar).subscribe(this::onSnackbarDismissed);
		}).show();
		recyclerView.smoothScrollToPosition(adapter.getItemCount() + 1);
	}

	public void onSnackbarDismissed(int e) {
		setRefreshing(false);
		hideLoading();
	}

	@Override public Context context() {
		return getActivity();
	}

	@Override public void onFailure(Throwable e) {}

	@Override public void onItemClick(View convertView,int position) {
		CardView sharedCardView = (CardView)convertView.findViewById(R.id.container);
		Intent targetActivity = new Intent(getActivity(),DailyActivity.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			TextView mTvTitle = (TextView)convertView.findViewById(R.id.daily_title_tv);
			ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(),sharedCardView,
																																										 "sharedCardView");
			Bundle bundle = activityOptions.toBundle();
			bundle.putString("title",mTvTitle.getText().toString());

			targetActivity.putExtras(bundle);
			startActivity(targetActivity,bundle);
		} else {
			startActivity(targetActivity);
		}
	}

	@Override public void onItemLongClick(View convertView,int position) {}

	public void loadUserList() {
		presenter.initialize();
	}

	private void setAdapter() {
		if (adapter == null) {
			adapter = new UserListAdapter(getActivity());
			recyclerView.setAdapter(adapter);
			adapter.setOnItemClickListener(this);
			adapter.setOnItemLongClickListener(this);
		}
	}

	private void setSwipeRefreshLayout() {
		dataDecration = new BorderDividerItemDecration(getResources().getDimensionPixelOffset(R.dimen
																																																	.data_border_divider_height)
						/*getResources().getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans)*/);
		recyclerView.addItemDecoration(dataDecration);
		linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
		recyclerView.setLayoutManager(linearLayoutManager);
	}

	private void feedAdapter(Collection<UserModel> usersCollection) {
		if (usersCollection == null) return;
		checkNotNull(adapter,"adapter == null");
		this.usersLists = (ArrayList<UserModel>)usersCollection;
		this.hideLoading();
		this.setRefreshing(false);
		((MainActivity)getActivity()).getApplicationComponent().rxBus().send(usersLists.get(0));

		if (loadingMoreData) {
			loadingMoreData = false;
			adapter.addAll(usersLists);
			adapter.dismissFooterViewLoading();
		} else {
			this.checkTheLifeCycleIsChanging(resestTheLifeCycle);
			adapter.setList(usersLists);
			adapter.notifyDataSetChanged();
		}
	}

	private void checkTheLifeCycleIsChanging(boolean resestTheLifeCycle) {
		if (resestTheLifeCycle) {
			this.resestTheLifeCycle = false;
			this.clearDecoration();
			recyclerView.setLayoutManager(this.linearLayoutManager);
			recyclerView.addItemDecoration(this.dataDecration);
		}
	}

	private void clearDecoration() {
		this.recyclerView.removeItemDecoration(this.dataDecration);
	}

	private void setRefreshing(boolean refreshing) {
		checkNotNull(swipeRefreshLayout,"swipeRefreshLayout == null");
		if (refreshing) this.onRefresh();
		else swipeRefreshLayout.setRefreshing(false);
	}

	@Override public void onRefresh() {

	}
}
