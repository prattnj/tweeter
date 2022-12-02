package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;

public class GetStoryTest {

    @Test
    public void getStoryTest() {
        AuthToken token = new AuthToken("cd026aef-1768-4e01-8f61-99d6c3921e16", "@kanyegrohl", "2022-11-23T00:46:42.298888");
        GetStoryRequest req = new GetStoryRequest(token, "@kanyegrohl", 10, null);
        GetStoryResponse resp = new StatusService(new DynamoFeedDAO(), new DynamoStoryDAO(), new DynamoAuthtokenDAO()).getStory(req);
        System.out.println("debug");
    }

}
