package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.abstract_task.PagedStatusTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String URL_PATH = "/getstory";
    private static final String LOG_TAG = "GetStoryTask";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected void getItems() {

        try {
            String targetUserAlias = targetUser == null ? null : targetUser.getAlias();

            GetStoryRequest request = new GetStoryRequest(authToken, targetUserAlias, limit, lastItem);
            GetStoryResponse response = getServerFacade().getStory(request, URL_PATH);


            if (response.isSuccess()) {
                this.items = response.getStatuses();
                this.hasMorePages = response.getHasMorePages();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get story", ex);
            sendExceptionMessage(ex);
        }

    }
}
