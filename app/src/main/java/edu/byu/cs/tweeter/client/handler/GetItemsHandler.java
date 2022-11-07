package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.abstract_task.PagedTask;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;

public class GetItemsHandler<T> extends BaseHandler {

    private final PagedPresenter<T>.PagedObserver observer;

    public GetItemsHandler(PagedPresenter<T>.PagedObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        List<T> items = (List<T>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
        observer.displayList(items, hasMorePages);
    }

}
