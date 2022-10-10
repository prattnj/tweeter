package edu.byu.cs.tweeter.client.observer_interface;

public abstract class ServiceObserver {
    public abstract void handleFailure(String message);
    public abstract void handleException(Exception exception);
}
