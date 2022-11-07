package edu.byu.cs.tweeter.model.net.response;

public class GetUserResponse extends Response {
    GetUserResponse(boolean success) {
        super(success);
    }

    GetUserResponse(boolean success, String message) {
        super(success, message);
    }
}
