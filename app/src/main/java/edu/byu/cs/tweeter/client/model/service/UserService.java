package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.handler.LoginHandler;
import edu.byu.cs.tweeter.client.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.handler.RegisterHandler;
import edu.byu.cs.tweeter.client.observer_interface.UserObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public void login(String username, String password, UserObserver observer) {
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(observer));
        BackgroundTaskUtils.runTask(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, UserObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password, imageBytesBase64, new RegisterHandler(observer));
        BackgroundTaskUtils.runTask(registerTask);
    }

    public void logout(UserObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        BackgroundTaskUtils.runTask(logoutTask);
    }

    public void getUser(AuthToken currUserAuthToken, String username, PagedPresenter<User>.PagedObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken, username, new GetUserHandler<>(observer));
        BackgroundTaskUtils.runTask(getUserTask);
    }

}
