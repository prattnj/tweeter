package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class DynamoUserDAO implements UserDAO {

    private final DynamoDbTable<UserBean> table;
    private final String TABLE_NAME = "tweeter_users";

    public DynamoUserDAO() {
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(UserBean.class));
    }

    @Override
    public void insert(User user) {
        UserBean bean = new UserBean(user.getFirstName(), user.getLastName(), user.getAlias(), user.getPassword(), user.getImageUrl());
        table.putItem(bean);
    }

    @Override
    public void insertGroup(List<User> users) {

        List<UserBean> allBeans = new ArrayList<>();
        for (User user : users) allBeans.add(new UserBean(user.getFirstName(), user.getLastName(), user.getAlias(), user.getPassword(), user.getImageUrl()));
        List<List<UserBean>> groups = new ArrayList<>();

        // Organize into groups, max size 25
        int groupSize = 25;
        int numGroups = allBeans.size() / groupSize;
        if (allBeans.size() % groupSize != 0) numGroups++;
        for (int i = 0; i < numGroups; i++) {
            int startingIndex = i * groupSize;
            List<UserBean> tempList = new ArrayList<>();
            for (int j = startingIndex; j < groupSize + startingIndex; j++) {
                if (j > allBeans.size() - 1) break;
                tempList.add(allBeans.get(j));
            }
            groups.add(tempList);
        }

        System.out.println("debug");

        int progress = 1;
        for (List<UserBean> beans : groups) {
            System.out.println("Writing users group " + progress + "/" + groups.size());
            insertChunkOfBeans(beans);
            progress++;
        }

    }

    private void insertChunkOfBeans(List<UserBean> beans) {

        WriteBatch.Builder<UserBean> test = WriteBatch.builder(UserBean.class)
                .mappedTableResource(table);
        for (UserBean bean : beans) {
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
    public User find(String alias) {
        Key key = Key.builder().partitionValue(alias).build();
        UserBean bean = table.getItem(key);
        if (bean == null) return null;
        return new User(bean.getFirstName(), bean.getLastName(), bean.getUsername(), bean.getPassword(), bean.getImageUrl());
    }

    @Override
    public boolean validate(String username, String password) {
        Key key = Key.builder().partitionValue(username).build();
        UserBean bean = table.getItem(key);
        if (bean == null) return false;
        return bean.getPassword().equals(password);
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

        int progress = 1;
        for (UserBean bean : table.scan().items()) {
            System.out.println("Clearing: " + progress);
            table.deleteItem(bean);
            progress++;
        }

    }
}
