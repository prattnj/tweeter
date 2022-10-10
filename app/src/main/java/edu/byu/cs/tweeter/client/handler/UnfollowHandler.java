package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.observer_interface.MainObserver;

public class UnfollowHandler extends BaseHandler {

    private final MainObserver observer;

    public UnfollowHandler(MainObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.unfollowSuccess();
        observer.enableFollowButton();
    }
}
