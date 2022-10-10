package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.observer_interface.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
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
    //protected boolean isGettingUser;
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

    public class PagedObserver extends ServiceObserver {

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
        public void handleFailure(String message) {
            boolean isUserRelated = false;
            User user = new User();
            if (lastItem.getClass() == user.getClass()) isUserRelated = true;
            message = "Failed to do: " + getDescription();
            if (!isUserRelated) {
                isLoading = false;
                view.setLoading(false);
            }
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            String message = "Failed to do " + getDescription() + " because of exception: " + exception.getMessage();
            view.displayMessage(message);
        }
    }

    public void loadMoreItems() {
        isLoading = true;
        view.setLoading(true);
        getItems(Cache.getInstance().getCurrUserAuthToken(), targetUser, pageSize, lastItem);
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
    public abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);
    public abstract String getDescription();

}
