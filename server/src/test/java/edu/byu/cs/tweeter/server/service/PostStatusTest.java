package edu.byu.cs.tweeter.server.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class PostStatusTest {

    @Test
    public void testPostStatus() {

        UserDAO udao = new DynamoUserDAO();
        AuthtokenDAO adao = new DynamoAuthtokenDAO();
        FeedDAO fdao1 = new DynamoFeedDAO();
        FollowDAO fdao2 = new DynamoFollowDAO();
        StoryDAO sdao = new DynamoStoryDAO();

        User user = udao.find("@njpratt");
        if (user == null) return;

        // Login to get an authtoken
        LoginResponse res = new UserService(adao, udao).login(new LoginRequest("@njpratt", "fake"));

        List<String> mentions = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        mentions.add("@test");
        urls.add("https://byu.edu");
        Status status = new Status("This is a test status. @test told me to post this at https://byu.edu.",
                user, LocalDateTime.now().toString(), urls, mentions);
        PostStatusRequest req = new PostStatusRequest(res.getAuthToken(), status);

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(req));

        PostStatusResponse resp = new StatusService(fdao1, sdao, adao).postStatus(req);

        assertTrue(resp.isSuccess());

    }

}
