package com.github.hilo.di.modules;

import android.content.Context;

import com.github.hilo.data.cache.UserCache;
import com.github.hilo.data.cache.UserCacheImpl;
import com.github.hilo.data.executor.JobExecutor;
import com.github.hilo.data.net.ApiConnection;
import com.github.hilo.data.net.ApiConnectionImpl;
import com.github.hilo.data.repository.UserDataRepository;
import com.github.hilo.domain.executor.PostExecutionThread;
import com.github.hilo.domain.executor.ThreadExecutor;
import com.github.hilo.domain.repository.UserRepository;
import com.github.hilo.executor.UIThread;
import com.github.hilo.util.RxBus;
import com.github.hilo.util.RxUtils;
import com.github.hilo.util.ToastUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module类，管理着其他类的实例对象(Module与Provides为了解决第三方库而生)，
 * 这里的Provides起着建立桥梁的作用（与Component另一端目标类（@Inject）建立关系）;
 * 注：如果是返回值的类型接口，则形参必须是接口的实现类才可以，并在实现类的构造函数上@Inject注入依赖，如果返回值并非接口，则一般通过return new X();的方式返回该实例对象
 */
@Module
public class ApplicationModule {

	Context context;

	public ApplicationModule(Context context) {
		this.context = context;
	}

	@Provides @Singleton public Context provideContext() {
		return context;
	}

	@Provides @Singleton public UserCache provideUserCache(UserCacheImpl userCache) {
		return userCache;
	}

	@Provides @Singleton public ToastUtils provideToastUtils() {
		return new ToastUtils(context);
	}

	@Provides @Singleton ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
		return jobExecutor;
	}

	@Provides @Singleton PostExecutionThread providePostExecutionThread(UIThread uiThread) {
		return uiThread;
	}

	@Provides @Singleton UserRepository provideUserRepository(UserDataRepository userDataRepository) {
		return userDataRepository;
	}

	@Provides @Singleton public RxUtils provideRxUtils() {
		return new RxUtils();
	}

	@Provides @Singleton public ApiConnection provideApiConnection(ApiConnectionImpl apiConnection) {
		return apiConnection;
	}

	@Provides @Singleton public RxBus provideRxBus() {
		return new RxBus();
	}
}
