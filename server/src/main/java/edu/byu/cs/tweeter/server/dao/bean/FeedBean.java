package edu.byu.cs.tweeter.server.dao.bean;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedBean {

    private String receiver_alias;
    private String sender_alias;
    private String post;
    private String timestamp;
    private List<String> urls;
    private List<String> mentions;
    private String statusID;
    private String firstName;
    private String lastName;
    private String password;
    private String imageUrl;

    public FeedBean() {}

    public FeedBean(String viewerAlias, String creatorAlias, String post, String datetime, List<String> urls, List<String> mentions,
                    String statusID, String firstName, String lastName, String password, String imageUrl) {
        this.receiver_alias = viewerAlias;
        this.sender_alias = creatorAlias;
        this.post = post;
        this.timestamp = datetime;
        this.urls = urls;
        this.mentions = mentions;
        this.statusID = statusID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    @DynamoDbPartitionKey
    public String getReceiver_alias() {
        return receiver_alias;
    }

    public String getSender_alias() {
        return sender_alias;
    }

    public String getPost() {
        return post;
    }

    @DynamoDbSortKey
    public String getTimestamp() {
        return timestamp;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // SETTERS (necessary for DynamoDB)

    public void setReceiver_alias(String receiver_alias) {
        this.receiver_alias = receiver_alias;
    }

    public void setSender_alias(String sender_alias) {
        this.sender_alias = sender_alias;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
