package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import com.toedter.calendar.JDateChooser; // Import JDateChooser

/**
 * AttendanceManagement provides an interface for teachers to mark student attendance.
 */
public class AttendanceManagement extends JFrame {

    private JTextField studentIdField;
    private JComboBox<String> statusComboBox;
    private JButton markAttendanceButton, closeButton, viewAttendanceButton;
    private Connection connection;
    private String teacherId; // To hold the ID of the teacher
    private JDateChooser dateChooser; // Date chooser for selecting attendance date

    public AttendanceManagement(Connection connection, String teacherId) {
        this.connection = connection;
        this.teacherId = teacherId; // Store the teacher ID for future use
        setTitle("Attendance Management");
        setSize(900, 600);
        setLocation(350, 50);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Adding heading
        JLabel heading = new JLabel("ATTENDANCE MANAGEMENT");
        heading.setBounds(200, 30, 600, 50);
        heading.setFont(new Font("Bodoni MT", Font.BOLD, 40));
        add(heading);

        // Student ID Label and Field
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setBounds(50, 150, 150, 30);
        add(studentIdLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(200, 150, 150, 30);
        add(studentIdField);

        // Status Label and ComboBox
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(50, 200, 150, 30);
        add(statusLabel);

        String[] statusOptions = {"Present", "Absent"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setBounds(200, 200, 150, 30);
        add(statusComboBox);

        // Date Label and DateChooser
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(50, 250, 150, 30);
        add(dateLabel);

        dateChooser = new JDateChooser(); // Initialize JDateChooser
        dateChooser.setBounds(200, 250, 150, 30);
        add(dateChooser);

        // Mark Attendance Button
        markAttendanceButton = new JButton("Mark Attendance");
        markAttendanceButton.setBounds(200, 310, 150, 40);
        markAttendanceButton.setBackground(new Color(0, 153, 76)); // Green color
        markAttendanceButton.setForeground(Color.WHITE);
        markAttendanceButton.addActionListener(e -> markAttendance());
        add(markAttendanceButton);

        // View Attendance Button
        viewAttendanceButton = new JButton("View Attendance");
        viewAttendanceButton.setBounds(200, 370, 150, 40);
        viewAttendanceButton.setBackground(new Color(0, 102, 204)); // Blue color
        viewAttendanceButton.setForeground(Color.WHITE);
        viewAttendanceButton.addActionListener(e -> viewAttendance());
        add(viewAttendanceButton);

        // Close Button
        closeButton = new JButton("Close");
        closeButton.setBounds(400, 310, 150, 40);
        closeButton.setBackground(new Color(204, 0, 0)); // Red color
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose()); // Close the window
        add(closeButton);

        setVisible(true);
    }

    private void markAttendance() {
        String studentId = studentIdField.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();

        // Get the selected date from the date chooser
        java.util.Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return;
        }

        // Convert java.util.Date to java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.");
            return;
        }

        // Utilize AttendanceManagementLogic to check if the student exists and mark attendance
        AttendanceManagementLogic attendanceLogic = new AttendanceManagementLogic(connection);

        // Check if the student exists
        if (!attendanceLogic.isStudentExists(studentId)) {
            JOptionPane.showMessageDialog(this, "No student record found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Updated attendance marking with the selected date
        boolean success = attendanceLogic.markAttendance(studentId, status, teacherId, sqlDate);

        if (success) {
            JOptionPane.showMessageDialog(this, "Attendance marked successfully.");
            studentIdField.setText(""); // Clear the input field
            statusComboBox.setSelectedIndex(0); // Reset the status combo box
            dateChooser.setDate(null); // Reset the date chooser
        } else {
            JOptionPane.showMessageDialog(this, "Failed to mark attendance. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAttendance() {
        String studentId = studentIdField.getText().trim();

        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID to view attendance.");
            return;
        }

        // Create a new JFrame for viewing attendance records
        JFrame viewAttendanceFrame = new JFrame("View Attendance");
        viewAttendanceFrame.setSize(800, 600);
        viewAttendanceFrame.setLocationRelativeTo(this);
        viewAttendanceFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create the attendance panel and add it to the frame
        ViewAttendancePanel viewAttendancePanel = new ViewAttendancePanel(studentId, connection);
        viewAttendanceFrame.add(viewAttendancePanel);

        viewAttendanceFrame.setVisible(true); // Show the attendance frame
    }

    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        String teacherId = "teacher123"; // Replace with actual logic to get the teacher ID

        if (connection != null) {
            SwingUtilities.invokeLater(() -> new AttendanceManagement(connection, teacherId));
        } else {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
