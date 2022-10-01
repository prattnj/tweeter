package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    private final StatusService service;
    private final View view;

    private Status lastStatus;
    private static final int PAGE_SIZE = 10;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public FeedPresenter(View view) {
        this.view = view;
        service = new StatusService();
    }

    public interface View {
        void displayMessage(String s);
        void setLoadingFooter(boolean b);
        void addStatuses(List<Status> statuses);
        void displayUser(User user);
    }

    public class FeedObserver implements StatusService.FeedObserver {

        @Override
        public void displayMoreStatuses(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addStatuses(statuses);
            FeedPresenter.this.hasMorePages = hasMorePages;
        }

        @Override
        public void displayUser(User user) {
            view.displayUser(user);
        }

        @Override
        public void fail(String message, boolean isUserRelated) {
            if (!isUserRelated) {
                isLoading = false;
                view.setLoadingFooter(false);
            }
            view.displayMessage(message);
        }
    }

    public void goToUser(String clickable) {
        service.getUser_Feed(Cache.getInstance().getCurrUserAuthToken(), clickable, new FeedObserver());
    }

    public void loadMoreItems(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        service.loadMore_Feed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new FeedObserver());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

}
