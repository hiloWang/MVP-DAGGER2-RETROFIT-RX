package com.github.hilo.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Administrator on 2016/4/20.
 * @Scope
 *      被作注解的依赖会变成单例，但是这会与component的生命周期关联(不是整个应用Application的生命周期);
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}
