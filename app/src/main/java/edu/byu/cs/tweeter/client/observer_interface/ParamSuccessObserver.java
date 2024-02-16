package edu.byu.cs.tweeter.client.observer_interface;

public interface ParamSuccessObserver<T> extends ServiceObserver {
    void success(T var);
}
