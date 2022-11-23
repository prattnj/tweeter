package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface StoryDAO {

    void insert(Status status);
    Pair<List<Status>, Boolean> getPage(String creator, Status lastStatus, int limit);
    void clear();
    Status find(String username);

}
