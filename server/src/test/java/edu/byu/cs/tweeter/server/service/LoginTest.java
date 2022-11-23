package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class LoginTest {

    @Test
    public void testLogin() {
        LoginRequest req = new LoginRequest("@fluximpala", "test");
        LoginResponse resp = new UserService(new DynamoAuthtokenDAO(), new DynamoUserDAO()).login(req);
        System.out.println("Debug");
    }

}
