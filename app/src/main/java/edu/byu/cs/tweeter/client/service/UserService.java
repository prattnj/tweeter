package edu.byu.cs.tweeter.client.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    // OBSERVERS

    public interface LoginObserver {
        void loginSuccess(User user);
        void loginFail(String message);
        void cacheSession(User loggedInUser, AuthToken authToken);
    }

    public interface RegisterObserver {
        void registerSuccess(User registeredUser);
        void registerFail(String message);
        void cacheSession(User registeredUser, AuthToken authToken);
    }

    public interface MainObserver {
        void logoutSuccess();
        void fail(String message);
    }



    // HANDLERS

    private static class LoginHandler extends Handler {

        private final LoginObserver observer;

        public LoginHandler(LoginObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);
                observer.cacheSession(loggedInUser, authToken); // Cache user session information
                observer.loginSuccess(loggedInUser);
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                observer.loginFail("Failed to login: " + message);
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                observer.loginFail("Failed to login because of exception: " + ex.getMessage());
            }
        }
    }

    private static class RegisterHandler extends Handler {

        private final RegisterObserver observer;

        public RegisterHandler(RegisterObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
            if (success) {
                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
                observer.cacheSession(registeredUser, authToken);
                observer.registerSuccess(registeredUser);
            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                observer.registerFail("Failed to register: " + message);
            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                observer.registerFail("Failed to register because of exception: " + ex.getMessage());
            }
        }
    }

    private static class LogoutHandler extends Handler {

        private final MainObserver observer;

        public LogoutHandler(MainObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
            if (success) {
                observer.logoutSuccess();
            } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
                observer.fail("Failed to logout: " + message);
            } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
                observer.fail("Failed to logout because of exception: " + ex.getMessage());
            }
        }
    }

    private static class GetUserFollowHandler extends Handler {

        private FollowService.FollowingObserver followingObserver;
        private FollowService.FollowersObserver followersObserver;
        private final int type; // 1 (Following) or 2 (Followers), avoids duplication of this class

        public GetUserFollowHandler(FollowService.FollowingObserver observer) {
            followingObserver = observer;
            type = 1;
        }
        public GetUserFollowHandler(FollowService.FollowersObserver observer) {
            followersObserver = observer;
            type = 2;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                if (type == 1) followingObserver.displayUser(user);
                else followersObserver.displayUser(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                if (type == 1) followingObserver.fail("Failed to get user's profile: " + message, true);
                else followersObserver.fail("Failed to get user's profile: " + message, true);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                if (type == 1) followingObserver.fail("Failed to get user's profile because of exception: " + ex.getMessage(), true);
                else followersObserver.fail("Failed to get user's profile because of exception: " + ex.getMessage(), true);
            }
        }
    }

    private static class GetUserStatusHandler extends Handler {

        private StatusService.FeedObserver feedObserver;
        private StatusService.StoryObserver storyObserver;
        private final int type; // 1 (Feed) or 2 (Story), avoids duplication of this class

        public GetUserStatusHandler(StatusService.FeedObserver observer) {
            feedObserver = observer;
            type = 1;
        }
        public GetUserStatusHandler(StatusService.StoryObserver observer) {
            storyObserver = observer;
            type = 2;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                if (type == 1) feedObserver.displayUser(user);
                else storyObserver.displayUser(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                if (type == 1) feedObserver.fail("Failed to get user's profile: " + message, true);
                else storyObserver.fail("Failed to get user's profile: " + message, true);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                if (type == 1) feedObserver.fail("Failed to get user's profile because of exception: " + ex.getMessage(), true);
                else storyObserver.fail("Failed to get user's profile because of exception: " + ex.getMessage(), true);
            }
        }
    }



    // SERVICE FUNCTIONS

    public void login(String username, String password, LoginObserver observer) {
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, RegisterObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password, imageBytesBase64, new RegisterHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    public void logout(MainObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

    public void getUser_Following(AuthToken currUserAuthToken, String username, FollowService.FollowingObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken, username, new GetUserFollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void getUser_Followers(AuthToken currUserAuthToken, String username, FollowService.FollowersObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken, username, new GetUserFollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void getUser_Feed(AuthToken authToken, String clickable, StatusService.FeedObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, clickable, new GetUserStatusHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void getUser_Story(AuthToken authToken, String clickable, StatusService.StoryObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, clickable, new GetUserStatusHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

}