package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.observer_interface.MainObserver;

public class FollowHandler extends BaseHandler {

    private final MainObserver observer;

    public FollowHandler(MainObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.followSuccess();
        observer.enableFollowButton();
    }
}
