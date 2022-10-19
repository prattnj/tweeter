package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenterTest {

    private MainPresenter.View mockView;
    private StatusService mockService;
    private MainPresenter spyPresenter;

    private final String SUCCESS_MESSAGE = "Successfully Posted!";
    private final String FAIL_MESSAGE = "Failed to post status: ";
    private final String EXCEPTION_MESSAGE = "Failed to post status due to exception: ";

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockService = Mockito.mock(StatusService.class);
        spyPresenter = Mockito.spy(new MainPresenter(mockView));

        Mockito.when(spyPresenter.getStatusService()).thenReturn(mockService);
    }

    @Test
    public void testSuccess() throws ParseException {
        Answer<Void> answer = invocation -> {
            MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
            observer.success();
            return null;
        };

        Mockito.doAnswer(answer).when(mockService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        spyPresenter.postStatus(Mockito.anyString());

        Mockito.verify(mockView).cancelPostToast();
        verifyDisplayMessage(SUCCESS_MESSAGE);
    }

    @Test
    public void testFail() throws ParseException {
        Answer<Void> answer = invocation -> {
            MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
            observer.handleFailure(""); // This will be the message returned from the bundle
            return null;
        };

        Mockito.doAnswer(answer).when(mockService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        spyPresenter.postStatus(Mockito.anyString());

        verifyDisplayMessage(FAIL_MESSAGE);
    }

    @Test
    public void testException() throws ParseException {
        Answer<Void> answer = invocation -> {
            MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
            observer.handleException(new Exception(""));
            return null;
        };

        Mockito.doAnswer(answer).when(mockService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        spyPresenter.postStatus(Mockito.anyString());

        verifyDisplayMessage(EXCEPTION_MESSAGE);
    }

    @Test
    public void testParams() throws ParseException {
        String dummyPost = "I love https://byu.edu and http://lds.org, says @mickeymouse and his friend @donaldduck";
        MainPresenter presenter = new MainPresenter();
        User dummyUser = new User();
        Status dummyStatus = new Status(dummyPost, dummyUser, presenter.getFormattedDateTime(), presenter.parseURLs(dummyPost), presenter.parseMentions(dummyPost));

        List<String> urls = new ArrayList<>();
        urls.add("https://byu.edu");
        urls.add("http://lds.org");
        List<String> mentions = new ArrayList<>();
        mentions.add("@mickeymouse");
        mentions.add("@donaldduck");

        Assertions.assertEquals(dummyPost, dummyStatus.post);
        Assertions.assertEquals(urls, dummyStatus.urls);
        Assertions.assertEquals(mentions, dummyStatus.mentions);
    }

    private void verifyDisplayMessage(String s) {
        Mockito.verify(mockView).displayMessage(s);
    }

}
