package com.github.hilo.view;

import com.github.hilo.model.UserModel;

import java.util.Collection;

/**
 * Created by Administrator on 2016/4/21.
 */
public interface UserListView extends LoadDataView {
    /**
     * Render a user list in the UI.
     *
     * @param userModelCollection The collection of {@link UserModel} that will be shown.
     */
    void renderUserList(Collection<UserModel> userModelCollection);

    /**
     * View a {@link UserModel} profile/details.
     *
     * @param userModel The user that will be shown.
     */
    void viewUser(UserModel userModel);
}
