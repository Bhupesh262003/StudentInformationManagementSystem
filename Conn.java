package student.information.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conn {
    private Connection con;
    private static Conn instance;

    // Database credentials and URL
    private final String DB_URL = "jdbc:mysql://localhost:3306/sims";
    private final String DB_USERNAME = "BMDB";
    private final String DB_PASSWORD = "Bhup@2003";

    // Private constructor for Singleton pattern
    Conn() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection with the database
            con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "JDBC Driver not found. Please add the JDBC library to the project.", "Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database. Please check the connection details.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to get the singleton instance
    public static synchronized Conn getInstance() {
        if (instance == null) {
            instance = new Conn();
        }
        return instance;
    }

    // Method to get the connection
    public Connection getConnection() {
        try {
            // If the connection is closed or null, establish a new one
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                System.out.println("Database connection re-established successfully.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to re-establish the database connection.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return con;
    }

    // Method to close the connection
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing the database connection.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
