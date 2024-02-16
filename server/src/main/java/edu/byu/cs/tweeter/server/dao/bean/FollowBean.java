package edu.byu.cs.tweeter.server.dao.bean;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FollowBean {

    // NOTE: Items with 1 correspond to follower, items with 2 to followee
    private String follower_handle;
    private String followee_handle;
    private String firstName1;
    private String lastName1;
    private String password1;
    private String imageUrl1;
    private String firstName2;
    private String lastName2;
    private String password2;
    private String imageUrl2;

    public FollowBean() {}

    public FollowBean(String follower_handle, String followee_handle, String firstName1, String lastName1, String password1,
                      String imageUrl1, String firstName2, String lastName2, String password2, String imageUrl2) {
        this.follower_handle = follower_handle;
        this.followee_handle = followee_handle;
        this.firstName1 = firstName1;
        this.lastName1 = lastName1;
        this.password1 = password1;
        this.imageUrl1 = imageUrl1;
        this.firstName2 = firstName2;
        this.lastName2 = lastName2;
        this.password2 = password2;
        this.imageUrl2 = imageUrl2;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = "tweeter_follows_index")
    public String getFollower_handle() {
        return follower_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "tweeter_follows_index")
    public String getFollowee_handle() {
        return followee_handle;
    }

    public String getFirstName1() {
        return firstName1;
    }

    public String getLastName1() {
        return lastName1;
    }

    public String getPassword1() {
        return password1;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public String getFirstName2() {
        return firstName2;
    }

    public String getLastName2() {
        return lastName2;
    }

    public String getPassword2() {
        return password2;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    // SETTERS (necessary for DynamoDB)

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    public void setFirstName1(String firstName1) {
        this.firstName1 = firstName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public void setFirstName2(String firstName2) {
        this.firstName2 = firstName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }
}
