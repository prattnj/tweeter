package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public FollowingPresenter(PagedView<User> view) {
        this.view = view;
    }

    @Override
    public void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        fService.loadMore_Following(Cache.getInstance().getCurrUserAuthToken(), targetUser, pageSize, lastItem, new PagedObserver());
    }

    @Override
    public String getDescription() {
        return "following";
    }

}
