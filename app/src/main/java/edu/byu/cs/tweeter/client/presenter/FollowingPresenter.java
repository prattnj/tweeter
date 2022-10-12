package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public FollowingPresenter(PagedView<User> view) {
        this.view = view;
    }

    @Override
    public void getItems(User targetUser) {
        isLoading = true;
        view.setLoading(true);
        fService.loadMore_Following(Cache.getInstance().getCurrUserAuthToken(), targetUser, pageSize, lastItem, new PagedObserver());
    }

    @Override
    public String getDescription() {
        return "following";
    }

}
