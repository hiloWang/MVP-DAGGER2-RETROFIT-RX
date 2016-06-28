package com.github.hilo.di.modules;


import com.github.hilo.di.scope.PerActivity;
import com.github.hilo.domain.interactor.GetUserList;
import com.github.hilo.domain.interactor.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * 注意，因为UseCase是接口，该形参必须是接口的实现类且实现类的构造方法必须写上@Inject才可以注入
 * UserComponent接口中并没有返回该接口的实例对象，因为不想暴露给别人调用，既然不提供给调用者使用，
 * 为什么这里要写呢？因为可能在别的构造方法里的形参用到了该UseCase，所以凡是可以拿到UserComponent
 * 实例对象的类，间接也拥有UseCase实例
 */
@Module
public class UserModule {

	@Provides @PerActivity @Named("userList") public UseCase provideGetUserList(GetUserList getUserList) {
		return getUserList;
	}
}
