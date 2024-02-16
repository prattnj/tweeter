package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {

    void insert(User user);
    void insertGroup(List<User> users);
    User find(String alias);
    boolean validate(String username, String password);
    void clear();
    void scanClear();

}
