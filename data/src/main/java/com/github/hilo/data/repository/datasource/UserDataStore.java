package com.github.hilo.data.repository.datasource;

import com.github.hilo.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface UserDataStore {
	/**
	 * Get an {@link rx.Observable} which will emit a List of {@link UserEntity}.
	 */
	Observable<List<UserEntity>> userEntityList();

	/**
	 * Get an {@link rx.Observable} which will emit a {@link UserEntity} by its id.
	 *
	 * @param userId The id to retrieve user data.
	 */
	Observable<UserEntity> userEntityDetails(final int userId);

	Observable<UserEntity> userEntity();
}
