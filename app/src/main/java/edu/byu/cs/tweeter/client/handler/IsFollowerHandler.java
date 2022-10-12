package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.observer_interface.ParamSuccessObserver;

public class IsFollowerHandler extends BaseHandler {

    private final ParamSuccessObserver<Boolean> observer;

    public IsFollowerHandler(ParamSuccessObserver<Boolean> observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.success(isFollower);
    }
}
