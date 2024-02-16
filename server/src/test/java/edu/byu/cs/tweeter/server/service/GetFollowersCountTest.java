package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class GetFollowersCountTest {

    @Test
    public void testFollowerCount() {
        AuthToken token = new AuthToken("3b698099-d141-40bd-8972-3cbcf2a129ca", "@kanyegrohl", "2022-11-23T00:08:28.379905");
        GetFollowersCountRequest req = new GetFollowersCountRequest(token, "@kanyegrohl");
        GetFollowersCountResponse resp = new FollowService(new DynamoFollowDAO(), new DynamoAuthtokenDAO(), new DynamoUserDAO()).getFollowersCount(req);
        System.out.println("debug");
    }

}
