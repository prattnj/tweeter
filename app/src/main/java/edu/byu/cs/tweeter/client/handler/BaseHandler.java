package edu.byu.cs.tweeter.client.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.abstract_task.BackgroundTask;
import edu.byu.cs.tweeter.client.observer_interface.ServiceObserver;

public abstract class BaseHandler extends Handler {

    ServiceObserver observer;

    public BaseHandler(ServiceObserver observer) {
        this.observer = observer;
    }

    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
        if (success) {
            handleSuccess(msg);
        } else if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(BackgroundTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }

    public abstract void handleSuccess(Message msg);

}
