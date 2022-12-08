package edu.byu.cs.tweeter.client.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.observer_interface.DoubleParamSuccessObserver;
import edu.byu.cs.tweeter.client.observer_interface.ParamSuccessObserver;
import edu.byu.cs.tweeter.client.observer_interface.SimpleSuccessObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PostStatusTest {

    private CountDownLatch countDownLatch;

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void testPostStatus() throws InterruptedException {

        resetCountDownLatch();

        // Spy on the services and mock the observers
        UserService userServiceSpy = Mockito.spy(new UserService());
        StatusService statusServiceSpy = Mockito.spy(new StatusService());
        LoginObserver loginObserver = new LoginObserver();
        PostStatusObserver postStatusObserver = new PostStatusObserver();
        GetStoryObserver getStoryObserver = new GetStoryObserver();

        // Perform login
        userServiceSpy.login("@njpratt", "fake", loginObserver);
        awaitCountDownLatch();

        // Verify login results
        assertTrue(loginObserver.success);
        User user = loginObserver.user;
        assertEquals("@njpratt", user.getAlias());
        AuthToken token = Cache.getInstance().getCurrUserAuthToken();
        assertNotNull(token);

        // Perform post status
        Status status = new Status("This is a test status for M4C, #1", user, LocalDateTime.now().toString(), null, null);
        statusServiceSpy.postStatus(token, status, postStatusObserver);
        awaitCountDownLatch();

        // Verify post status results
        assertTrue(postStatusObserver.success); // if this passes, then the "Successfully posted status!" toast pops up

        // Perform get story (only succeeds if the user has less than 11 statuses total in their story)
        statusServiceSpy.loadMore_Story(token, user, 10, null, getStoryObserver);
        awaitCountDownLatch();

        // Verify get story results
        List<Status> inStory = getStoryObserver.statuses;
        assertTrue(getStoryObserver.success);
        assertTrue(inStory.contains(status));

    }

    private class LoginObserver implements ParamSuccessObserver<User> {
        private User user;
        private String message;
        private Exception exception;
        private boolean success;

        @Override
        public void success(User user) {
            success = true;
            this.user = user;
            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.message = message;
            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.exception = exception;
            countDownLatch.countDown();
        }
    }

    private class PostStatusObserver implements SimpleSuccessObserver {
        private String message;
        private Exception exception;
        private boolean success;

        @Override
        public void success() {
            success = true;
            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.message = message;
            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.exception = exception;
            countDownLatch.countDown();
        }
    }

    private class GetStoryObserver implements DoubleParamSuccessObserver<List<Status>, Boolean> {
        private List<Status> statuses;
        private Boolean hasMorePages;
        private String message;
        private Exception exception;
        private boolean success;

        @Override
        public void success(List<Status> statuses, Boolean hasMorePages) {
            success = true;
            this.statuses = statuses;
            this.hasMorePages = hasMorePages;
            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.message = message;
            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.exception = exception;
            countDownLatch.countDown();
        }
    }
}
