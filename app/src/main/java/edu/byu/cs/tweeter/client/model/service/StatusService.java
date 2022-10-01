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
import edu.byu.cs.tweeter.client.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {

    // OBSERVERS

    public interface FeedObserver {
        void displayMoreStatuses(List<Status> statuses, boolean hasMorePages);
        void displayUser(User user);
        void fail(String message, boolean isUserRelated);
    }

    public interface StoryObserver {
        void displayMoreStatuses(List<Status> statuses, boolean hasMorePages);
        void displayUser(User user);
        void fail(String message, boolean isUserRelated);
    }

    public interface MainObserver {
        void postStatusSuccess();
        void fail(String message);
    }



    // HANDLERS

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
                observer.fail("Failed to get status: " + message, false);
            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
                observer.fail("Failed to get status because of exception: " + ex.getMessage(), false);
            }
        }
    }

    private static class GetStoryHandler extends Handler {

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
                observer.fail("Failed to get status: " + message, false);
            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
                observer.fail("Failed to get status because of exception: " + ex.getMessage(), false);
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
                if (type == 1) feedObserver.fail("Failed to get user's profile: " + message, true);
                else storyObserver.fail("Failed to get user's profile: " + message, true);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                if (type == 1) feedObserver.fail("Failed to get user's profile because of exception: " + ex.getMessage(), true);
                else storyObserver.fail("Failed to get user's profile because of exception: " + ex.getMessage(), true);
            }
        }
    }

    private static class PostStatusHandler extends Handler {

        private final MainObserver observer;

        public PostStatusHandler(MainObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
            if (success) {
                observer.postStatusSuccess();
            } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
                observer.fail("Failed to post status: " + message);
            } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
                observer.fail("Failed to post status because of exception: " + ex.getMessage());
            }
        }
    }



    // SERVICE FUNCTIONS

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

    public void postStatus(AuthToken authToken, Status newStatus, MainObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(authToken, newStatus, new PostStatusHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }

}
