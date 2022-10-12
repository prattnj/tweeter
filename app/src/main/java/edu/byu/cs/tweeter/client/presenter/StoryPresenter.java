package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public StoryPresenter(PagedView<Status> view) {
        this.view = view;
    }

    @Override
    public void getItems(User targetUser) {
        isLoading = true;
        view.setLoading(true);
        sService.loadMore_Story(Cache.getInstance().getCurrUserAuthToken(), targetUser, pageSize, lastItem, new PagedObserver());
    }

    @Override
    public String getDescription() {
        return "story";
    }

}
