package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.FeedBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoFeedDAO implements FeedDAO {

    private final DynamoDbTable<FeedBean> table;

    public DynamoFeedDAO() {
        String TABLE_NAME = "tweeter_feeds";
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

        // WARNING: PERFORMS SCAN (EXPENSIVE)
        for (FeedBean bean : table.scan().items()) {
            table.deleteItem(bean);
        }

    }
}
