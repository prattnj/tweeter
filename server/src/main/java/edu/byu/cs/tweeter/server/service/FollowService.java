package edu.byu.cs.tweeter.server.service;

import java.util.List;

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
import edu.byu.cs.tweeter.server.dao.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    private final FollowDAO follow_dao;
    private final AuthtokenDAO authtoken_dao;

    public FollowService(FollowDAO follow_dao, AuthtokenDAO authtoken_dao) {
        this.follow_dao = follow_dao;
        this.authtoken_dao = authtoken_dao;
    }

    public GetFollowingResponse getFollowing(GetFollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a valid authtoken");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        Pair<List<User>, Boolean> ret = follow_dao.getPageFollowing(request.getFollowerAlias(), request.getLastFolloweeAlias(), request.getLimit());
        return new GetFollowingResponse(ret.getFirst(), ret.getSecond());
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        return new GetFollowingCountResponse(follow_dao.getFollowingCount(request.getAlias()));
    }

    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a valid authtoken");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        Pair<List<User>, Boolean> ret = follow_dao.getPageFollowers(request.getFolloweeAlias(), request.getLastFollowerAlias(), request.getLimit());
        return new GetFollowersResponse(ret.getFirst(), ret.getSecond());
    }

    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        return new GetFollowersCountResponse(follow_dao.getFollowersCount(request.getAlias()));
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        String follower = authtoken_dao.getUsername(request.getAuthToken().token);
        follow_dao.insert(follower, request.getFolloweeAlias());
        return new FollowResponse(true);
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        String follower = authtoken_dao.getUsername(request.getAuthToken().token);
        follow_dao.remove(follower, request.getFolloweeAlias());
        return new UnfollowResponse(true);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        return new IsFollowerResponse(true, follow_dao.isFollower(request.getFollowerAlias(), request.getFolloweeAlias()));
    }
}
