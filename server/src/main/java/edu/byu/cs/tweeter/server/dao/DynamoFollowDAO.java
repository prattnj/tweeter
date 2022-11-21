package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.bean.FollowBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoFollowDAO implements FollowDAO {

    private final String TABLE_NAME = "tweeter_follows";
    private final DynamoDbTable<FollowBean> table;

    public DynamoFollowDAO() {
        this.table = DynamoDAOUtil.getInstance().getClient().table(TABLE_NAME, TableSchema.fromBean(FollowBean.class));
    }

    @Override
    public void insert(String follower, String followee) {

    }

    @Override
    public List<User> getAllFollowers(String username) {
        return null;
    }

    @Override
    public Pair<List<User>, Boolean> getPageFollowing(String username, String lastUser, int limit) {
        return null;
    }

    @Override
    public Pair<List<User>, Boolean> getPageFollowers(String username, String lastUser, int limit) {
        return null;
    }

    @Override
    public int getFollowingCount(String username) {
        return 0;
    }

    @Override
    public int getFollowersCount(String username) {
        return 0;
    }

    @Override
    public boolean isFollower(String follower, String followee) {
        return false;
    }

    @Override
    public void remove(String follower, String followee) {

    }
}
