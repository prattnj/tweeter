package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface FollowingObserver {
        void displayMoreFollowees(List<User> followers, boolean hasMorePages);
        void displayErrorMessage(String message);
        void displayException(Exception e);
        void displayUser(User user);
        void displayUserErrorMessage(String message);
        void displayUserException(Exception e);
    }

    public interface FollowersObserver {
        void displayMoreFollowers(List<User> followers, boolean hasMorePages);
        void displayErrorMessage(String message);
        void displayException(Exception e);
        void displayUser(User user);
        void displayUserErrorMessage(String message);
        void displayUserException(Exception e);
    }

    private static class GetFollowingHandler extends Handler {

        private final FollowingObserver observer;

        public GetFollowingHandler(FollowingObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.displayMoreFollowees(followees, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                observer.displayErrorMessage(message);
            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    private static class GetFollowersHandler extends Handler {

        private final FollowersObserver observer;

        public GetFollowersHandler(FollowersObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {
                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
                observer.displayMoreFollowers(followers, hasMorePages);
            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                observer.displayErrorMessage(message);
            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    private static class GetUserHandler extends Handler {

        private FollowingObserver followingObserver;
        private FollowersObserver followersObserver;
        private final int type; // 1 (Following) or 2 (Followers), avoids duplication of this class

        public GetUserHandler(FollowingObserver observer) {
            followingObserver = observer;
            type = 1;
        }
        public GetUserHandler(FollowersObserver observer) {
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
                if (type == 1) followingObserver.displayUserErrorMessage(message);
                else followersObserver.displayUserErrorMessage(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                if (type == 1) followingObserver.displayUserException(ex);
                else followersObserver.displayUserException(ex);
            }
        }
    }

    public void loadMore_Following(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, FollowingObserver followingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(followingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void loadMore_Followers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, FollowersObserver followersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(followersObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);
    }

    public void getUser_Following(AuthToken currUserAuthToken, String username, FollowingObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                username, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void getUser_Followers(AuthToken currUserAuthToken, String username, FollowersObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                username, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

}