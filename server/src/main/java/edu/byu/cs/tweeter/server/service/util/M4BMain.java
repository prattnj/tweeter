package edu.byu.cs.tweeter.server.service.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoUserDAO;

public class M4BMain {

    private static final int numUsers = 10000;
    private static final String outFileName = "fullnames.json";

    public static void main(String[] args) throws IOException {

        //buildJson();
        addAllUsers();

    }

    private static void addAllUsers() throws FileNotFoundException {

        FullNameData data = new Gson().fromJson(new FileReader(outFileName), FullNameData.class);

        List<User> users = new ArrayList<>();
        List<Follow> follows = new ArrayList<>();

        for (int i = 0; i < numUsers; i++) {
            String[] fullName = data.getData()[i].split(" ");
            String firstName = fullName[0];
            String lastName = fullName[1];
            String username = "@" + firstName.toLowerCase(Locale.ROOT) + lastName.toLowerCase(Locale.ROOT);
            String password = Integer.toString((lastName.toLowerCase(Locale.ROOT) + "123").hashCode());
            String imageURL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
            users.add(new User(firstName, lastName, username, password, imageURL));
        }

        User base = new User("Noah", "Pratt", "@njpratt", Integer.toString("fake".hashCode()), "https://np275-tweeter.s3.us-west-2.amazonaws.com/%40njpratt_profile");

        for (User u : users) {
            follows.add(new Follow(u, base));
        }

        users.add(base);

        new DynamoUserDAO().insertGroup(users);
        new DynamoFollowDAO().insertGroup(follows);

    }

    private static void buildJson() throws IOException {

        HashSet<String> firstSet = new HashSet<>();
        HashSet<String> lastSet = new HashSet<>();
        List<String> fullNames = new ArrayList<>();

        // no higher than 100,000 (based on nba_data.txt)
        int numUsers = 10000;

        File raw = new File("nba_data.txt");
        Scanner scanner = new Scanner(raw);
        while (scanner.hasNext()) {
            String fullName = scanner.nextLine();
            String[] both = fullName.split(" ");
            firstSet.add(both[0]);
            lastSet.add(both[1]);
        }

        String[] first = firstSet.toArray(new String[0]);
        String[] last = lastSet.toArray(new String[0]);
        Random rand = new Random();

        for (int i = 0; i < numUsers; i++) {
            boolean isTaken = true;
            String fullName = "";
            while (isTaken) {
                String firstName = first[rand.nextInt(first.length)];
                String lastName = last[rand.nextInt(last.length)];
                fullName = firstName + " " + lastName;
                isTaken = fullNames.contains(fullName);
            }
            fullNames.add(fullName);
        }

        FullNameData all = new FullNameData();
        all.setData(fullNames.toArray(new String[0]));
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(all);

        File jsonData = new File(outFileName);
        boolean success = jsonData.createNewFile();
        if (!success) return;

        BufferedWriter writer = new BufferedWriter(new FileWriter(jsonData));
        writer.write(json);
        writer.close();

    }

}
