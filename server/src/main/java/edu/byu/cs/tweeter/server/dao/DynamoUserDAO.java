package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.UserBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoUserDAO implements UserDAO {

    private final String TABLE_NAME = "tweeter_users";
    private final DynamoDbTable<UserBean> table;

    public DynamoUserDAO() {
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(UserBean.class));
    }

    @Override
    public void insert(User user) {

    }

    @Override
    public User find(String alias) {
        return null;
    }

    @Override
    public boolean validate(String username, String password) {
        return false;
    }
}
