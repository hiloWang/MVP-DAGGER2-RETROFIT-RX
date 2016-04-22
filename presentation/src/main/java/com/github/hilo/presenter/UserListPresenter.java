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
        this.getUserListUseCase.execute(new UserListSubscriber());
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

    private final class UserListSubscriber extends DefaultSubscriber<User> {
        @Override
        public void onCompleted() {
            UserListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(User user) {
            UserListPresenter.this.showUsersInView(user);
        }
    }
}
