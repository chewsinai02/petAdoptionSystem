package com.mycompany.petAdoptionSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/adopt";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Establishes a connection to the database using the specified URL, user,
     * and password.
     *
     * @return a Connection object if successful, or null if the connection
     * attempt fails due to a SQLException.
     */
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}
