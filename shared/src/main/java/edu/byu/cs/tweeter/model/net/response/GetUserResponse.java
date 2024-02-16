package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class GetUserResponse extends Response {

    public GetUserResponse(boolean success, User user) {
        super(success);
        this.user = user;
    }

    public GetUserResponse(boolean success, String message) {
        super(success, message);
    }

    private User user;

    public User getUser() {
        return user;
    }
}
