package student.information.management.system;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class FeedbackManagement extends JFrame implements ActionListener {

    JTextArea feedbackField;
    JButton submitButton;
    private Connection con;  // Database connection
    private String studentID;  // Student ID

    // Updated constructor to accept studentID and Connection
    public FeedbackManagement(String studentID, Connection con) {
        this.studentID = studentID;
        this.con = con;  // Save the connection

        // UI setup
        JLabel label = new JLabel("Enter Feedback:");
        label.setBounds(50, 50, 150, 30);

        feedbackField = new JTextArea();
        feedbackField.setBounds(50, 100, 300, 150);

        submitButton = new JButton("Submit Feedback");
        submitButton.setBounds(150, 300, 150, 30);
        submitButton.addActionListener(this);

        add(label);
        add(feedbackField);
        add(submitButton);

        setSize(400, 400);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Ensure the window closes properly
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String feedback = feedbackField.getText();
        if (feedback.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Feedback cannot be empty");
            return; // Exit if feedback is empty
        }

        // Insert feedback into the database using the provided connection
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO feedback (student_id, content) VALUES (?, ?)")) {
            ps.setString(1, studentID);  // Insert student ID as well
            ps.setString(2, feedback);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Feedback submitted successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error submitting feedback: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example usage, fetch connection from Conn class
        Connection con = Conn.getInstance().getConnection();  // Get database connection
        new FeedbackManagement("123", con);  // Pass student ID and connection
    }
}
