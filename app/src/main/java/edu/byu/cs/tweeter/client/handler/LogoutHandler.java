package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.observer_interface.UserObserver;

public class LogoutHandler extends BaseHandler {

    private final UserObserver observer;

    public LogoutHandler(UserObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.handleSuccess(null);
    }
}
