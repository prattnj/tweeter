package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.backgroundTask.abstract_task.PagedStatusTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    private static final String URL_PATH = "/getfeed";
    private static final String LOG_TAG = "GetFeedTask";

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected void getItems() {

        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();

            GetFeedRequest request = new GetFeedRequest(authToken, targetUserAlias, limit, lastItem);
            GetFeedResponse response = getServerFacade().getFeed(request, URL_PATH);

            if (response.isSuccess()) {
                this.items = response.getStatuses();
                this.hasMorePages = response.getHasMorePages();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get feed", ex);
            sendExceptionMessage(ex);
        }

    }
}
