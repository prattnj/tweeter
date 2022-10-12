package edu.byu.cs.tweeter.client.observer_interface;

public abstract class ParamSuccessObserver<T> extends ServiceObserver {
    public abstract void success(T var);
}
