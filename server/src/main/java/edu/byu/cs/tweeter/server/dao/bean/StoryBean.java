package edu.byu.cs.tweeter.server.dao.bean;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class StoryBean {

    private String creatorAlias;
    private String post;
    private String datetime;
    private List<String> urls;
    private List<String> mentions;
    private String statusID;

    public StoryBean(String creatorAlias, String post, String datetime, List<String> urls, List<String> mentions, String statusID) {
        this.creatorAlias = creatorAlias;
        this.post = post;
        this.datetime = datetime;
        this.urls = urls;
        this.mentions = mentions;
        this.statusID = statusID;
    }

    @DynamoDbPartitionKey
    public String getCreatorAlias() {
        return creatorAlias;
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

}
