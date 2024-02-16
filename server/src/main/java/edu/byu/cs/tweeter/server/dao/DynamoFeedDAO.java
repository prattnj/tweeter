package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.FeedBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class DynamoFeedDAO implements FeedDAO {

    private final DynamoDbTable<FeedBean> table;
    private final String TABLE_NAME = "tweeter_feeds";

    public DynamoFeedDAO() {
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(FeedBean.class));
    }

    @Override
    public void insert(String receiver, Status status) {
        FeedBean bean = new FeedBean(receiver, status.getUser().getAlias(), status.getPost(),
                status.getDatetime(), status.getUrls(), status.getMentions(), status.getStatusID(),
                status.getUser().getFirstName(), status.getUser().getLastName(), status.getUser().getPassword(),
                status.getUser().getImageUrl());
        table.putItem(bean);
    }

    @Override
    public void insertGroup(List<String> receivers, Status status) {

        List<FeedBean> allBeans = new ArrayList<>();
        for (String receiver : receivers) allBeans.add(new FeedBean(receiver, status.getUser().getAlias(), status.getPost(),
                status.getDatetime(), status.getUrls(), status.getMentions(), status.getStatusID(), status.getUser().getFirstName(),
                status.getUser().getLastName(), status.getUser().getPassword(), status.getUser().getImageUrl()));
        List<List<FeedBean>> groups = new ArrayList<>();

        // Organize into groups, max size 25
        int groupSize = 25;
        int numGroups = allBeans.size() / groupSize;
        if (allBeans.size() % groupSize != 0) numGroups++;
        for (int i = 0; i < numGroups; i++) {
            int startingIndex = i * groupSize;
            List<FeedBean> tempList = new ArrayList<>();
            for (int j = startingIndex; j < groupSize + startingIndex; j++) {
                if (j > allBeans.size() - 1) break;
                tempList.add(allBeans.get(j));
            }
            groups.add(tempList);
        }

        System.out.println("debug");

        int progress = 1;
        for (List<FeedBean> beans : groups) {
            System.out.println("Writing feed group " + progress + "/" + groups.size());
            insertChunkOfBeans(beans);
            progress++;
        }

    }

    private void insertChunkOfBeans(List<FeedBean> beans) {

        WriteBatch.Builder<FeedBean> test = WriteBatch.builder(FeedBean.class)
                .mappedTableResource(table);
        for (FeedBean bean : beans) {
            test.addPutItem(bean);
        }
        BatchWriteItemEnhancedRequest req = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(test.build())
                .build();
        BatchWriteResult result =  DynamoDAOUtil.getInstance().getClient().batchWriteItem(req);
        if (result.unprocessedPutItemsForTable(table).size() > 0) {
            insertChunkOfBeans(result.unprocessedPutItemsForTable(table));
        }

    }

    @Override
    public Pair<List<Status>, Boolean> getPage(String receiver, Status lastStatus, int limit) {
        Key key = Key.builder().partitionValue(receiver).build();

        QueryEnhancedRequest.Builder rb = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false);

        if(lastStatus != null) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("receiver_alias", AttributeValue.builder().s(receiver).build());
            startKey.put("timestamp", AttributeValue.builder().s(lastStatus.getDatetime()).build());

            rb.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = rb.build();
        List<FeedBean> beans = table.query(request)
                .items()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
        List<Status> actual = new ArrayList<>();
        for (FeedBean b : beans) {
            Status s = new Status(b.getPost(), new User(b.getFirstName(), b.getLastName(), b.getSender_alias(),
                    b.getPassword(), b.getImageUrl()), b.getTimestamp(), b.getUrls(), b.getMentions());
            actual.add(s);
        }
        boolean hasMorePages = (beans.size() == limit);
        return new Pair<>(actual, hasMorePages);
    }

    @Override
    public void clear() {

        // Most efficient and cheapest way is to delete the table and recreate it
        DynamoDbWaiter waiter = DynamoDbWaiter.create();
        table.deleteTable();
        waiter.waitUntilTableNotExists(builder -> builder.tableName(TABLE_NAME));

        table.createTable(builder -> builder
                .provisionedThroughput(b -> b
                        .readCapacityUnits(100L)
                        .writeCapacityUnits(100L)
                        .build())
        );

        waiter.waitUntilTableExists(builder -> builder.tableName(TABLE_NAME));

    }

    @Override
    public void scanClear() {

        for (FeedBean bean : table.scan().items()) {
            table.deleteItem(bean);
        }

    }
}
