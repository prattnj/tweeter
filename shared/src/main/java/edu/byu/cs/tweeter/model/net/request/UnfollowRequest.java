package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UnfollowRequest {

    public AuthToken authToken;
    public String followeeAlias;

    private UnfollowRequest() {}

    public UnfollowRequest(AuthToken authToken, String followeeAlias) {
        this.authToken = authToken;
        this.followeeAlias = followeeAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
