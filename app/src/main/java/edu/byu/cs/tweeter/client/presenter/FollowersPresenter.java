package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private static final int PAGE_SIZE = 10;
    private FollowService service;
    private User lastFollower;

    private boolean hasMorePages;
    private boolean isLoading = false;
    private FollowersPresenter.View view;

    public FollowersPresenter(View view) {
        this.view = view;
        service = new FollowService();
    }

    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean add);
        void addFollowers(List<User> followees);
    }

    private class FollowersObserver implements FollowService.FollowersObserver {

        @Override
        public void displayMoreFollowers(List<User> followees, boolean hasMorePages) {
            view.setLoadingFooter(false);
            lastFollower = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowers(followees);
            FollowersPresenter.this.hasMorePages = hasMorePages;
            isLoading = false;
        }

        @Override
        public void displayErrorMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void displayException(Exception e) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following because of exception: " + e.getMessage());
        }
    }

    public void loadMoreItems(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        service.loadMoreFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new FollowersObserver());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

}
