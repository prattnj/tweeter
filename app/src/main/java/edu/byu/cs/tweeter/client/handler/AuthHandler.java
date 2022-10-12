package edu.byu.cs.tweeter.client.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.observer_interface.ParamSuccessObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthHandler extends BaseHandler {

    private final ParamSuccessObserver<User> observer;

    public AuthHandler(ParamSuccessObserver<User> observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    public void handleSuccess(Message msg) {
        User user = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);
        observer.success(user);
    }

}
