package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    // todo: return fake data for everything function in this service

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
        Pair<List<Status>, Boolean> ret = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new GetFeedResponse(ret.getFirst(), ret.getSecond());
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
        Pair<List<Status>, Boolean> ret = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new GetStoryResponse(ret.getFirst(), ret.getSecond());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        return null; // todo
    }

    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

}
