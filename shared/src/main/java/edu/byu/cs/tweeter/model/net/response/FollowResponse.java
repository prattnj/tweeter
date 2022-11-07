package edu.byu.cs.tweeter.model.net.response;

public class FollowResponse extends Response {
    FollowResponse(boolean success) {
        super(success);
    }

    FollowResponse(boolean success, String message) {
        super(success, message);
    }
}
