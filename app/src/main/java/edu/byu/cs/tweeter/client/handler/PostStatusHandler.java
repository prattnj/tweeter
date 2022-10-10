package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.observer_interface.MainObserver;

public class PostStatusHandler extends BaseHandler {

    private final MainObserver observer;

    public PostStatusHandler(MainObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.postStatusSuccess();
    }
}
