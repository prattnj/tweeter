package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.FollowBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoFollowDAO implements FollowDAO {

    private final DynamoDbTable<FollowBean> table;
    private final DynamoDbIndex<FollowBean> index;

    public DynamoFollowDAO() {
        String TABLE_NAME = "tweeter_follows";
        String INDEX_NAME = "tweeter_follows_index";
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(FollowBean.class));
        this.index = table.index(INDEX_NAME);
    }

    @Override
    public void insert(User follower, User followee) {
        FollowBean bean = new FollowBean(follower.getAlias(), followee.getAlias(), follower.getFirstName(), follower.getLastName(),
                follower.getPassword(), follower.getImageUrl(), followee.getFirstName(), followee.getLastName(), followee.getPassword(),
                followee.getImageUrl());
        table.putItem(bean);
    }

    @Override
    public List<User> getAllFollowers(String username) {

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(username).build());

        PageIterable<FollowBean> pages = PageIterable.create(index.query(queryConditional));
        List<FollowBean> beans = new ArrayList<>();

        pages.stream().limit(1).forEach(page -> beans.addAll(page.items()));

        List<User> users = new ArrayList<>();
        for (FollowBean b : beans) {
            User u = new User(b.getFirstName1(), b.getLastName1(), b.getFollower_handle(), b.getPassword1(), b.getImageUrl1());
            users.add(u);
        }
        return users;
    }

    @Override
    public Pair<List<User>, Boolean> getPageFollowing(String username, String lastUser, int limit) {
        Key key = Key.builder()
                .partitionValue(username)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        if(lastUser != null && lastUser.length() > 0) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("follower_handle", AttributeValue.builder().s(username).build());
            startKey.put("followee_handle", AttributeValue.builder().s(lastUser).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<FollowBean> results =  table.query(request)
                .items()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        for (FollowBean b : results) {
            User u = new User(b.getFirstName2(), b.getLastName2(), b.getFollowee_handle(), b.getPassword2(), b.getImageUrl2());
            users.add(u);
        }
        boolean hasMorePages = (results.size() == limit);
        return new Pair<>(users, hasMorePages);
    }

    @Override
    public Pair<List<User>, Boolean> getPageFollowers(String username, String lastUser, int limit) {
        Key key = Key.builder()
                .partitionValue(username)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(true);

        if(lastUser != null && lastUser.length() > 0) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("followee_handle", AttributeValue.builder().s(username).build());
            startKey.put("follower_handle", AttributeValue.builder().s(lastUser).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<FollowBean> beans = new ArrayList<>();

        PageIterable<FollowBean> pages = PageIterable.create(index.query(request));
        pages.stream()
                .limit(1)
                .forEach(visitsPage -> beans.addAll(visitsPage.items()));

        List<User> users = new ArrayList<>();
        for (FollowBean b : beans) {
            User u = new User(b.getFirstName1(), b.getLastName1(), b.getFollower_handle(), b.getPassword1(), b.getImageUrl1());
            users.add(u);
        }
        boolean hasMorePages = (beans.size() == limit);
        return new Pair<>(users, hasMorePages);
    }

    @Override
    public int getFollowingCount(String username) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(username)
                        .build());

        return (int) table.query(r -> r.queryConditional(queryConditional)).items().stream().count();
    }

    @Override
    public int getFollowersCount(String username) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(username)
                        .build());

        List<FollowBean> beans = new ArrayList<>();
        PageIterable<FollowBean> pages = PageIterable.create(index.query(queryConditional));
        pages.stream().limit(1).forEach(page -> beans.addAll(page.items()));

        return beans.size();
    }

    @Override
    public boolean isFollower(String follower, String followee) {
        FollowBean bean = table.getItem(Key.builder().partitionValue(follower).sortValue(followee).build());
        return bean != null;
    }

    @Override
    public void remove(String follower, String followee) {
        table.deleteItem(Key.builder().partitionValue(follower).sortValue(followee).build());
    }

    @Override
    public void clear() {

        // WARNING: PERFORMS SCAN (EXPENSIVE)
        for (FollowBean bean : table.scan().items()) {
            table.deleteItem(bean);
        }

    }
}
