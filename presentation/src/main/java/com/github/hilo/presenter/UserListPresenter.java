package com.github.hilo.presenter;

import com.github.hilo.di.scope.PerActivity;
import com.github.hilo.domain.User;
import com.github.hilo.domain.interactor.DefaultSubscriber;
import com.github.hilo.domain.interactor.UseCase;
import com.github.hilo.mapper.UserModelDataMapper;
import com.github.hilo.model.UserModel;
import com.github.hilo.view.UserListView;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class UserListPresenter extends BasePresenter<UserListView> {

    private final UseCase getUserListUseCase;
    private final UserModelDataMapper userModelDataMapper;

    @Inject
    public UserListPresenter(@Named("userList") UseCase getUserListUseCase,
                             UserModelDataMapper userModelDataMapper) {
        this.getUserListUseCase = getUserListUseCase;
        this.userModelDataMapper = userModelDataMapper;

    }

    @Override
    public void resume() {
        // 1.通过subscription管理，但是在ondestroy时，要主动解除订阅事件 getUserListUseCase.unsubscribe();
        // this.getUserListUseCase.execute(new UserListSubscriber());
        // 2.通过BasePresent基类中CompositeSubscription管理，不需要处理解除订阅事件问题，当activity 生命周期结束时，会自动解除，
        // 但是需要手动在onCompleted里remove掉该subscription对象
        mCompositeSubscription.add(
                getUserListUseCase.execute().
                        subscribe(new UserListSubscriber()));
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        getUserListUseCase.unsubscribe();
    }

    private void hideViewLoading() {
        this.getMvpView().hideLoading();
    }

    private void showUsersInView(User user) {
        final UserModel userModel = userModelDataMapper.transform(user);
        this.getMvpView().viewUser(userModel);
    }

    private void showErrorMessage(Exception e) {
        getMvpView().showError(e.getMessage());
    }

    private final class UserListSubscriber extends DefaultSubscriber<User> {
        @Override
        public void onCompleted() {
            if (mCompositeSubscription != null)
                mCompositeSubscription.remove(this);
            UserListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            UserListPresenter.this.showErrorMessage((Exception) e);
        }

        @Override
        public void onNext(User user) {
            UserListPresenter.this.showUsersInView(user);
        }
    }
}
