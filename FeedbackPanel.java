package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackPanel extends JPanel {
    private String studentId;
    private JTextArea txtFeedback;
    private JButton btnSubmit;
    private Connection connection; // Connection object for database operations

    public FeedbackPanel(String studentId, Connection connection) {
        this.studentId = studentId;
        this.connection = connection; // Store the connection for future use
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Feedback", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Feedback Text Area
        txtFeedback = new JTextArea();
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtFeedback);
        add(scrollPane, BorderLayout.CENTER);

        // Submit Button
        btnSubmit = new JButton("Submit Feedback");
        add(btnSubmit, BorderLayout.SOUTH);

        // Action Listener for Submit Button
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitFeedback();
            }
        });
    }

    /**
     * Submits the feedback to the database.
     */
    private void submitFeedback() {
        String feedback = txtFeedback.getText().trim();
        if (feedback.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your feedback before submitting.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Ensure connection is valid
            if (connection == null) {
                throw new SQLException("Database connection is not established.");
            }

            String query = "INSERT INTO feedback (student_id, feedback_text, feedback_date) VALUES (?, ?, NOW())";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, studentId);
            pstmt.setString(2, feedback);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtFeedback.setText(""); // Clear the text area
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit feedback. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting feedback: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
