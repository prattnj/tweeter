package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetCountsTask extends AuthenticatedTask {

    public static final String COUNT_KEY1 = "count1";
    public static final String COUNT_KEY2 = "count2";

    /**
     * The user whose count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;

    private int count1;
    private int count2;

    public GetCountsTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
    }

    protected User getTargetUser() {
        return targetUser;
    }

    @Override
    protected void runTask() {
        count1 = runCountTask1();
        count2 = runCountTask2();

        // Call sendSuccessMessage if successful
        sendSuccessMessage();
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }

    protected int runCountTask1() {
        return 20;
    }

    protected int runCountTask2() {
        return 20;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY1, count1);
        msgBundle.putInt(COUNT_KEY2, count2);
    }
}
