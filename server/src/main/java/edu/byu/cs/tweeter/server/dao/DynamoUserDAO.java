package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoUserDAO implements UserDAO {

    private final DynamoDbTable<UserBean> table;

    public DynamoUserDAO() {
        String TABLE_NAME = "tweeter_users";
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

        // WARNING: PERFORMS SCAN (EXPENSIVE)
        for (UserBean bean : table.scan().items()) {
            table.deleteItem(bean);
        }

    }
}
