package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface FeedDAO {

    void insert(String receiver, Status status);
    Pair<List<Status>, Boolean> getPage(String receiver, Status lastStatus, int limit);

}
