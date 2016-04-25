package com.github.hilo.data.net;

import com.github.hilo.data.entity.UserEntity;

import retrofit.http.GET;
import rx.Observable;

/**
 * RestApi for retrieving data from the network.
 */
public interface RestApi {

    @GET("api/day/history")
    Observable<UserEntity> requestUserEntityFromApi();

}
