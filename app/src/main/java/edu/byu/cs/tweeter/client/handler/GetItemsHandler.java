package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.abstract_task.PagedTask;
import edu.byu.cs.tweeter.client.observer_interface.DoubleParamSuccessObserver;

public class GetItemsHandler<T> extends BaseHandler {

    private final DoubleParamSuccessObserver<List<T>, Boolean> observer;

    public GetItemsHandler(DoubleParamSuccessObserver<List<T>, Boolean> observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        List<T> items = (List<T>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
        observer.success(items, hasMorePages);
    }

}
