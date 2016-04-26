package com.github.hilo.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.hilo.R;
import com.github.hilo.adapter.BaseRecyclerViewHolder;
import com.github.hilo.adapter.UserListAdapter;
import com.github.hilo.adapter.decration.BorderDividerItemDecration;
import com.github.hilo.di.components.UserComponent;
import com.github.hilo.model.UserModel;
import com.github.hilo.presenter.UserListPresenter;
import com.github.hilo.view.UserListView;
import com.github.hilo.view.activity.MainActivity;
import com.github.hilo.widget.pulltorefresh.PullRefreshLayout;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;

public class UserListFragment extends BaseFragment implements UserListView,
        BaseRecyclerViewHolder.OnItemClickListener, BaseRecyclerViewHolder.OnItemLongClickListener {

    private static final String FRAGMENT_SAVED_STATE_KEY = UserListFragment.class.getSimpleName();
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    PullRefreshLayout swipeRefreshLayout;

    @Inject
    UserListPresenter presenter;
    @Bind(R.id.progressBar)
    RelativeLayout progressBar;

    private BorderDividerItemDecration dataDecration;
    private LinearLayoutManager linearLayoutManager;
    private boolean loadingMoreData;
    private UserListAdapter adapter;
    private ArrayList<UserModel> usersLists;
    /**
     * 屏幕旋转,或者其他事件,导致fragment重走生命周期方法,那么recycler的decration将会变形,要重新设置;
     */
    private boolean resestTheLifeCycle;

    public UserListFragment() {
        setRetainInstance(true);
    }

    @Override
    protected void initInjector() {
        getComponent(UserComponent.class).inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_list;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        setAdapter();
        setSwipeRefreshLayout();
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
        dataDecration = new BorderDividerItemDecration(
                getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
                getResources().getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans)
        );
        recyclerView.addItemDecoration(dataDecration);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void initListeners() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> presenter.initialize());
            swipeRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean moveToDown = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (loadingMoreData) return;
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (moveToDown && manager.findLastCompletelyVisibleItemPosition() == (manager.getItemCount() - 1)) {
                            loadingMoreData = true;
                            // 当滚动到最后一条时的逻辑处理
                            progressBar.setVisibility(View.VISIBLE);
                            loadUserList();
                        } else if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {

                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // 正在向下滑动
                    this.moveToDown = true;
                } else {
                    // 停止滑动或者向上滑动
                    this.moveToDown = false;
                }
            }
        });
    }

    @Override
    protected void initData() {
        presenter.attachView(this);
        loadUserList();
    }

    private void loadUserList() {
        presenter.initialize();
    }

    @Override
    protected void afterOnDetach() {
        presenter.detachView();
    }

    @Override
    protected void mOnViewStateRestored(Bundle savedInstanceState) {
        usersLists = (ArrayList<UserModel>) savedInstanceState.getSerializable(FRAGMENT_SAVED_STATE_KEY);
        resestTheLifeCycle = true;
        renderUserList(usersLists);
    }

    @Override
    protected void mOnSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(FRAGMENT_SAVED_STATE_KEY, usersLists);
    }

    @Override
    public void renderUserList(Collection<UserModel> userModelCollection) {
        feedAdapter(userModelCollection);
    }

    @Override
    public void viewUser(UserModel userModel) {

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        ((MainActivity) getActivity()).showToast("ErrorMessage: " + message);
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void onFailure(Throwable e) {

    }

    private void feedAdapter(Collection<UserModel> usersCollection) {
        this.validateIfNullThrowsException(usersCollection, adapter);
        this.usersLists = (ArrayList<UserModel>) usersCollection;
        this.hideLoading();
        this.setRefreshing(false);

        if (loadingMoreData) {
            loadingMoreData = false;
            int smoothScrollToPosition = adapter.getItemCount();
            adapter.addAll(usersLists);
            recyclerView.scrollToPosition(smoothScrollToPosition);
            adapter.notifyItemChanged(adapter.getItemCount());
        } else {
            checkTheLifeCycleIsChanging(resestTheLifeCycle);
            adapter.setList(usersLists);
            adapter.notifyDataSetChanged();
        }
    }

    private void checkTheLifeCycleIsChanging(boolean resestTheLifeCycle) {
        if (resestTheLifeCycle) {
            this.resestTheLifeCycle = false;
            this.clearDecoration();
            this.recyclerView.setLayoutManager(this.linearLayoutManager);
            this.recyclerView.addItemDecoration(this.dataDecration);
        }
    }

    private void clearDecoration() {
        this.recyclerView.removeItemDecoration(this.dataDecration);
    }

    private void setRefreshing(boolean refreshing) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(refreshing);
    }

    private void validateIfNullThrowsException(Collection<UserModel> usersCollection, UserListAdapter adapter) {
        if (usersCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        } else if (adapter == null) {
            throw new IllegalArgumentException("The adapter cannot be null");
        }
    }

    @Override
    public void onItemClick(View convertView, int position) {
        showToast(position + "");
    }

    @Override
    public void onItemLongClick(View convertView, int position) {

    }
}
