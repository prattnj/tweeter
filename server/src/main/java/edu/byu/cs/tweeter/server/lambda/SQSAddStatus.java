package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;

public class SQSAddStatus implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent input, Context context) {

        for (SQSEvent.SQSMessage msg : input.getRecords()) {

            SQSFollowerData data = new Gson().fromJson(msg.getBody(), SQSFollowerData.class);
            List<String> receivers = new ArrayList<>();
            for (User u : data.getFollowers()) receivers.add(u.getAlias());
            new DynamoFeedDAO().insertGroup(receivers, data.getStatus());

        }

        return null;
    }
}
