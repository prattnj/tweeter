package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private final FollowService service;
    private final View view;

    private User lastFollower;
    private static final int PAGE_SIZE = 10;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public FollowersPresenter(View view) {
        this.view = view;
        service = new FollowService();
    }

    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean add);
        void addFollowers(List<User> followees);
        void displayUser(User user);
    }

    private class FollowersObserver implements FollowService.FollowersObserver {

        @Override
        public void displayMoreFollowers(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastFollower = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowers(followees);
            FollowersPresenter.this.hasMorePages = hasMorePages;
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

        @Override
        public void displayUser(User user) {
            view.displayUser(user);
        }

        @Override
        public void displayUserErrorMessage(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void displayUserException(Exception e) {
            view.displayMessage("Failed to get user's profile because of exception: " + e.getMessage());
        }
    }

    public void goToUser(String username) {
        service.getUserFollowers(Cache.getInstance().getCurrUserAuthToken(), username, new FollowersObserver());
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
