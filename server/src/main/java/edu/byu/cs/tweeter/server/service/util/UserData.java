package edu.byu.cs.tweeter.server.service.util;

import java.util.HashSet;

import edu.byu.cs.tweeter.model.domain.User;

public class UserData {

    private final HashSet<User> users;

    public UserData(HashSet<User> users) {
        this.users = users;
    }

    public HashSet<User> getUsers() {
        return users;
    }
}
