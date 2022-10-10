package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountsTask;
import edu.byu.cs.tweeter.client.observer_interface.MainObserver;

public class GetCountsHandler extends BaseHandler {

    private final MainObserver observer;

    public GetCountsHandler(MainObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        int followersCount = msg.getData().getInt(GetCountsTask.COUNT_KEY1);
        int followingCount = msg.getData().getInt(GetCountsTask.COUNT_KEY2);
        observer.getCountsSuccess(followersCount, followingCount);
    }
}
