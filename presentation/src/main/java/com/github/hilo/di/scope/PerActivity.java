package com.github.hilo.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Administrator on 2016/4/20.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {}
