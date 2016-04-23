package com.github.hilo.di.components;

import com.github.hilo.MainActivity;
import com.github.hilo.di.modules.ActivityModule;
import com.github.hilo.di.modules.UserModule;
import com.github.hilo.di.scope.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, UserModule.class})
public interface UserComponent extends ActivityComponent {

    void inject(MainActivity mainActivity);
}