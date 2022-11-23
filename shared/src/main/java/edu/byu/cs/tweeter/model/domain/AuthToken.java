package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    /**
     * Value of the auth token.
     */
    private String token;
    private String username;
    private String datetime;

    public AuthToken() {}

    public AuthToken(String token) {
        this.token = token;
    }

    public AuthToken(String token, String username, String datetime) {
        this.token = token;
        this.username = username;
        this.datetime = datetime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "Authtoken: [" + token + ", " + username + ", " + datetime + "]";
    }
}