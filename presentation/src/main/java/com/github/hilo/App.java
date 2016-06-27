package com.github.hilo;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.github.hilo.di.components.ApplicationComponent;
import com.github.hilo.di.components.DaggerApplicationComponent;
import com.github.hilo.di.modules.ApplicationModule;

import java.lang.ref.WeakReference;

public class App extends Application {

	private ApplicationComponent applicationComponent;

	@Override public void onCreate() {
		super.onCreate();

		this.initializeInjector();
		this.initializeLakCanary();
		this.initializeExceptionHandler();
		this.initializeDevMetrics();
		this.initializeRuntimeMemoryManager();
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

	private void initializeRuntimeMemoryManager() {
		if (dummyHandler == null) dummyHandler = new DummyHandler(this);
		dummyHandler.obtainMessage().sendToTarget();
	}

	DummyHandler dummyHandler;

	private Runnable dummyRunnable = new Runnable() {
		@Override public void run() {
//			if (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() > Runtime.getRuntime().maxMemory() * 0.8) {
//
//			}
			onTrimMemory(TRIM_MEMORY_RUNNING_LOW);
			if(Runtime.getRuntime().totalMemory() > 25000000) {
				Log.e("HILO","释放内存中");
				System.gc();
			}
			dummyHandler.obtainMessage().sendToTarget();
		}
	};

	private static class DummyHandler extends Handler {

		private WeakReference<App> weakReference;

		DummyHandler(App app) {
			weakReference = new WeakReference<>(app);
		}

		@Override public void handleMessage(Message msg) {
			final App mApp = weakReference.get();
			if (mApp != null) {
				mApp.dummyHandler.postDelayed(mApp.dummyRunnable,3000);
			}
		}
	}
}
