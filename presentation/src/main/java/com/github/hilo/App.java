package com.github.hilo;

import android.app.Application;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.github.hilo.di.components.ApplicationComponent;
import com.github.hilo.di.components.DaggerApplicationComponent;
import com.github.hilo.di.modules.ApplicationModule;

public class App extends Application {

	private ApplicationComponent applicationComponent;

	@Override public void onCreate() {
		super.onCreate();

		this.initializeInjector();
		this.initializeLakCanary();
		this.initializeExceptionHandler();
		this.initializeDevMetrics();
	}

	public ApplicationComponent getApplicationComponent() {
		return applicationComponent;
	}

	/**
	 * AndroidDevMetrics measures how performant are Activity lifecycle methods in your app.
	 * It can give you information how much time was needed to open new Activity (or Application
	 * if it was launch screen)
	 * and where are possible bottlenecks.
	 */
	private void initializeDevMetrics() {
		if (BuildConfig.DEBUG) {
			AndroidDevMetrics.initWith(this);
		}
	}

	private void initializeExceptionHandler() {
		//        CrashHandler crashHandler = CrashHandler.getInstance();
		//        crashHandler.init(this);
	}

	private void initializeLakCanary() {
		// 检查内存泄露
		//		LeakCanary.install(this);
	}

	private void initializeInjector() {
		applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
	}
}
