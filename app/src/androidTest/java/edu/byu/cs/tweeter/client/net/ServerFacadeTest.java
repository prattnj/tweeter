package edu.byu.cs.tweeter.client.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {

    private ServerFacade facade;
    private User dummyUser;
    private AuthToken dummyToken;

    @BeforeEach
    public void setup() {
        facade = getFacade();
        dummyUser = FakeData.getInstance().getFirstUser();
        dummyToken = FakeData.getInstance().getAuthToken();
    }

    @Test
    public void RegisterTest() throws IOException, TweeterRemoteException {
        RegisterRequest request = new RegisterRequest(dummyUser.getFirstName(), dummyUser.getLastName(), dummyUser.getAlias(), "fakepword", "fakeimage");
        RegisterResponse response = facade.register(request, "/register");

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(dummyUser.getAlias(), response.getUser().getAlias());
        assertEquals(dummyUser.getFirstName(), response.getUser().getFirstName());
        assertEquals(dummyUser.getLastName(), response.getUser().getLastName());
        assertNotNull(response.getAuthToken());
    }

    @Test
    public void GetFollowersTest() throws IOException, TweeterRemoteException {
        GetFollowersRequest request = new GetFollowersRequest(dummyToken, dummyUser.getAlias(), 10, null);
        GetFollowersResponse response = facade.getFollowers(request, "/getfollowers");

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(FakeData.getInstance().getPageOfUsers(null, 10, dummyUser.getAlias()).getFirst(), response.getFollowers());
    }

    @Test
    public void GetFollowersCountTest() throws IOException, TweeterRemoteException {
        GetFollowersCountRequest request = new GetFollowersCountRequest(dummyToken, dummyUser.getAlias());
        GetFollowersCountResponse response = facade.getFollowersCount(request, "/getfollowerscount");

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(20, response.getCount());
    }

    private ServerFacade getFacade() {
        if (facade == null) return new ServerFacade();
        else return facade;
    }

}
