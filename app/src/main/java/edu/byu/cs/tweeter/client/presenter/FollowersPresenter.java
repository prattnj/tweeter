package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    private final FollowService fService;
    private final UserService uService;
    private final View view;

    private User lastFollower;
    private static final int PAGE_SIZE = 10;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public FollowersPresenter(View view) {
        this.view = view;
        fService = new FollowService();
        uService = new UserService();
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

    public void goToUser(String username) {
        uService.getUser_Followers(Cache.getInstance().getCurrUserAuthToken(), username, new FollowersObserver());
    }

    public void loadMoreItems(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        fService.loadMore_Followers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new FollowersObserver());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

}
