package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.bean.AuthtokenBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

public class DynamoAuthtokenDAO implements AuthtokenDAO {

    private final DynamoDbTable<AuthtokenBean> table;
    private final DynamoDbIndex<AuthtokenBean> index;
    private final String TABLE_NAME = "tweeter_authtokens";

    public DynamoAuthtokenDAO() {
        table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(AuthtokenBean.class));
        String INDEX_NAME = "tweeter_authtokens_index";
        index = table.index(INDEX_NAME);
    }

    @Override
    public void insert(AuthToken token) {
        AuthtokenBean bean = new AuthtokenBean(token.getToken(), token.getUsername(), token.getDatetime());
        table.putItem(bean);
    }

    @Override
    public boolean validate(AuthToken token) {
        Key key = Key.builder().partitionValue(token.getUsername()).sortValue(token.getToken()).build();
        AuthtokenBean bean = table.getItem(key);
        if (bean == null) return false;
        if (!bean.getToken().equals(token.getToken())) return false;
        LocalDateTime expiration = LocalDateTime.parse(token.getDatetime()).plus(30, ChronoUnit.HOURS);
        return expiration.isAfter(LocalDateTime.now());
    }

    @Override
    public void remove(String username) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(username).build());

        List<AuthtokenBean> beans = table.query(queryConditional).items().stream().collect(Collectors.toList());

        for (AuthtokenBean bean : beans) {
            table.deleteItem(bean);
        }
    }

    @Override
    public String getUsername(String token) {
        Key key = Key.builder().partitionValue(token).build();
        List<AuthtokenBean> beans = new ArrayList<>();
        PageIterable<AuthtokenBean> pages = PageIterable.create(index.query(QueryConditional.keyEqualTo(key)));
        pages.stream()
                .limit(1)
                .forEach(visitsPage -> beans.addAll(visitsPage.items()));
        AuthtokenBean bean = beans.get(0);
        if (bean == null) return null;
        return bean.getUsername();
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

        for (AuthtokenBean bean : table.scan().items()) {
            table.deleteItem(bean);
        }

    }
}
