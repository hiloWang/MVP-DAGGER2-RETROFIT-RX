package com.github.hilo.data.repository.datasource;

import com.github.hilo.data.cache.UserCache;
import com.github.hilo.data.net.ApiConnection;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Factory that creates different implementations of {@link UserDataStore}.
 */
@Singleton
public class UserDataStoreFactory {

    private final UserCache userCache;
    private final ApiConnection apiConnection;

    @Inject
    public UserDataStoreFactory(UserCache userCache, ApiConnection apiConnection) {
        if (userCache == null || apiConnection == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        this.userCache = userCache;
        this.apiConnection = apiConnection;
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
        return new CloudUserDataStore(userCache, apiConnection);
    }
}
