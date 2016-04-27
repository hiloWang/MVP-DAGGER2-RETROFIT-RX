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
package com.github.hilo.data.repository;

import com.github.hilo.data.entity.mapper.UserEntityDataMapper;
import com.github.hilo.data.repository.datasource.UserDataStore;
import com.github.hilo.data.repository.datasource.UserDataStoreFactory;
import com.github.hilo.domain.User;
import com.github.hilo.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * {@link UserRepository} for retrieving user data.
 */
@Singleton
public class UserDataRepository implements UserRepository {

	private final UserEntityDataMapper userEntityDataMapper;
	private final UserDataStoreFactory userDataStoreFactory;

	/**
	 * Constructs a {@link UserRepository}.
	 *
	 * @param dataStoreFactory     A factory to construct different data source implementations.
	 * @param userEntityDataMapper {@link UserEntityDataMapper}.
	 */
	@Inject public UserDataRepository(UserDataStoreFactory dataStoreFactory,
					UserEntityDataMapper userEntityDataMapper) {
		this.userDataStoreFactory = dataStoreFactory;
		this.userEntityDataMapper = userEntityDataMapper;
	}

	@Override public Observable<List<User>> users() {
		return null;
	}

	@Override public Observable<User> user(int userId) {
		return null;
	}

	@SuppressWarnings("Convert2MethodRef") @Override public Observable<User> user() {
		//we always get all users from the cloud
		final UserDataStore userDataStore = this.userDataStoreFactory.createCloudDataStore();
		return userDataStore.userEntity().map(userEntity -> userEntityDataMapper.transform(userEntity));
	}

	//    @SuppressWarnings("Convert2MethodRef")
	//    @Override
	//    public Observable<List<User>> users() {
	//        //we always get all users from the cloud
	//        final UserDataStore userDataStore = this.userDataStoreFactory.createCloudDataStore();
	//        // userEntity转User
	//        return userDataStore.userEntity()
	//                .map((Func1<UserEntity, List<User>>) userEntity -> userEntityDataMapper
    // .transform(userEntity));
	//    }

	//  @SuppressWarnings("Convert2MethodRef")
	//  @Override
	//  public Observable<User> user(int userId) {
	//    final UserDataStore userDataStore = this.userDataStoreFactory.create(userId);
	//    return userDataStore.userEntityDetails(userId)
	//        .map(userEntity -> this.userEntityDataMapper.transform(userEntity));
	//  }
}
