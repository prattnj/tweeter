package edu.byu.cs.tweeter.server.dao.bean;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class AuthtokenBean {

    private String token;
    private String username;
    private String datetime;

    public AuthtokenBean(String token, String datetime) {
        this.token = token;
        this.datetime = datetime;
    }

    public String getToken() {
        return token;
    }

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

    public String getDatetime() {
        return datetime;
    }
}
