//package org.example.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserManager {
//    private List<User> users;
//
//    public UserManager() {
//        this.users = new ArrayList<>();
//    }
//
//    public User createUser(String username, String password) {
//        try {
//            User newUser = new User(username, password);
//            users.add(newUser);
//            return newUser;
//        } catch (IllegalArgumentException e) {
//            System.out.println("Error creating user: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public User logIn(String username, String password) {
//        try {
//            for (User user : users) {
//                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                    return user;
//                }
//            }
//            return null; // User not found or incorrect password
//        } catch (Exception e) {
//            System.out.println("Error logging in: " + e.getMessage());
//            return null;
//        }
//    }
//}

package org.example.User;


import java.util.HashMap;

import java.util.Map;

public class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public User createUser(String username, String password) {
        try {
            User newUser = new User(username, password);
            users.put(username, newUser);
            return newUser;
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating user: " + e.getMessage());
            return null;
        }
    }

    public User logIn(String username, String password) {
        try {
            User user = users.get(username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null; // User not found or incorrect password
        } catch (Exception e) {
            System.out.println("Error logging in: " + e.getMessage());
            return null;
        }
    }
}
