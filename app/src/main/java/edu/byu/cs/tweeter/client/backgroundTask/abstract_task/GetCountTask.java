package edu.byu.cs.tweeter.client.backgroundTask.abstract_task;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AuthenticatedTask {

    public static final String COUNT_KEY = "count";

    /**
     * The user whose count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected final User targetUser;

    protected int count;

    public GetCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
    }

    protected User getTargetUser() {
        return targetUser;
    }

    @Override
    protected void runTask() {
        //count = runCountTask();
        runCountTask();

        // Call sendSuccessMessage if successful
        //sendSuccessMessage();
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }

    protected abstract void runCountTask();

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }
}
