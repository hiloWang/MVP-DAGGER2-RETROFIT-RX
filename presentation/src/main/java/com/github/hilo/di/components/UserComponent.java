package com.github.hilo.di.components;

import com.github.hilo.di.modules.ActivityModule;
import com.github.hilo.di.modules.UserModule;
import com.github.hilo.di.scope.PerActivity;
import com.github.hilo.view.fragment.UserListFragment;

import dagger.Component;

/**
 * MainActivity的Component，是ActivityComponent的子类，对外提供绑定fragment的inject方法(参数是谁就在谁里面调用inject)，
 * 并拥有基类的getActivity方法，用于返回该MainActivity实例对象，这里要注意一下，即使基类里添加了applicationComponent依赖，
 * 这里也必须再写一遍，俩者不能共用
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class,UserModule.class})
public interface UserComponent extends ActivityComponent {

	void inject(UserListFragment fragment);
}
