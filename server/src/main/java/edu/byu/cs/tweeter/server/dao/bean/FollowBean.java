package edu.byu.cs.tweeter.server.dao.bean;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FollowBean {

    private String follower_handle;
    private String followee_handle;

    public FollowBean(String follower_handle, String followee_handle) {
        this.follower_handle = follower_handle;
        this.followee_handle = followee_handle;
    }

    @DynamoDbPartitionKey
    public String getFollower_handle() {
        return follower_handle;
    }

    @DynamoDbSortKey
    public String getFollowee_handle() {
        return followee_handle;
    }
}
