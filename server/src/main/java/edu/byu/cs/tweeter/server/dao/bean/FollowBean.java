package edu.byu.cs.tweeter.server.dao.bean;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
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
    public String getFollower_handle() {
        return follower_handle;
    }

    @DynamoDbSortKey
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
}
