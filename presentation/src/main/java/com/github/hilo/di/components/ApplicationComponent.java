package com.github.hilo.di.components;

import com.github.hilo.di.modules.ApplicationModule;
import com.github.hilo.domain.executor.PostExecutionThread;
import com.github.hilo.domain.executor.ThreadExecutor;
import com.github.hilo.domain.repository.UserRepository;
import com.github.hilo.util.RxBus;
import com.github.hilo.util.RxUtils;
import com.github.hilo.util.ToastUtils;
import com.github.hilo.view.activity.BaseAppCompatActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * ApplicationComponent管理全局类的实例，生命周期与app一样长,
 * Component类必须是接口或者抽象类，起着桥梁的作用，
 * 一端连着目标类（@Inject），另一端连着Module管理的实例对象(可以有多个Mudule、用modules管理)
 * Component提供返回该实例的方法，意味着调用者只需拿到Component实例即可操纵这些全局类的实例
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

	void inject(BaseAppCompatActivity baseAppCompatActivity);

	ToastUtils toast();

	RxUtils rx();

	ThreadExecutor threadExecutor();

	PostExecutionThread postExecutionThread();

	UserRepository userRepository();

	RxBus rxBus();
}
