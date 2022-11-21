package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {

    void insert(User user);
    User find(String alias);
    boolean validate(String username, String password);

}
