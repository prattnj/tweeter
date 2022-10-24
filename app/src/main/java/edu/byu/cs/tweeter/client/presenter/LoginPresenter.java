package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    private final UserService service;
    private final View view;

    public LoginPresenter(LoginPresenter.View view) {
        this.view = view;
        this.service = new UserService();
    }

    public interface View {
        void displayInfoMessage(String message);
        void clearInfoMessage();
        void displayErrorMessage(String message);
        void clearErrorMessage();
        void navigateToMain(User user);
    }

    public class LoginObserver implements UserService.LoginObserver {

        @Override
        public void loginSuccess(User user) {
            view.displayInfoMessage("Hello " + user.getFirstName());
            view.clearErrorMessage();
            view.navigateToMain(user);
        }

        @Override
        public void loginFail(String message) {
            view.clearInfoMessage();
            view.displayErrorMessage(message);
        }

        @Override
        public void cacheSession(User loggedInUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
        }
    }

    public void initiateLogin(String username, String password) {
        String message = validateLogin(username, password);
        if (message == null) {
            view.clearErrorMessage();
            view.displayInfoMessage("Logging in...");
            service.login(username, password, new LoginObserver());
        } else {
            view.clearInfoMessage();
            view.displayErrorMessage(message);
        }
    }

    public String validateLogin(String username, String password) {
        if (username.charAt(0) != '@') return "Alias must begin with @.";
        if (username.length() < 2) return "Alias must contain 1 or more characters after the @.";
        if (password.length() == 0) return "Password cannot be empty.";
        return null;
    }

}
