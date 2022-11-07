package edu.byu.cs.tweeter.client.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.observer_interface.DoubleParamSuccessObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {

    private User dummyUser;
    private AuthToken dummyToken;
    private StatusService serviceSpy;
    private StatusServiceObserver observer;

    private CountDownLatch countDownLatch;

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @BeforeEach
    public void setup() {
        dummyUser = FakeData.getInstance().getFirstUser();
        dummyToken = FakeData.getInstance().getAuthToken();
        serviceSpy = Mockito.spy(new StatusService());
        observer = new StatusServiceObserver();
        resetCountDownLatch();
    }

    private class StatusServiceObserver implements DoubleParamSuccessObserver<List<Status>, Boolean> {

        private List<Status> items;
        private boolean hasMorePages;
        private String message;
        private Exception exception;
        private boolean success;

        @Override
        public void success(List<Status> items, Boolean hasMore) {
            success = true;
            if (items == null) return;
            this.items = items;
            hasMorePages = hasMore;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            success = false;
            this.message = message;
        }

        @Override
        public void handleException(Exception exception) {
            success = false;
            this.exception = exception;
        }

        public List<Status> getItems() {
            return items;
        }

        public boolean hasMorePages() {
            return hasMorePages;
        }

        public String getMessage() {
            return message;
        }

        public Exception getException() {
            return exception;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    @Test
    public void testStatusService() throws InterruptedException {

        int pageSize = 1;

        serviceSpy.loadMore_Story(dummyToken, dummyUser, pageSize, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = FakeData.getInstance().getPageOfStatus(null, pageSize).getFirst();
        assertNotNull(observer);
        assertTrue(observer.isSuccess());
        assertTrue(observer.hasMorePages());
        assertNotNull(observer.getItems());
        assertEquals(expectedStatuses, observer.getItems());
        assertNull(observer.getMessage());
        assertNull(observer.getException());

    }

}
