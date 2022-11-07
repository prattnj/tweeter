package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {

    public IsFollowerResponse(boolean success, boolean isFollower) {
        super(success);
        this.isFollower = isFollower;
    }

    public IsFollowerResponse(boolean success, String message) {
        super(success, message);
    }

    private boolean isFollower;

    public boolean getIsFollower() {
        return isFollower;
    }


}
