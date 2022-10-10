package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.observer_interface.MainObserver;

public class IsFollowerHandler extends BaseHandler {

    private final MainObserver observer;

    public IsFollowerHandler(MainObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        // If logged in user if a follower of the selected user, display the follow button as "following"
        observer.isFollowingSuccess(isFollower);
    }
}
