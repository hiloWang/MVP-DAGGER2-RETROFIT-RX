package com.github.hilo.mapper;

import com.github.hilo.di.scope.PerActivity;
import com.github.hilo.domain.User;
import com.github.hilo.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

@PerActivity
public class UserModelDataMapper {

    @Inject
    UserModelDataMapper() {
    }

    /**
     * Transform a {@link User} into an {@link UserModel}.
     *
     * @param user Object to be transformed.
     * @return {@link UserModel}.
     */
    public UserModel transform(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        UserModel userModel = new UserModel();
        userModel.setResults(user.getResults());
        userModel.setError(user.isError());

        return userModel;
    }

    /**
     * Transform a Collection of {@link User} into a Collection of {@link UserModel}.
     *
     * @param usersCollection Objects to be transformed.
     * @return List of {@link UserModel}.
     */
    public Collection<UserModel> transform(Collection<User> usersCollection) {
        Collection<UserModel> userModelsCollection;

        if (usersCollection != null && !usersCollection.isEmpty()) {
            userModelsCollection = new ArrayList<>();
            for (User user : usersCollection) {
                userModelsCollection.add(transform(user));
            }
        } else {
            userModelsCollection = Collections.emptyList();
        }

        return userModelsCollection;
    }

}
