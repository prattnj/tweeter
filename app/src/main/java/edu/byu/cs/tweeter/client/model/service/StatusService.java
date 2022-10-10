package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.observer_interface.MainObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public void loadMore_Feed(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedPresenter<Status>.PagedObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, pageSize, lastStatus, new GetItemsHandler<>(observer));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void loadMore_Story(AuthToken authToken, User user, int pageSize, Status lastStatus, PagedPresenter<Status>.PagedObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken, user, pageSize, lastStatus, new GetItemsHandler<>(observer));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void postStatus(AuthToken authToken, Status newStatus, MainObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken, newStatus, new PostStatusHandler(observer));
        BackgroundTaskUtils.runTask(statusTask);
    }

}
