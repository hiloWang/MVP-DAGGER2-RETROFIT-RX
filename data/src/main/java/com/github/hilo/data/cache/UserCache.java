package com.github.hilo.data.cache;

import com.anupcowkur.reservoir.ReservoirGetCallback;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * An interface representing a user Cache.
 */
public interface UserCache {

    /**
     * Puts and element into the cache.
     *
     * @param entity Element to insert in the cache.
     */
    void put(String key, Object entity);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired(String key);

    /**
     * delete element in the cache
     *
     * @param key
     */
    void delete(String key);

    /**
     * deleteAsync key which that if contains the key And put the new key into the cache
     *
     * @param key
     * @param entity
     */
    void refresh(String key, Object entity);

    <T> Observable<T> get(String key, Class<T> clazz);

    <T> Observable<T> get(Class<T> clazz);

    <T> void get(final String key, final Type typeOfT, final ReservoirGetCallback<T> callback);

    <T> void get(final String key, final Class<T> clazz, final ReservoirGetCallback<T> callback);


}
