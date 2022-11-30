package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Test;

public class ClearTest {

    @Test
    public void clearTest() {
        new DynamoAuthtokenDAO().scanClear();
        new DynamoStoryDAO().scanClear();
        new DynamoFollowDAO().scanClear();
        new DynamoUserDAO().scanClear();
        new DynamoFeedDAO().scanClear();
    }

}
