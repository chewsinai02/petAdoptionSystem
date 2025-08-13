/**
 * Manages session information for the currently logged-in user.
 * Stores details such as username, role, and authentication state.
 * 
 * @author Chew Sin Ai (finalize, and debug)
 * @version 1.0
 */
package com.mycompany.petAdoptionSystem;

public class UserSession {

    private static boolean loggedIn = false;
    private static int currentUserId = -1;
    private static boolean isAdmin = false;

    /**
     * Returns true if the user is logged in, false otherwise.
     * <p>
     * This is a static method that will return the current state of the user
     * session. If the user is logged in, this method will return true. If the
     * user is not logged in, this method will return false.
     *
     * @return true if the user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Sets the logged-in state of the user session.
     * <p>
     * This is a static method that updates the current state of the user
     * session based on the provided boolean value. If the value is true, the
     * user is considered logged in. If the value is false, the user is
     * considered logged out.
     *
     * @param value true to set the user session as logged in, false to set it
     * as logged out
     */
    public static void setLoggedIn(boolean value) {
        loggedIn = value;
    }

    /**
     * Returns the ID of the user who is currently logged in.
     * <p>
     * This is a static method that will return the ID of the user who is
     * currently logged in. If the user is not logged in, this method will
     * return -1.
     *
     * @return the ID of the user who is currently logged in
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Sets the ID of the user who is currently logged in.
     * <p>
     * This is a static method that updates the current user ID in the session
     * to the given user ID. This method should be called whenever there is a
     * change in the logged-in user.
     *
     * @param userId the ID of the user to set as the current user
     */
    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }

    /**
     * Returns true if the user is an administrator, false otherwise.
     * <p>
     * This is a static method that will return true if the user is an
     * administrator and false otherwise. This can be used to check access
     * control for certain features in the application.
     *
     * @return true if the user is an administrator, false otherwise
     */
    public static boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets the admin status of the user session.
     * <p>
     * This is a static method that updates the current admin status in the
     * session to the given boolean value. This method should be called whenever
     * there is a change in the administrator status of the logged-in user.
     *
     * @param admin true if the user is an administrator, false otherwise
     */
    public static void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
