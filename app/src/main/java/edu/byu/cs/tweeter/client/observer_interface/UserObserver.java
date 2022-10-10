package edu.byu.cs.tweeter.client.observer_interface;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserObserver extends ServiceObserver {

    public abstract void handleSuccess(User user);

    public void cacheSession(User user, AuthToken authToken) {
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);
    }
}
