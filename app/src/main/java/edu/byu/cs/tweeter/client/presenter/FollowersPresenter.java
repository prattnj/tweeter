package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public FollowersPresenter(PagedView<User> view) {
        this.view = view;
    }

    @Override
    public void getItems(User targetUser) {
        isLoading = true;
        view.setLoading(true);
        fService.loadMore_Followers(Cache.getInstance().getCurrUserAuthToken(), targetUser, pageSize, lastItem, new PagedObserver());
    }

    @Override
    public String getDescription() {
        return "followers";
    }

}
