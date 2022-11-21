package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.bean.FeedBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoFeedDAO implements FeedDAO {

    private final String TABLE_NAME = "tweeter_feeds";
    private final DynamoDbTable<FeedBean> table;

    public DynamoFeedDAO() {
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(FeedBean.class));
    }

    @Override
    public void insert(String receiver, Status status) {

    }

    @Override
    public Pair<List<Status>, Boolean> getPage(String receiver, Status lastStatus, int limit) {
        return null;
    }
}
