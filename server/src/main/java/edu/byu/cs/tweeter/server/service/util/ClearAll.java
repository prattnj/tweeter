package edu.byu.cs.tweeter.server.service.util;

import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class ClearAll {

    public static void main(String[] args) {

        new DynamoUserDAO().clear();

    }

}
