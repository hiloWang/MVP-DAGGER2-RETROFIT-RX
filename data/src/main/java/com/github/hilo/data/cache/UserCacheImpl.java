package com.github.hilo.data.cache;

import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirDeleteCallback;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * UserCacheImpl 本地数据存储
 */
@Singleton
public class UserCacheImpl implements UserCache {

	public static final int USER = 22061;

	@Inject public UserCacheImpl() {

	}

	@Override public void put(String key,Object object) {
		if (object == null) return;
		Reservoir.putAsync(key,object,new ReservoirPutCallback() {
			@Override public void onSuccess() {
				Log.i("log","Put success: key=" + key + " object=" + object.getClass());
			}

			@Override public void onFailure(Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override public void delete(String key) {
		if (this.isExpired(key)) Reservoir.deleteAsync(key);
	}

	@Override public void refresh(String key,Object entity) {
		if (this.isExpired(key)) {
			Reservoir.deleteAsync(key,new ReservoirDeleteCallback() {
				@Override public void onSuccess() {
					UserCacheImpl.this.put(key,entity);
				}

				@Override public void onFailure(Exception e) {
					e.printStackTrace();
				}
			});
		} else {
			UserCacheImpl.this.put(key,entity);
		}
	}

	@Override public boolean isExpired(String key) {
		try {
			return Reservoir.contains(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override public <T> Observable<T> get(String key,Class<T> clazz) {
		return Reservoir.getAsync(key,clazz);

	}

	@Override public <T> Observable<T> get(Class<T> clazz) {
		String key = clazz.getSimpleName();
		return get(key,clazz);
	}

	@Override
	public <T> void get(final String key,final Type typeOfT,final ReservoirGetCallback<T> callback) {
		Reservoir.getAsync(key,typeOfT,callback);
	}

	@Override
	public <T> void get(final String key,final Class<T> clazz,
					final ReservoirGetCallback<T> callback) {
		Reservoir.getAsync(key,clazz,callback);
	}
}
