package edu.byu.cs.tweeter.client.observer_interface;

public interface DoubleParamSuccessObserver<T, U> extends ServiceObserver {
    void success(T foo, U bar);
}
