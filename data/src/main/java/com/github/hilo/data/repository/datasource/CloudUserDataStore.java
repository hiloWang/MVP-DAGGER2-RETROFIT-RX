package com.github.hilo.data.repository.datasource;

import com.github.hilo.data.cache.UserCache;
import com.github.hilo.data.cache.UserCacheImpl;
import com.github.hilo.data.entity.UserEntity;
import com.github.hilo.data.net.ApiConnection;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * {@link UserDataStore} implementation based on connections to the api (Cloud).
 */
public class CloudUserDataStore implements UserDataStore {

	private final UserCache userCache;
	private final ApiConnection apiConnection;

	private final Action1<UserEntity> saveToCacheAction = userEntity -> {
		if (userEntity != null) {
			CloudUserDataStore.this.userCache.put(UserCacheImpl.USER + "",userEntity);
		}
	};

	public CloudUserDataStore(UserCache userCache,ApiConnection apiConnection) {
		this.userCache = userCache;
		this.apiConnection = apiConnection;
	}

	@Override public Observable<List<UserEntity>> userEntityList() {
		return null;
	}

	@Override public Observable<UserEntity> userEntityDetails(final int userId) {
		return /*this.restApi.userEntityById(userId).doOnNext(saveToCacheAction);*/ null;
	}

	@Override public Observable<UserEntity> userEntity() {
		return apiConnection.requestSyncCall()
												.requestUserEntityFromApi()
												.subscribeOn(Schedulers.io())
												.unsubscribeOn(Schedulers.computation())
												.observeOn(AndroidSchedulers.mainThread());
	}
}
