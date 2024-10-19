package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * StudentDashboard provides the main interface for students to view personal details, attendance, and provide feedback.
 */
public class StudentDashboard extends JFrame {
    private String studentId; // student_id
    private Student student; // Student object
    private Connection connection;

    // Panels for different functionalities
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Constants for card names
    private static final String PERSONAL_DETAILS = "Personal Details";
    private static final String ATTENDANCE = "Attendance";
    private static final String FEEDBACK = "Feedback";

    public StudentDashboard(String studentId, Connection connection) {
        this.studentId = studentId;
        this.connection = connection;
        this.student = fetchStudentDetails(studentId); // Fetch student details

        if (this.student == null) {
            // If student details are not found, redirect to Login
            showErrorAndRedirect("Student details not found.");
            return;
        }

        setupFrame();
        setupTopPanel();
        setupSidePanel();
        setupMainPanel();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Student Dashboard - " + this.student.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void setupTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + this.student.getName() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        JButton logoutButton = createLogoutButton();
        topPanel.add(logoutButton, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
    }

    private JButton createLogoutButton() {
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(204, 0, 0)); // Red color
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new Login(); // Redirect to Login screen
                dispose(); // Close the dashboard
            }
        });
        return logoutButton;
    }

    private void setupSidePanel() {
        JPanel sidePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton personalDetailsButton = createSideButton("View Personal Details");
        JButton attendanceButton = createSideButton("View Attendance");
        JButton feedbackButton = createSideButton("Feedback");

        sidePanel.add(personalDetailsButton);
        sidePanel.add(attendanceButton);
        sidePanel.add(feedbackButton);
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(sidePanel, BorderLayout.WEST);

        // Action Listeners for navigation buttons
        personalDetailsButton.addActionListener(e -> cardLayout.show(mainPanel, PERSONAL_DETAILS));
        attendanceButton.addActionListener(e -> cardLayout.show(mainPanel, ATTENDANCE));
        feedbackButton.addActionListener(e -> cardLayout.show(mainPanel, FEEDBACK));
    }

    private JButton createSideButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 102, 204)); // Blue color
        button.setForeground(Color.WHITE);
        return button;
    }

    private void setupMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new ViewPersonalDetailsPanel(
                student.getStudentId(),
                student.getName(),
                student.getFatherName(),
                student.getDob(),
                student.getAddress(),
                student.getPhone(),
                student.getEmail(),
                student.getGender(),
                connection // Pass the connection here
        ), PERSONAL_DETAILS);

        mainPanel.add(new ViewAttendancePanel(student.getStudentId(), connection), ATTENDANCE); // Pass connection here
        mainPanel.add(new FeedbackPanel(studentId, connection), FEEDBACK); // Ensure FeedbackPanel is correctly implemented

        add(mainPanel, BorderLayout.CENTER);

        // Initially show Personal Details
        cardLayout.show(mainPanel, PERSONAL_DETAILS);
    }

    /**
     * Fetches the student's details based on the student ID.
     *
     * @param studentId The ID of the student.
     * @return A Student object containing the student's details, or null if not found.
     */
    private Student fetchStudentDetails(String studentId) {
        String query = "SELECT * FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Student found: " + studentId); // Debugging statement
                return new Student(
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("father_name"),
                        rs.getDate("dob").toString(),
                        rs.getString("address"),
                        rs.getString("phone_no"), // Ensure column name matches your database
                        rs.getString("email"),
                        rs.getString("gender")
                );
            } else {
                System.out.println("No student found with ID: " + studentId); // Debugging statement
            }
        } catch (SQLException e) {
            showError("Error fetching student details: " + e.getMessage());
        }
        return null; // Return null if student not found
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showErrorAndRedirect(String message) {
        showError(message);
        new Login();
        dispose();
    }

    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        if (connection != null) {
            SwingUtilities.invokeLater(() -> new StudentDashboard("S001", connection)); // Example student ID
        } else {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
