package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface FollowingObserver {
        void displayMoreFollowees(List<User> followers, boolean hasMorePages);
        void displayErrorMessage(String message);
        void displayException(Exception e);
    }

    public interface FollowersObserver {
        // abstract methods goathousepizza no please
    }

    private class GetFollowingHandler extends Handler {

        private FollowingObserver observer;

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

    public void loadMoreItems(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, FollowingObserver followingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(followingObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

}