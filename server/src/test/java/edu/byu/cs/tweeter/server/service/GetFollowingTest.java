package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class GetFollowingTest {

    @Test
    public void testGetFollowing() {
        AuthToken token = new AuthToken("cd026aef-1768-4e01-8f61-99d6c3921e16", "@kanyegrohl", "2022-11-23T00:46:42.298888");
        GetFollowingRequest req = new GetFollowingRequest(token, "@kanyegrohl", 20, "@postscott");
        GetFollowingResponse resp = new FollowService(new DynamoFollowDAO(), new DynamoAuthtokenDAO(), new DynamoUserDAO()).getFollowing(req);
        System.out.println("debug");
    }

}
