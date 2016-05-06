package com.github.hilo.data.net;

import com.github.hilo.data.entity.UserEntity;

import retrofit.http.GET;
import rx.Observable;

/**
 * RestApiA for retrieving data from the network.
 */
public interface RestApiA {

	@GET("api/day/history") Observable<UserEntity> requestUserEntityFromApi();

}

