/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hilo.data.repository.datasource;

import com.github.hilo.data.cache.UserCacheImpl;
import com.github.hilo.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * {@link UserDataStore} implementation based on file system data store.
 */
public class DiskUserDataStore implements UserDataStore {

	private final UserCacheImpl userCache;

	/**
	 * Construct a {@link UserDataStore} based file system data store.
	 *
	 * @param userCache A {@link UserCacheImpl} to cache data retrieved from the api.
	 */
	public DiskUserDataStore(UserCacheImpl userCache) {
		this.userCache = userCache;
	}

	@Override public Observable<List<UserEntity>> userEntityList() {
		throw new UnsupportedOperationException("Operation is not available!!!");
	}

	@Override public Observable<UserEntity> userEntityDetails(final int userId) {
		return /*this.userCache.get(userId);*/ null;
	}

	@Override public Observable<UserEntity> userEntity() {
		return null;
	}
}
