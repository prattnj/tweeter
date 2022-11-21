package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    /**
     * Value of the auth token.
     */
    public String token;
    public String username;
    /**
     * String representation of date/time at which the auth token was created.
     */
    public LocalDateTime datetime;

    public AuthToken() {
    }

    public AuthToken(String token) {
        this.token = token;
    }

    public AuthToken(String token, String username, LocalDateTime datetime) {
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

    public LocalDateTime getDatetime() {
        return datetime;
    }
}