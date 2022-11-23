package edu.byu.cs.tweeter.server.service.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.AuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoAuthtokenDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFeedDAO;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoStoryDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class PopulateMain {

    public static void main(String[] args) throws IOException {

        UserDAO userDAO = new DynamoUserDAO();
        FollowDAO followDAO = new DynamoFollowDAO();
        FeedDAO feedDAO = new DynamoFeedDAO();
        StoryDAO storyDAO = new DynamoStoryDAO();
        AuthtokenDAO authtokenDAO = new DynamoAuthtokenDAO();

        // DO NOT UNCOMMENT UNLESS USERS TABLE IS EMPTY
        //initializeUserData();

        //clearAll(userDAO, followDAO, feedDAO, storyDAO, authtokenDAO);

        /*UserData data = new Gson().fromJson(new FileReader("UserData.json"), UserData.class);
        HashSet<User> users = data.getUsers();

        // Add users
        for (User u : users) {
            //userDAO.insert(u);
        }*/

        // Add follow relationships
        // Every user follows every other user (not themselves)
        // THIS FOR LOOP TAKES A LONG TIME TO RUN (45 MINUTES FOR 1 WRITE UNIT)
        /*int progress = 0;
        for (User u : users) {
            System.out.println("FOLLOW PROGRESS: " + progress + "/" + users.size());
            for (User v : users) {
                if (!u.getAlias().equals(v.getAlias())) {
                    followDAO.insert(u, v);
                }
            }
            progress++;
        }*/

        // Create posts
        // THIS FOR LOOP TAKES A LONG TIME TO RUN (45 MINUTES FOR 1 WRITE UNIT)
        /*int progress = 0;
        for (User u : users) {
            System.out.println("STATUS PROGRESS: " + progress + "/" + users.size());
            List<String> mentions = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            mentions.add("@test");
            urls.add("https://byu.edu");
            Status status = new Status("This is a test status. @test told me to post this at https://byu.edu.",
                    u, LocalDateTime.now().toString(), urls, mentions);
            storyDAO.insert(status);
            List<User> followers = followDAO.getAllFollowers(u.getAlias());
            for (User f : followers) {
                feedDAO.insert(f.getAlias(), status);
            }
            progress++;
        }*/

    }

    private static void initializeUserData() throws IOException {
        HashSet<User> users = new HashSet<>();
        for (int i = 0; i < 55; i++) {
            String firstName = NameData.getFirstNames()[new Random().nextInt(NameData.getFirstNames().length)];
            String lastName = NameData.getLastNames()[new Random().nextInt(NameData.getLastNames().length)];
            String username = "@" + firstName.toLowerCase(Locale.ROOT) + lastName.toLowerCase(Locale.ROOT);
            String password = Integer.toString((lastName.toLowerCase(Locale.ROOT) + "123").hashCode());
            String imageURL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
            users.add(new User(firstName, lastName, username, password, imageURL));
        }

        UserData userData = new UserData(users);
        String userDataString = new GsonBuilder().setPrettyPrinting().create().toJson(userData);
        File userDataJsonFile = new File("UserData.json");
        if (!userDataJsonFile.createNewFile()) {
            System.out.println("Error creating file.");
            return;
        }
        FileWriter fr = new FileWriter(userDataJsonFile);
        fr.write(userDataString);
        fr.flush();
        fr.close();
    }

    private static void clearAll(UserDAO udao, FollowDAO fdao, FeedDAO fdao2, StoryDAO sdao, AuthtokenDAO adao) {
        udao.clear();
        fdao.clear();
        fdao2.clear();
        sdao.clear();
        adao.clear();
    }

}
