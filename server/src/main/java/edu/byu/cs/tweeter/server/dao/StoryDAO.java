package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface StoryDAO {

    void insert(Status status);
    void insertGroup(List<Status> statuses);
    Pair<List<Status>, Boolean> getPage(String creator, Status lastStatus, int limit);
    Status find(String username);
    void clear();
    void scanClear();

}
