package edu.byu.cs.tweeter.client.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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

        RegisterRequest request_good = new RegisterRequest(dummyUser.getFirstName(), dummyUser.getLastName(), dummyUser.getAlias(), "fakepword", "fakeimage");
        RegisterRequest request_bad = new RegisterRequest(dummyUser.getFirstName(), dummyUser.getLastName(), dummyUser.getAlias(), null, "fakeimage");
        RegisterResponse response = facade.register(request_good, "/register");
        //RegisterResponse response2 = facade.register(request2, "/register");

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(dummyUser.getAlias(), response.getUser().getAlias());
        assertEquals(dummyUser.getFirstName(), response.getUser().getFirstName());
        assertEquals(dummyUser.getLastName(), response.getUser().getLastName());
        assertNotNull(response.getAuthToken());

        assertThrows(TweeterRequestException.class, () -> facade.register(request_bad, "/register"));
    }

    @Test
    public void GetFollowersTest() throws IOException, TweeterRemoteException {
        GetFollowersRequest request_good = new GetFollowersRequest(dummyToken, dummyUser.getAlias(), 10, null);
        GetFollowersResponse response = facade.getFollowers(request_good, "/getfollowers");
        GetFollowersRequest request_bad = new GetFollowersRequest(null, dummyUser.getAlias(), 10, null);
        GetFollowersRequest request_bad2 = new GetFollowersRequest(dummyToken, dummyUser.getAlias(), -10, null);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(FakeData.getInstance().getPageOfUsers(null, 10, dummyUser.getAlias()).getFirst(), response.getFollowers());

        assertThrows(TweeterRequestException.class, () -> facade.getFollowers(request_bad, "/getfollowers"));
        assertThrows(TweeterRequestException.class, () -> facade.getFollowers(request_bad2, "/getfollowers"));
    }

    @Test
    public void GetFollowersCountTest() throws IOException, TweeterRemoteException {
        GetFollowersCountRequest request_good = new GetFollowersCountRequest(dummyToken, dummyUser.getAlias());
        GetFollowersCountResponse response = facade.getFollowersCount(request_good, "/getfollowerscount");
        GetFollowersCountRequest request_bad = new GetFollowersCountRequest(dummyToken, null);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(20, response.getCount());

        assertThrows(TweeterRequestException.class, () -> facade.getFollowersCount(request_bad, "/getfollowerscount"));
    }

    private ServerFacade getFacade() {
        if (facade == null) return new ServerFacade();
        else return facade;
    }

}
