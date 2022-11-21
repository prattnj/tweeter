package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.bean.AuthtokenBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoAuthtokenDAO implements AuthtokenDAO {

    private final DynamoDbTable<AuthtokenBean> table;

    public DynamoAuthtokenDAO() {
        String TABLE_NAME = "tweeter_authtokens";
        table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(AuthtokenBean.class));
    }

    @Override
    public void insert(AuthToken token) {
        AuthtokenBean bean = new AuthtokenBean(token.getToken(), token.getUsername());
        table.putItem(bean);
    }

    @Override
    public boolean validate(AuthToken token) {
        Key key = Key.builder().partitionValue(token.getUsername()).build();
        AuthtokenBean bean = table.getItem(key);
        if (bean == null) return false;
        if (!bean.getToken().equals(token.getToken())) return false;
        LocalDateTime expiration = token.getDatetime().plus(6, ChronoUnit.HOURS);
        return expiration.isAfter(LocalDateTime.now());
    }

    @Override
    public void remove(String username) {
        Key key = Key.builder().partitionValue(username).build();
        table.deleteItem(key);
    }

    @Override
    public String getUsername(String token) {
        Key key = Key.builder().sortValue(token).build();
        AuthtokenBean bean = table.getItem(key);
        if (bean == null) return null;
        return bean.getUsername();
    }

}
