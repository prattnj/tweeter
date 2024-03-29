package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.observer_interface.ParamSuccessObserver;
import edu.byu.cs.tweeter.client.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends Presenter {

    private final UserService service;
    private final View view;

    public LoginPresenter(LoginPresenter.View view) {
        this.view = view;
        this.service = new UserService();
    }

    public interface View extends Presenter.View {
        void displayInfoMessage(String message);
        void clearInfoMessage();
        void displayErrorMessage(String message);
        void clearErrorMessage();
        void navigateToMain(User user);
    }

    public class LoginObserver implements ParamSuccessObserver<User> {

        @Override
        public void success(User var) {
            view.displayInfoMessage("Hello " + var.getFirstName());
            view.clearErrorMessage();
            view.navigateToMain(var);
        }

        @Override
        public void handleFailure(String message) {
            view.clearInfoMessage();
            view.displayErrorMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayInfoMessage(exception.getMessage());
            exception.printStackTrace();
        }
    }



    // EXTRA PRESENTER FUNCTIONS

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
