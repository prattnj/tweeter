package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

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

    public interface View {
        void displayMessage(String message);
        void logOutUpdateView();
        void setFollowerCount(int count);
        void setFolloweeCount(int count);
        void setFollowButton(boolean isFollower);
        void setEnableFollowButton(boolean b);
        void cancelPostToast();
        void updateView_Follow();
        void updateView_Unfollow();
    }

    public class MainObserver implements UserService.MainObserver, FollowService.MainObserver, StatusService.MainObserver {

        @Override
        public void logoutSuccess() {
            view.logOutUpdateView();
        }

        @Override
        public void getFollowersCountSuccess(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void getFollowingCountSuccess(int count) {
            view.setFolloweeCount(count);
        }

        @Override
        public void isFollowingSuccess(boolean isFollower) {
            view.setFollowButton(isFollower);
        }

        @Override
        public void followSuccess() {
            view.updateView_Follow();
        }

        @Override
        public void unfollowSuccess() {
            view.updateView_Unfollow();
        }

        @Override
        public void postStatusSuccess() {
            view.cancelPostToast();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public void fail(String message) {
            view.displayMessage(message);
        }

        @Override
        public void enableFollowButton() {
            view.setEnableFollowButton(true);
        }
    }

    public void logout() {
        uService.logout(new MainObserver());
    }

    public void follow(User user) {
        fService.follow(Cache.getInstance().getCurrUserAuthToken(), user, new MainObserver());
    }

    public void unfollow(User user) {
        fService.unfollow(Cache.getInstance().getCurrUserAuthToken(), user, new MainObserver());
    }

    public void isFollower(User user) {
        fService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), user, new MainObserver());
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        fService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new MainObserver());
        fService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new MainObserver());
        //ExecutorService executor = Executors.newFixedThreadPool(2);
    }

    public void postStatus(String post) throws ParseException {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
        sService.postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new MainObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
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
