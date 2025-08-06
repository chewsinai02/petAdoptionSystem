package com.mycompany.petAdoptionSystem;

public class UserSession {
    private static boolean loggedIn = false;
    private static int currentUserId = -1;

    public static boolean isLoggedIn() { return loggedIn; }
    public static void setLoggedIn(boolean value) { loggedIn = value; }
    public static int getCurrentUserId() { return currentUserId; }
    public static void setCurrentUserId(int userId) { currentUserId = userId; }
}
