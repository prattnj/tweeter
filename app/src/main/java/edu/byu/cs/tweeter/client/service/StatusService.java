package edu.byu.cs.tweeter.client.service;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.handler.SimpleSuccessHandler;
import edu.byu.cs.tweeter.client.observer_interface.DoubleParamSuccessObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public void loadMore_Feed(AuthToken authToken, User user, int pageSize, Status lastStatus, DoubleParamSuccessObserver<List<Status>, Boolean> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, pageSize, lastStatus, new GetItemsHandler<>(observer));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void loadMore_Story(AuthToken authToken, User user, int pageSize, Status lastStatus, DoubleParamSuccessObserver<List<Status>, Boolean> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken, user, pageSize, lastStatus, new GetItemsHandler<>(observer));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void postStatus(AuthToken authToken, Status newStatus, MainPresenter.PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken, newStatus, new SimpleSuccessHandler(observer));
        BackgroundTaskUtils.runTask(statusTask);
    }

}
