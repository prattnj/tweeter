package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;

public class GetFeedTest {

    @Test
    public void testGetFeed() {
        AuthToken token = new AuthToken("cd026aef-1768-4e01-8f61-99d6c3921e16", "@kanyegrohl", "2022-11-23T00:46:42.298888");
        GetFeedRequest req = new GetFeedRequest(token, "@kanyegrohl", 50, null);
        GetFeedResponse resp = new StatusService(new DynamoFeedDAO(), new DynamoStoryDAO(), new DynamoFollowDAO(), new DynamoAuthtokenDAO()).getFeed(req);
        GetFeedRequest req2 = new GetFeedRequest(token, "@kanyegrohl", 50, resp.getStatuses().get(resp.getStatuses().size()-1));
        GetFeedResponse resp2 = new StatusService(new DynamoFeedDAO(), new DynamoStoryDAO(), new DynamoFollowDAO(), new DynamoAuthtokenDAO()).getFeed(req2);
        System.out.println("debug");
    }

}
