package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ViewPersonalDetailsPanel extends JPanel {
    private Connection connection; // Connection object for database operations

    public ViewPersonalDetailsPanel(String studentId, String name, String fatherName, String dob, String address, String phone, String email, String gender, Connection connection) {
        this.connection = connection; // Store the connection for future use
        setLayout(new BorderLayout());

        // Create and configure the details panel
        JPanel detailsPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        detailsPanel.add(new JLabel("Student ID:"));
        detailsPanel.add(new JLabel(studentId));

        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(new JLabel(name));

        detailsPanel.add(new JLabel("Father's Name:"));
        detailsPanel.add(new JLabel(fatherName));

        detailsPanel.add(new JLabel("Date of Birth:"));
        detailsPanel.add(new JLabel(dob));

        detailsPanel.add(new JLabel("Address:"));
        detailsPanel.add(new JLabel(address));

        detailsPanel.add(new JLabel("Phone:"));
        detailsPanel.add(new JLabel(phone));

        detailsPanel.add(new JLabel("Email:"));
        detailsPanel.add(new JLabel(email));

        detailsPanel.add(new JLabel("Gender:"));
        detailsPanel.add(new JLabel(gender));

        add(detailsPanel, BorderLayout.CENTER);

        // Additional features can be added here using the connection object
        // For example, you can add a button to update personal details:
        JButton updateButton = new JButton("Update Details");
        updateButton.addActionListener(e -> {
            // Logic for updating student details in the database
            // This is where you would use the connection object
            // For example, show a dialog for editing details and save to the database
            updateStudentDetails(studentId);
        });

        add(updateButton, BorderLayout.SOUTH);
    }

    // Example method to handle updating student details
    private void updateStudentDetails(String studentId) {
        // Logic for updating student details goes here
        // You can use the connection object to execute SQL updates
        // Example: Prepare an update statement, show a dialog to get new details, etc.
        JOptionPane.showMessageDialog(this, "Update functionality not yet implemented.", "Update Student Details", JOptionPane.INFORMATION_MESSAGE);
    }
}
