package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class RegisterTest {

    @Test
    public void testRegister() {
        RegisterRequest req = new RegisterRequest("Test", "Testy", "@test2", "test", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        RegisterResponse resp = new UserService(new DynamoAuthtokenDAO(), new DynamoUserDAO()).register(req);
        System.out.println("This line here for debug purposes.");
    }

}
