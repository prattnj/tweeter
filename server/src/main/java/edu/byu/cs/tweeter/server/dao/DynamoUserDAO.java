package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
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

        for (UserBean bean : table.scan().items()) {
            table.deleteItem(bean);
        }

    }
}
