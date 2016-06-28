package com.github.hilo.di.modules;

import android.app.Activity;

import com.github.hilo.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Activity state and expose it to the graph.
 * 提供的构造方法用于注入不同的activity
 */
@Module
public class ActivityModule {

	private final Activity activity;

	public ActivityModule(Activity activity) {
		this.activity = activity;
	}

	/** Expose the activity to dependents in the graph. */
	@Provides @PerActivity public Activity provideActivity() {
		return this.activity;
	}
}
