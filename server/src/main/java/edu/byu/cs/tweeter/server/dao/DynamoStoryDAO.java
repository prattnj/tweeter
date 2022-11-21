package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.StoryBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoStoryDAO implements StoryDAO {

    private final DynamoDbTable<StoryBean> table;

    public DynamoStoryDAO() {
        String TABLE_NAME = "tweeter_stories";
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(StoryBean.class));
    }

    @Override
    public void insert(Status status) {
        StoryBean bean = new StoryBean(status.getUser().getAlias(), status.getPost(),
                status.getDate().toString(), status.getUrls(), status.getMentions(), status.getStatusID(),
                status.getUser().getFirstName(), status.getUser().getLastName(), status.getUser().getPassword(),
                status.getUser().getImageUrl());
        table.putItem(bean);
    }

    @Override
    public Pair<List<Status>, Boolean> getPage(String creator, Status lastStatus, int limit) {
        Key key = Key.builder().partitionValue(creator).build();
        QueryEnhancedRequest.Builder rb = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(false);
        if(lastStatus != null) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("sender_alias", AttributeValue.builder().s(creator).build());
            startKey.put("datetime", AttributeValue.builder().s(lastStatus.getDate().toString()).build());

            rb.exclusiveStartKey(startKey);
        }
        QueryEnhancedRequest request = rb.build();
        PageIterable<StoryBean> results = table.query(request);
        List<StoryBean> beans = new ArrayList<>();
        List<Status> actual = new ArrayList<>();
        results.stream().limit(1).forEach(visitsPage -> beans.addAll(visitsPage.items()));
        for (StoryBean b : beans) {
            Status s = new Status(b.getPost(), new User(b.getFirstName(), b.getLastName(), b.getSenderAlias(),
                    b.getPassword(), b.getImageUrl()), LocalDateTime.parse(b.getDatetime()), b.getUrls(), b.getMentions());
            actual.add(s);
        }
        boolean hasMorePages = (beans.size() == limit);
        return new Pair<>(actual, hasMorePages);
    }
}
