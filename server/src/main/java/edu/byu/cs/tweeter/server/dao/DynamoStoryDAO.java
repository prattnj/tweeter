package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.bean.StoryBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoStoryDAO implements StoryDAO {

    private final String TABLE_NAME = "tweeter_stories";
    private final DynamoDbTable<StoryBean> table;

    public DynamoStoryDAO() {
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(StoryBean.class));
    }

    @Override
    public void insert(Status status) {

    }

    @Override
    public Pair<List<Status>, Boolean> getPage(String creator, Status lastStatus, int limit) {
        return null;
    }
}
