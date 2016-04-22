package com.github.hilo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.widget.Button;

import com.github.hilo.di.components.DaggerUserComponent;
import com.github.hilo.di.components.UserComponent;
import com.github.hilo.model.UserModel;
import com.github.hilo.presenter.UserListPresenter;
import com.github.hilo.view.UserListView;
import com.github.hilo.view.activity.BaseDrawerLayoutActivity;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseDrawerLayoutActivity implements UserListView {

    @Bind(R.id.btnTest)
    Button btnTest;

    @Inject
    UserListPresenter presenter;
    UserComponent userComponent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        userComponent = DaggerUserComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .build();
        userComponent.inject(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void initData() {
        presenter.attachView(this);
    }

    @Override
    protected NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return null;
    }

    @Override
    protected int[] getMenuItemIds() {
        return new int[0];
    }

    @Override
    protected void onMenuItemOnClick(MenuItem now) {

    }


    @OnClick(R.id.btnTest)
    public void onClick() {
        presenter.resume();
    }

    @Override
    public void renderUserList(Collection<UserModel> userModelCollection) {

    }

    @Override
    public void viewUser(UserModel userModel) {
        getApplicationComponent().toast().makeText(userModel.toString());
    }

    @Override
    public void onFailure(Throwable e) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public Context context() {
        return null;
    }
}
