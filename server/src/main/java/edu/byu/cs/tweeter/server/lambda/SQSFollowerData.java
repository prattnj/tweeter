package edu.byu.cs.tweeter.server.lambda;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class SQSFollowerData {

    private Status status;
    private List<String> followers;

    public SQSFollowerData(Status status, List<String> followers) {
        this.status = status;
        this.followers = followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}
