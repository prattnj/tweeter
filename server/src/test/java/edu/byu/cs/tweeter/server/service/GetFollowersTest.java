package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class GetFollowersTest {

    @Test
    public void testGetFollowers() {
        AuthToken token = new AuthToken("cd026aef-1768-4e01-8f61-99d6c3921e16", "@kanyegrohl", "2022-11-23T00:46:42.298888");
        GetFollowersRequest req = new GetFollowersRequest(token, "@kanyegrohl", 10, "@steveuchis");
        GetFollowersResponse resp = new FollowService(new DynamoFollowDAO(), new DynamoAuthtokenDAO(), new DynamoUserDAO()).getFollowers(req);
        System.out.println("debug");
    }

}
