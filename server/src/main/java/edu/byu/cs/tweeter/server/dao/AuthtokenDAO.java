package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthtokenDAO {

    void insert(AuthToken token);
    boolean validate(AuthToken token);
    void remove(String token);
    String getUsername(String token);

}
