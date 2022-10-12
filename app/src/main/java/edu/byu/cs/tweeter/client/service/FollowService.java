package edu.byu.cs.tweeter.client.service;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.handler.GetCountHandler;
import edu.byu.cs.tweeter.client.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.handler.SimpleSuccessHandler;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
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

    public void follow(AuthToken authToken, User selectedUser, MainPresenter.FollowObserver observer) {
        FollowTask followTask = new FollowTask(authToken, selectedUser, new SimpleSuccessHandler(observer));
        BackgroundTaskUtils.runTask(followTask);
    }

    public void unfollow(AuthToken authToken, User selectedUser, MainPresenter.UnfollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(authToken, selectedUser, new SimpleSuccessHandler(observer));
        BackgroundTaskUtils.runTask(unfollowTask);
    }

    public void isFollower(AuthToken authToken, User currUser, User selectedUser, MainPresenter.IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(authToken, currUser, selectedUser, new IsFollowerHandler(observer));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }

    public void getFollowerCount(AuthToken authToken, User user, MainPresenter.GetFollowerCountObserver observer) {
        GetCountTask countTask = new GetFollowersCountTask(authToken, user, new GetCountHandler(observer));
        BackgroundTaskUtils.runTask(countTask);
    }

    public void getFollowingCount(AuthToken authToken, User user, MainPresenter.GetFollowingCountObserver observer) {
        GetCountTask countTask = new GetFollowingCountTask(authToken, user, new GetCountHandler(observer));
        BackgroundTaskUtils.runTask(countTask);
    }

}