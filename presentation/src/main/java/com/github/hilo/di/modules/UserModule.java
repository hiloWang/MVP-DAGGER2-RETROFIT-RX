package com.github.hilo.di.modules;


import com.github.hilo.di.scope.PerActivity;
import com.github.hilo.domain.interactor.GetUserList;
import com.github.hilo.domain.interactor.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule  {

    @Provides @PerActivity @Named("userList")
    public UseCase provideGetUserList(GetUserList getUserList) {
        return getUserList;
    }
}
