package edu.byu.cs.tweeter.server.lambda;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class SQSFollowerData {

    private Status status;
    private List<User> followers;

    public SQSFollowerData(Status status, List<User> followers) {
        this.status = status;
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }
}
