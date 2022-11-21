package edu.byu.cs.tweeter.server.service;

import java.time.LocalDateTime;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class UserService {

    private final AuthtokenDAO authtoken_dao;
    private final UserDAO user_dao;

    public UserService(AuthtokenDAO authtoken_dao, UserDAO user_dao) {
        this.authtoken_dao = authtoken_dao;
        this.user_dao = user_dao;
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if (user_dao.find(request.getUsername()) == null) {
            throw new RuntimeException("[Bad Request] Invalid username");
        } else if (!user_dao.validate(request.getUsername(), myHash(request.getPassword()))) {
            throw new RuntimeException("[Bad Request] Invalid password");
        }

        User user = user_dao.find(request.getUsername());
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString(), user.getAlias(), LocalDateTime.now());
        authtoken_dao.insert(authToken);
        return new LoginResponse(user, authToken);
    }

    public LogoutResponse logout(LogoutRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        authtoken_dao.remove(request.getAuthToken().token);
        return new LogoutResponse(true);
    }

    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if (request.getFirstname() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if (request.getLastname() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        } else if (user_dao.find(request.getUsername()) != null) {
            throw new RuntimeException("[Bad Request] Username taken");
        }

        User user = new User(request.getFirstname(), request.getLastname(), request.getUsername(), myHash(request.getPassword()), request.getImage());
        AuthToken authToken = new AuthToken(UUID.randomUUID().toString(), user.getAlias(), LocalDateTime.now());
        user_dao.insert(user);
        authtoken_dao.insert(authToken);
        // todo s3 stuff
        return new RegisterResponse(user, authToken);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs a valid authToken");
        } else if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (!authtoken_dao.validate(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        User user = user_dao.find(request.getAlias());
        if (user == null) return new GetUserResponse(false, "No user found");
        else return new GetUserResponse(true, user);
    }

    private String myHash(String password) {
        return null; // TODO
    }
}
