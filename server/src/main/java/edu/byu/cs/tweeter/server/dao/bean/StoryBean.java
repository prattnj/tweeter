package edu.byu.cs.tweeter.server.dao.bean;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class StoryBean {

    private String sender_alias;
    private String post;
    private String datetime;
    private List<String> urls;
    private List<String> mentions;
    private String statusID;
    private String firstName;
    private String lastName;
    private String password;
    private String imageUrl;

    public StoryBean(String creatorAlias, String post, String datetime, List<String> urls, List<String> mentions, String statusID,
                     String firstName, String lastName, String password, String imageUrl) {
        this.sender_alias = creatorAlias;
        this.post = post;
        this.datetime = datetime;
        this.urls = urls;
        this.mentions = mentions;
        this.statusID = statusID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    @DynamoDbPartitionKey
    public String getSenderAlias() {
        return sender_alias;
    }

    public String getPost() {
        return post;
    }

    @DynamoDbSortKey
    public String getDatetime() {
        return datetime;
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
}
