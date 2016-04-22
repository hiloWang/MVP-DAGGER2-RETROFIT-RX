package com.github.hilo.data.repository.datasource;

import com.github.hilo.data.cache.UserCache;
import com.github.hilo.data.entity.mapper.UserEntityGsonMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Factory that creates different implementations of {@link UserDataStore}.
 */
@Singleton
public class UserDataStoreFactory {

    private final UserCache userCache;
    private final UserEntityGsonMapper userEntityGsonMapper;

    @Inject
    public UserDataStoreFactory(UserCache userCache, UserEntityGsonMapper userEntityGsonMapper) {
        if (userCache == null || userEntityGsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        this.userCache = userCache;
        this.userEntityGsonMapper = userEntityGsonMapper;
    }

//  /**
//   * Create {@link UserDataStore} from a user id.
//   */
//  public UserDataStore create(int userId) {
//    UserDataStore userDataStore;
//
//    if (this.userCache.isExpired(C) != null && this.userCache.isCached(userId)) {
//      userDataStore = new DiskUserDataStore(this.userCache);
//    } else {
//      userDataStore = createCloudDataStore();
//    }
//
//    return userDataStore;
//  }

    /**
     * Create {@link UserDataStore} to retrieve data from the Cloud.
     */
    public UserDataStore createCloudDataStore() {
        return new CloudUserDataStore(userCache, userEntityGsonMapper.getGson());
    }
}
