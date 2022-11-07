package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {
    IsFollowerResponse(boolean success) {
        super(success);
    }

    IsFollowerResponse(boolean success, String message) {
        super(success, message);
    }
}
