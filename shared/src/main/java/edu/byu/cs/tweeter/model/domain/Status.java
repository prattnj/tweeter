package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a status (or tweet) posted by a user.
 */
public class Status implements Serializable {
    /**
     * Text for the status.
     */
    private String post;
    /**
     * User who sent the status.
     */
    private User user;
    /**
     * String representation of the date/time at which the status was sent.
     */
    private String datetime;
    /**
     * URLs contained in the post text.
     */
    private List<String> urls;
    /**
     * User mentions contained in the post text.
     */
    private List<String> mentions;

    private String statusID;

    public Status() {
    }

    public Status(String post, User user, String datetime, List<String> urls, List<String> mentions) {
        this.post = post;
        this.user = user;
        this.datetime = datetime;
        this.urls = urls;
        this.mentions = mentions;
        this.statusID = user.getAlias() + datetime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getPost() {
        return post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(post, status.post) &&
                Objects.equals(user, status.user) &&
                /*Objects.equals(datetime, status.datetime) &&*/
                Objects.equals(mentions, status.mentions) &&
                Objects.equals(urls, status.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user, datetime, mentions, urls);
    }

    @Override
    public String toString() {
        return "Status{" +
                "post='" + post + '\'' +
                ", user=" + user +
                ", datetime=" + datetime +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }

}
