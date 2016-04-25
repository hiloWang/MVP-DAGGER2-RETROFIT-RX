package com.github.hilo.di.components;

import com.github.hilo.di.modules.ApplicationModule;
import com.github.hilo.domain.executor.PostExecutionThread;
import com.github.hilo.domain.executor.ThreadExecutor;
import com.github.hilo.domain.repository.UserRepository;
import com.github.hilo.util.RxUtils;
import com.github.hilo.util.ToastUtils;
import com.github.hilo.view.activity.BaseAppCompatActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseAppCompatActivity baseAppCompatActivity);

    ToastUtils toast();

    RxUtils rx();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    UserRepository userRepository();
}
