package edu.byu.cs.tweeter.server.service.util;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class ClearAll {

    public static void main(String[] args) {

        new DynamoStoryDAO().clear();
        new DynamoFeedDAO().clear();
        //new DynamoFollowDAO().scanClear();
        //new DynamoUserDAO().clear();

    }

}
