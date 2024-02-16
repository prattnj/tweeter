package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.backgroundTask.abstract_task.GetCountTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;

public class GetFollowersCountTask extends GetCountTask {

    private static final String URL_PATH = "/getfollowerscount";
    private static final String LOG_TAG = "GetFollowersCountTask";

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected void runCountTask() {

        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();

            GetFollowersCountRequest request = new GetFollowersCountRequest(authToken, targetUserAlias);
            GetFollowersCountResponse response = getServerFacade().getFollowersCount(request, URL_PATH);

            if (response.isSuccess()) {
                this.count = response.getCount();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }

        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers count", ex);
            sendExceptionMessage(ex);
        }
    }
}
