package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.observer_interface.SimpleSuccessObserver;

public class SimpleSuccessHandler extends BaseHandler {

    private final SimpleSuccessObserver observer;

    public SimpleSuccessHandler(SimpleSuccessObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.success();
    }

}
