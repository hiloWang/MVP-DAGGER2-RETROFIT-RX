package com.github.hilo.di.components;

import com.github.hilo.di.modules.ActivityModule;
import com.github.hilo.di.scope.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface DetailsComponent extends ActivityComponent {

}
