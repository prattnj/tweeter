package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDAO {

    void insert(User follower, User followee);
    void insertGroup(List<Follow> follows);
    List<User> getAllFollowers(String username);
    Pair<List<User>, Boolean> getPageFollowing(String username, String lastUser, int limit);
    Pair<List<User>, Boolean> getPageFollowers(String username, String lastUser, int limit);
    int getFollowingCount(String username);
    int getFollowersCount(String username);
    boolean isFollower(String follower, String followee);
    void remove(String follower, String followee);
    void clear();
    void scanClear();

}
