package edu.byu.cs.tweeter.server.dao;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDAOUtil {

    private static final DynamoDAOUtil instance = new DynamoDAOUtil();
    public static DynamoDAOUtil getInstance() { return instance; }

    private DynamoDbEnhancedClient eClient;

    public DynamoDbEnhancedClient getClient() {
        if (eClient == null) {
            DynamoDbClient client = DynamoDbClient.builder().region(Region.US_WEST_2).build();
            eClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
        }
        return eClient;
    }

}
