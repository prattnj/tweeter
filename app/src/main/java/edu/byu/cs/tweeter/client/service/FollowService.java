package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetCountsTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.handler.FollowHandler;
import edu.byu.cs.tweeter.client.handler.GetCountsHandler;
import edu.byu.cs.tweeter.client.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.observer_interface.MainObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public void loadMore_Following(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedPresenter<User>.PagedObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken, user, pageSize, lastFollowee, new GetItemsHandler<>(observer));
        BackgroundTaskUtils.runTask(getFollowingTask);
    }

    public void loadMore_Followers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PagedPresenter<User>.PagedObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken, user, pageSize, lastFollower, new GetItemsHandler<>(observer));
        BackgroundTaskUtils.runTask(getFollowersTask);
    }

    public void follow(AuthToken authToken, User selectedUser, MainObserver observer) {
        FollowTask followTask = new FollowTask(authToken, selectedUser, new FollowHandler(observer));
        BackgroundTaskUtils.runTask(followTask);
    }

    public void unfollow(AuthToken authToken, User selectedUser, MainObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, selectedUser, new UnfollowHandler(observer));
        BackgroundTaskUtils.runTask(unfollowTask);
    }

    public void isFollower(AuthToken authToken, User currUser, User selectedUser, MainObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, currUser, selectedUser, new IsFollowerHandler(observer));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }

    public void getCounts(AuthToken authToken, User user, MainObserver observer) {
        GetCountsTask countTask = new GetCountsTask(authToken, user, new GetCountsHandler(observer));
        BackgroundTaskUtils.runTask(countTask);
    }

}