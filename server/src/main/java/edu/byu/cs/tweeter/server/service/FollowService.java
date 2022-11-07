package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public GetFollowingResponse getFollowing(GetFollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        Pair<List<User>, Boolean> ret = getFakeData().getPageOfUsers(request.getLastFolloweeAlias(), request.getLimit(), request.getFollowerAlias());
        return new GetFollowingResponse(ret.getFirst(), ret.getSecond());
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
        return new GetFollowingCountResponse(20);
    }

    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a valid authtoken");
        }
        Pair<List<User>, Boolean> ret = getFakeData().getPageOfUsers(request.getLastFollowerAlias(), request.getLimit(), request.getFolloweeAlias());
        return new GetFollowersResponse(ret.getFirst(), ret.getSecond());
    }

    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
        return new GetFollowersCountResponse(20);
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
        return new FollowResponse(true);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
        return new UnfollowResponse(true);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        boolean isFollower = new Random().nextInt() > 0;
        return new IsFollowerResponse(true, isFollower);
    }

    // To be removed after M3
    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }
}
