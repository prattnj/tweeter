package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.server.lambda.GetUserHandler;

public class GetUserTest {

    @Test
    public void testGetUser() {
        AuthToken token = new AuthToken("32224ff3-50de-45e0-a2c8-3471591d16be", "@test", "2022-11-21T15:07:05.8");
        GetUserRequest req = new GetUserRequest(token, "@test");
        GetUserResponse resp = new GetUserHandler().handleRequest(req, null);
        System.out.println("debug");
    }

}
