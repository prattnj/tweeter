package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    public interface FeedObserver {
        void displayMoreStatuses(List<Status> statuses, boolean hasMorePages);
        void displayErrorMessage(String message);
        void displayException(Exception e);
        void displayUser(User user);
        void displayUserErrorMessage(String message);
        void displayUserException(Exception e);
    }

    public interface StoryObserver {
        void displayMoreStatuses(List<Status> statuses, boolean hasMorePages);
        void displayErrorMessage(String message);
        void displayException(Exception e);
        void displayUser(User user);
        void displayUserErrorMessage(String message);
        void displayUserException(Exception e);
    }

    private class GetStoryHandler extends Handler {

        private final StoryObserver observer;

        public GetStoryHandler(StoryObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
                observer.displayMoreStatuses(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
                observer.displayErrorMessage(message);
            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    private static class GetFeedHandler extends Handler {

        private final FeedObserver observer;

        public GetFeedHandler(FeedObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
            if (success) {
                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.STATUSES_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
                observer.displayMoreStatuses(statuses, hasMorePages);
            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
                observer.displayErrorMessage(message);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    private static class GetUserHandler extends Handler {

        private FeedObserver feedObserver;
        private StoryObserver storyObserver;
        private final int type; // 1 (Feed) or 2 (Story), avoids duplication of this class

        public GetUserHandler(FeedObserver observer) {
            feedObserver = observer;
            type = 1;
        }
        public GetUserHandler(StoryObserver observer) {
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
                if (type == 1) feedObserver.displayUserErrorMessage(message);
                else storyObserver.displayUserErrorMessage(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                if (type == 1) feedObserver.displayUserException(ex);
                else storyObserver.displayUserException(ex);
            }
        }
    }

    public void loadMore_Feed(AuthToken authToken, User user, int pageSize, Status lastStatus, FeedObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(authToken, user, pageSize, lastStatus, new GetFeedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    public void loadMore_Story(AuthToken authToken, User user, int pageSize, Status lastStatus, StoryObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(authToken, user, pageSize, lastStatus, new GetStoryHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }

    public void getUser_Feed(AuthToken authToken, String clickable, FeedObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, clickable, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void getUser_Story(AuthToken authToken, String clickable, StoryObserver observer) {
        GetUserTask getUserTask = new GetUserTask(authToken, clickable, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

}
