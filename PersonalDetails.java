package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class PersonalDetails extends JFrame {

    private Connection con;  // Database connection
    private String studentID;  // Student ID

    // Updated constructor to accept studentID and Connection
    public PersonalDetails(String studentID, Connection con) {
        this.studentID = studentID;
        this.con = con;  // Save the connection

        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null);

        JLabel heading = new JLabel("Personal Details");
        heading.setBounds(310, 30, 500, 50);
        heading.setFont(new Font("Bodoni MT", Font.BOLD, 40));
        add(heading);

        // Add other components like text fields, combo boxes, etc.
        // For example, Name, Address, Phone Number, etc.
        JLabel labelStudentID = new JLabel("Student ID: " + studentID);
        labelStudentID.setBounds(50, 100, 200, 30);
        add(labelStudentID);

        // Other components like name, address, etc. can be added similarly

        setVisible(true);
    }

    public static void main(String[] args) {
        // Example usage, fetch connection from Conn class
        Connection con = Conn.getInstance().getConnection();  // Get database connection
        new PersonalDetails("123", con);  // Pass student ID and connection
    }
}
