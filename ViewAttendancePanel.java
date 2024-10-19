package student.information.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ViewAttendancePanel displays attendance records for a specific student.
 */
public class ViewAttendancePanel extends JPanel {
    private Connection connection; // Database connection
    private DefaultTableModel model; // Table model for attendance records
    private JTextField studentIdField; // Input field for student ID

    public ViewAttendancePanel(String studentId, Connection connection) {
        this.connection = connection; // Initialize the connection

        setLayout(new BorderLayout()); // Set layout for the panel

        // Create and configure input panel for student ID
        JPanel inputPanel = new JPanel();
        studentIdField = new JTextField(15); // Input field for student ID
        studentIdField.setText(studentId); // Pre-fill with passed studentId
        JButton viewButton = new JButton("View Attendance"); // Button to view attendance

        inputPanel.add(new JLabel("Student ID:")); // Label for input field
        inputPanel.add(studentIdField); // Add input field to the panel
        inputPanel.add(viewButton); // Add button to the panel

        add(inputPanel, BorderLayout.NORTH); // Add input panel to the north

        // Create and configure the attendance table
        String[] columnNames = {"Date", "Status"}; // Column names for the table
        model = new DefaultTableModel(columnNames, 0); // Initialize the table model
        JTable attendanceTable = new JTable(model); // Create table with the model
        JScrollPane scrollPane = new JScrollPane(attendanceTable); // Add scroll pane for the table
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the panel

        // Title label for the panel
        JLabel titleLabel = new JLabel("Attendance Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set title font
        add(titleLabel, BorderLayout.WEST); // Add title to the west of the panel

        // Info label for additional instructions
        JLabel infoLabel = new JLabel("Note: Only teachers can manage attendance.");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14)); // Set font for info label
        add(infoLabel, BorderLayout.SOUTH); // Add info label to the south

        // Add action listener to the button
        viewButton.addActionListener(e -> {
            String enteredStudentId = studentIdField.getText().trim(); // Get student ID from input
            if (!enteredStudentId.isEmpty()) {
                fetchAttendanceData(enteredStudentId); // Fetch attendance data
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid Student ID.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE); // Warn if input is empty
            }
        });

        // Automatically fetch attendance data for the pre-filled studentId
        if (!studentId.isEmpty()) {
            fetchAttendanceData(studentId);
        }
    }

    /**
     * Fetches attendance data from the database and populates the table model.
     *
     * @param studentId The ID of the student whose attendance records are to be fetched.
     */
    private void fetchAttendanceData(String studentId) {
        model.setRowCount(0); // Clear existing rows
        String query = "SELECT attendance_date, status FROM attendance WHERE student_id = ?"; // SQL query
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, studentId); // Set the student ID parameter
            ResultSet rs = pstmt.executeQuery(); // Execute the query
            boolean hasData = false; // Flag to check if records are found

            // Iterate through the result set and populate the table model
            while (rs.next()) {
                String date = rs.getDate("attendance_date").toString(); // Get attendance date
                String status = rs.getString("status"); // Get attendance status
                model.addRow(new Object[]{date, status}); // Add row to the table model
                hasData = true; // Mark that data was found
            }

            // Show message if no records were found
            if (!hasData) {
                JOptionPane.showMessageDialog(this, "No attendance records found for student ID: " + studentId,
                        "No Records", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for debugging
            JOptionPane.showMessageDialog(this, "Error fetching attendance data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
