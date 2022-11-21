package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.bean.AuthtokenBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoAuthtokenDAO implements AuthtokenDAO {

    private final String TABLE_NAME = "tweeter_authtokens";
    private final DynamoDbTable<AuthtokenBean> table;

    public DynamoAuthtokenDAO() {
        table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(AuthtokenBean.class));
    }

    @Override
    public void insert(AuthToken token) {

    }

    @Override
    public boolean validate(AuthToken token) {
        return false;
    }

    @Override
    public void remove(String token) {

    }

    @Override
    public String getUsername(String token) {
        return null;
    }

}
