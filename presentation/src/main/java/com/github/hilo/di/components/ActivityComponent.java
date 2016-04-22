package com.github.hilo.di.components;

import android.app.Activity;

import com.github.hilo.di.modules.ActivityModule;
import com.github.hilo.di.scope.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/20.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

    //Exposed to sub-graphs.
    Activity getActivity();

}
