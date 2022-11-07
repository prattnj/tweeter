package edu.byu.cs.tweeter.model.net.response;

public class RegisterResponse extends Response {
    RegisterResponse(boolean success) {
        super(success);
    }

    RegisterResponse(boolean success, String message) {
        super(success, message);
    }
}
