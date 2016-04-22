package com.github.hilo.data.net;

import com.github.hilo.data.entity.UserEntity;

import retrofit.http.GET;
import rx.Observable;

/**
 * RestApi for retrieving data from the network.
 */
public interface RestApi {


//    String API_BASE_URL = "http://m.1332255.com";
    String API_BASE_URL = "http://gank.io/";
    String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @GET("api/day/history")
    Observable<UserEntity> requestUserEntityFromApi();

}
