package edu.byu.cs.tweeter.server.dao.bean;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class AuthtokenBean {

    private String token;
    private String username;
    private String datetime;

    public AuthtokenBean() {}

    public AuthtokenBean(String token, String username, String datetime) {
        this.token = token;
        this.username = username;
        this.datetime = datetime;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = "tweeter_authtokens_index")
    public String getToken() {
        return token;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = "tweeter_authtokens_index")
    public String getUsername() {
        return username;
    }

    public String getDatetime() {
        return datetime;
    }

    // SETTERS (necessary for DynamoDB)

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
