package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;

public class GetFeedTest {

    @Test
    public void testGetFeed() {
        AuthToken token = new AuthToken("576a327f-e91a-42fc-aebe-36e73f2a1a8b", "@njpratt", "2022-11-23T05:38:10.714262");
        GetFeedRequest req = new GetFeedRequest(token, "@njpratt", 50, null);
        GetFeedResponse resp = new StatusService(new DynamoFeedDAO(), new DynamoStoryDAO(), new DynamoAuthtokenDAO()).getFeed(req);
        GetFeedRequest req2 = new GetFeedRequest(token, "@njpratt", 50, resp.getStatuses().get(resp.getStatuses().size()-1));
        GetFeedResponse resp2 = new StatusService(new DynamoFeedDAO(), new DynamoStoryDAO(), new DynamoAuthtokenDAO()).getFeed(req2);
        System.out.println("debug");
    }

}
