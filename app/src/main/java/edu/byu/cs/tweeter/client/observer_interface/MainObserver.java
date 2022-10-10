package edu.byu.cs.tweeter.client.observer_interface;

public abstract class MainObserver extends ServiceObserver {
    public abstract void getCountsSuccess(int count1, int count2);
    public abstract void isFollowingSuccess(boolean isFollower);
    public abstract void followSuccess();
    public abstract void unfollowSuccess();
    public abstract void postStatusSuccess();
    public abstract void enableFollowButton();
}
