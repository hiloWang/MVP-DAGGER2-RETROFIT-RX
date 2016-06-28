package com.github.hilo.di.components;

import android.app.Activity;

import com.github.hilo.di.modules.ActivityModule;
import com.github.hilo.di.scope.PerActivity;

import dagger.Component;

/**
 * 管理Activity类的实例，生命周期与activity生命周期一样长
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

	//Exposed to sub-graphs.
	Activity getActivity();

}
