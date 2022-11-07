package edu.byu.cs.tweeter.model.net.response;

public class PostStatusResponse extends Response {
    PostStatusResponse(boolean success) {
        super(success);
    }

    PostStatusResponse(boolean success, String message) {
        super(success, message);
    }
}
