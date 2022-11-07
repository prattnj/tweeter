package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.abstract_task.GetCountTask;
import edu.byu.cs.tweeter.client.observer_interface.ParamSuccessObserver;

public class GetCountHandler extends BaseHandler {

    private final ParamSuccessObserver<Integer> observer;

    public GetCountHandler(ParamSuccessObserver<Integer> observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        int count = msg.getData().getInt(GetCountTask.COUNT_KEY);
        observer.success(count);
    }
}
