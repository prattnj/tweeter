package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class LogoutTest {

    @Test
    public void testLogout() {
        LogoutRequest req = new LogoutRequest(new AuthToken("58223310-b3ad-4e66-b7c9-aa5d7b4c84c1", "@test", "kinda_null"));
        LogoutResponse resp = new UserService(new DynamoAuthtokenDAO(), new DynamoUserDAO()).logout(req);
        System.out.println("debug");
    }

}
