package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    private final FeedDAO feed_dao;
    private final StoryDAO story_dao;
    private final AuthtokenDAO authtoken_dao;

    public StatusService(FeedDAO feed_dao, StoryDAO story_dao, AuthtokenDAO authtoken_dao) {
        this.feed_dao = feed_dao;
        this.story_dao = story_dao;
        this.authtoken_dao = authtoken_dao;
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Limit cannot be a negative number");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        Pair<List<Status>, Boolean> ret = feed_dao.getPage(request.getUserAlias(), request.getLastStatus(), request.getLimit());
        List<Status> fixed = fixDateTime(ret.getFirst());
        return new GetFeedResponse(fixed, ret.getSecond());
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (request.getLimit() < 0) {
            throw new RuntimeException("[Bad Request] Limit cannot be a negative number");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        Pair<List<Status>, Boolean> ret = story_dao.getPage(request.getUserAlias(), request.getLastStatus(), request.getLimit());
        List<Status> fixed = fixDateTime(ret.getFirst());
        return new GetStoryResponse(fixed, ret.getSecond());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status object");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        String queueURL = "https://sqs.us-west-2.amazonaws.com/287157383925/tweeter_posted";

        SendMessageRequest req = new SendMessageRequest()
                .withQueueUrl(queueURL)
                .withMessageBody(new Gson().toJson(request.getStatus()));

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(req);

        return new PostStatusResponse(true);


    }

    private List<Status> fixDateTime(List<Status> orig) {
        for (Status s : orig) {
            LocalDateTime dt = LocalDateTime.parse(s.getDatetime());
            s.setDatetime(dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        }
        return orig;
    }

}
