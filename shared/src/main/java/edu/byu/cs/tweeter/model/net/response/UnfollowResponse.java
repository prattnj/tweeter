package edu.byu.cs.tweeter.model.net.response;

public class UnfollowResponse extends Response {
    UnfollowResponse(boolean success) {
        super(success);
    }

    UnfollowResponse(boolean success, String message) {
        super(success, message);
    }
}
