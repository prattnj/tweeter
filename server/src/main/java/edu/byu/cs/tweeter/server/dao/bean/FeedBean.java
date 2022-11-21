package edu.byu.cs.tweeter.server.dao.bean;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedBean {

    private String viewerAlias;
    private String creatorAlias;
    private String post;
    private String datetime;
    private List<String> urls;
    private List<String> mentions;
    private String statusID;

    public FeedBean(String viewerAlias, String creatorAlias, String post, String datetime, List<String> urls, List<String> mentions, String statusID) {
        this.viewerAlias = viewerAlias;
        this.creatorAlias = creatorAlias;
        this.post = post;
        this.datetime = datetime;
        this.urls = urls;
        this.mentions = mentions;
        this.statusID = statusID;
    }

    @DynamoDbPartitionKey
    public String getViewerAlias() {
        return viewerAlias;
    }

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
