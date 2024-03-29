package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.observer_interface.DoubleParamSuccessObserver;
import edu.byu.cs.tweeter.client.service.FollowService;
import edu.byu.cs.tweeter.client.service.StatusService;
import edu.byu.cs.tweeter.client.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    protected UserService uService;
    protected StatusService sService;
    protected FollowService fService;
    protected int pageSize = 10;
    protected User targetUser;
    protected AuthToken authToken;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading;
    protected PagedView<T> view;

    public PagedPresenter() {
        uService = new UserService();
        sService = new StatusService();
        fService = new FollowService();
    }

    public interface PagedView<U> extends View {
        void setLoading(boolean isLoading);
        void addItems(List<U> items);
        void navigateToUser(User user);
    }

    public class PagedObserver implements DoubleParamSuccessObserver<List<T>, Boolean> {

        public void displayUser(User user) {
            view.navigateToUser(user);
        }

        public void displayList(List<T> items, boolean hasMore) {
            if (items == null) return;
            isLoading = false;
            view.setLoading(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            view.addItems(items);
            hasMorePages = hasMore;
        }

        @Override
        public void success(List<T> foo, Boolean bar) {
            displayList(foo, bar);
        }

        @Override
        public void handleFailure(String message) {
            boolean isUserRelated = lastItem.getClass() == User.class;
            if (!isUserRelated) {
                isLoading = false;
                view.setLoading(false);
            }
            view.displayMessage("Failed to do " + getDescription() + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to do " + getDescription() + " due to exception: " + exception.getMessage());
        }
    }

    public void getUser(String username) {
        uService.getUser(Cache.getInstance().getCurrUserAuthToken(), username, new PagedPresenter<User>.PagedObserver());
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    // ABSTRACT METHODS
    public abstract void getItems(User targetUser);
    public abstract String getDescription();

}
