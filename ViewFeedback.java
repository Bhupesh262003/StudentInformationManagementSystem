package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewFeedback extends JFrame {
    private JTextArea feedbackArea;
    private Connection connection; // Database connection

    public ViewFeedback(Connection connection) {
        this.connection = connection;
        setTitle("View Feedback");
        setSize(600, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window without exiting the app

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("Serif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(feedbackArea); // Make it scrollable
        scrollPane.setBounds(20, 20, 550, 430);
        add(scrollPane);

        fetchFeedback(); // Fetch feedback data

        setVisible(true);
    }

    private void fetchFeedback() {
        String query = "SELECT * FROM feedback";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                feedbackArea.append("Feedback ID: " + rs.getInt("feedback_id") + "\n");
                feedbackArea.append("Student ID: " + rs.getString("student_id") + "\n");
                feedbackArea.append("Teacher ID: " + rs.getString("teacher_id") + "\n");
                feedbackArea.append("Feedback: " + rs.getString("feedback_text") + "\n");
                feedbackArea.append("Submission Date: " + rs.getDate("submission_date") + "\n");
                feedbackArea.append("Rating: " + rs.getInt("rating") + "\n");
                feedbackArea.append("Created At: " + rs.getTimestamp("created_at") + "\n");
                feedbackArea.append("------------------------------------\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching feedback: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Example usage
        Connection connection = Conn.getInstance().getConnection();
        new ViewFeedback(connection);
    }
}
