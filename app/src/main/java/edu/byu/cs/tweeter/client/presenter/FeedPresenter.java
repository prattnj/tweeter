package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    public FeedPresenter(PagedView<Status> view) {
        this.view = view;
    }

    @Override
    public void getItems(User targetUser) {
        isLoading = true;
        view.setLoading(true);
        sService.loadMore_Feed(Cache.getInstance().getCurrUserAuthToken(), targetUser, pageSize, lastItem, new PagedObserver());
    }

    @Override
    public String getDescription() {
        return "feed";
    }

}
