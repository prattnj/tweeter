package edu.byu.cs.tweeter.client.observer_interface;

public interface ServiceObserver {
    void handleFailure(String message);
    void handleException(Exception exception);
}
