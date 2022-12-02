package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;

public class SQSGetFollowers implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent input, Context context) {

        String queueURL = "https://sqs.us-west-2.amazonaws.com/287157383925/tweeter_followgroups";

        for (SQSEvent.SQSMessage msg : input.getRecords()) {

            Status status = new Gson().fromJson(msg.getBody(), Status.class);
            new DynamoStoryDAO().insert(status);
            List<List<User>> groups = new ArrayList<>();
            List<User> followers = new DynamoFollowDAO().getAllFollowers(status.getUser().getAlias());

            int groupSize = 25;
            int numGroups = followers.size() / groupSize;
            if (followers.size() % groupSize != 0) numGroups++;
            for (int i = 0; i < numGroups; i++) {
                int startingIndex = i * groupSize;
                List<User> tempList = new ArrayList<>();
                for (int j = startingIndex; j < groupSize + startingIndex; j++) {
                    if (j > followers.size() - 1) break;
                    tempList.add(followers.get(j));
                }
                groups.add(tempList);
            }

            for (List<User> group : groups) {

                SQSFollowerData data = new SQSFollowerData(status, group);

                SendMessageRequest req = new SendMessageRequest()
                        .withQueueUrl(queueURL)
                        .withMessageBody(new Gson().toJson(data));

                AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
                sqs.sendMessage(req);

            }

        }

        return null;
    }
}
