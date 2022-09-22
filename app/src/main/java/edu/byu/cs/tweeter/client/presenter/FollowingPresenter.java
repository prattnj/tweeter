package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {

    private static final int PAGE_SIZE = 10;
    private FollowService service;
    private User lastFollowee;

    private boolean hasMorePages;
    private boolean isLoading = false;
    private View view;

    public FollowingPresenter(View view) {
        this.view = view;
        service = new FollowService();
    }

    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean add);
        void addFollowees(List<User> followees);
    }

    private class FollowingObserver implements FollowService.FollowingObserver {

        @Override
        public void displayMoreFollowees(List<User> followees, boolean hasMorePages) {
            view.setLoadingFooter(false);
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addFollowees(followees);
            FollowingPresenter.this.hasMorePages = hasMorePages;
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
        service.loadMoreFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowee, new FollowingObserver());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

}
