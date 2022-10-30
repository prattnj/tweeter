package edu.byu.cs.tweeter.client.presenter;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.observer_interface.ParamSuccessObserver;
import edu.byu.cs.tweeter.client.observer_interface.SimpleSuccessObserver;
import edu.byu.cs.tweeter.client.service.FollowService;
import edu.byu.cs.tweeter.client.service.StatusService;
import edu.byu.cs.tweeter.client.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter {

    private final UserService uService;
    private final FollowService fService;
    private final StatusService sService;
    private final View view;

    public MainPresenter(View view) {
        this.view = view;
        uService = new UserService();
        fService = new FollowService();
        sService = new StatusService();
    }

    public interface View extends Presenter.View {
        void logOutUpdateView();
        void setFollowerCount(int count);
        void setFollowingCount(int count);
        void setFollowButton(boolean isFollower);
        void setEnableFollowButton(boolean b);
        void cancelPostToast();
        void updateView_Follow();
        void updateView_Unfollow();
    }

    public class GetFollowerCountObserver implements ParamSuccessObserver<Integer> {

        @Override
        public void success(Integer var) {
            view.setFollowerCount(var);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get follower count:" + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get follower count due to exception: " + exception.getMessage());
        }
    }

    public class GetFollowingCountObserver implements ParamSuccessObserver<Integer> {

        @Override
        public void success(Integer var) {
            view.setFollowingCount(var);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get following count:" + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get following count due to exception: " + exception.getMessage());
        }
    }

    public class FollowObserver implements SimpleSuccessObserver {

        @Override
        public void success() {
            view.updateView_Follow();
            view.setEnableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to follow due to exception: " + exception.getMessage());
        }
    }

    public class UnfollowObserver implements SimpleSuccessObserver {

        @Override
        public void success() {
            view.updateView_Unfollow();
            view.setEnableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to unfollow due to exception: " + exception.getMessage());
        }
    }

    public class IsFollowerObserver implements ParamSuccessObserver<Boolean> {

        @Override
        public void success(Boolean var) {
            view.setFollowButton(var);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to do isFollower: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to do isFollower due to exception: " + exception.getMessage());
        }
    }

    public class PostStatusObserver implements SimpleSuccessObserver {

        @Override
        public void success() {
            view.cancelPostToast();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to post status due to exception: " + exception.getMessage());
        }
    }

    public class LogoutObserver implements SimpleSuccessObserver {

        @Override
        public void success() {
            Cache.getInstance().clearCache();
            view.logOutUpdateView();
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to logout due to exception: " + exception.getMessage());
        }
    }





    // EXTRA PRESENTER FUNCTIONS

    public void logout() {
        uService.logout(new LogoutObserver());
    }

    public void follow(User user) {
        fService.follow(Cache.getInstance().getCurrUserAuthToken(), user, new FollowObserver());
    }

    public void unfollow(User user) {
        fService.unfollow(Cache.getInstance().getCurrUserAuthToken(), user, new UnfollowObserver());
    }

    public void isFollower(User user) {
        fService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), user, new IsFollowerObserver());
    }

    public void updateFollowerCount(User selectedUser) {
        fService.getFollowerCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowerCountObserver());
    }

    public void updateFollowingCount(User selectedUser) {
        fService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowingCountObserver());
    }

    public void postStatus(String post) throws ParseException {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
        sService.postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusObserver());
    }



    // DO NOT TOUCH

    public String getFormattedDateTime() throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(Objects.requireNonNull(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8))));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);
                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

}
