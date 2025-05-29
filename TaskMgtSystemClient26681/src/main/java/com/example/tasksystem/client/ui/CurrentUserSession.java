package com.example.tasksystem.client.ui;

import com.example.tasksystem.shared.entities.User;

public class CurrentUserSession {
    private static User loggedInUser;

    private CurrentUserSession() {
        // Private constructor to prevent instantiation
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static void clearSession() {
        loggedInUser = null;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
}
