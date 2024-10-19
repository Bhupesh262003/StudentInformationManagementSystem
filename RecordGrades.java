package student.information.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecordGrades extends JFrame implements ActionListener {
    private final String studentId;
    private JTable gradesTable;
    private JButton recordGradeButton;
    private Connection connection;

    public RecordGrades(String studentId, Connection connection) {
        this.studentId = studentId;
        this.connection = connection;

        setTitle("Record Grades");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Record Grades", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table to display courses
        gradesTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Record Grade Button
        recordGradeButton = new JButton("Record Grade");
        recordGradeButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(recordGradeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Fetch and display courses
        loadCourses();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadCourses() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Course ID");
        model.addColumn("Course Name");

        // Query to fetch courses that the student is enrolled in
        String courseQuery = "SELECT c.course_id, c.course_name " +
                "FROM courses c " +
                "JOIN enrollment e ON c.course_id = e.course_id " +
                "WHERE e.student_id = ?";

        try (PreparedStatement courseStmt = connection.prepareStatement(courseQuery)) {
            courseStmt.setString(1, studentId);
            ResultSet courseRs = courseStmt.executeQuery();

            while (courseRs.next()) {
                String courseId = courseRs.getString("course_id");
                String courseName = courseRs.getString("course_name");
                model.addRow(new Object[]{courseId, courseName});
            }

            gradesTable.setModel(model);

        } catch (SQLException e) {
            showError("Error loading courses: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == recordGradeButton) {
            recordGrade();
        }
    }

    private void recordGrade() {
        int selectedRow = gradesTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a course to record the grade.");
            return;
        }

        String courseId = (String) gradesTable.getValueAt(selectedRow, 0);
        String grade = JOptionPane.showInputDialog(this, "Enter Grade (A, B, C, D, F):");
        if (isInputInvalid(grade)) return;

        try {
            // Check if a grade already exists for this student and course
            String existingGradeQuery = "SELECT grade FROM grades WHERE student_id = ? AND course_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(existingGradeQuery)) {
                checkStmt.setString(1, studentId);
                checkStmt.setString(2, courseId);
                ResultSet existingGradeRs = checkStmt.executeQuery();

                if (existingGradeRs.next()) {
                    // Update existing grade
                    String updateQuery = "UPDATE grades SET grade = ? WHERE student_id = ? AND course_id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, grade);
                        updateStmt.setString(2, studentId);
                        updateStmt.setString(3, courseId);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Grade updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // Insert new grade
                    String insertQuery = "INSERT INTO grades (student_id, course_id, grade) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, studentId);
                        insertStmt.setString(2, courseId);
                        insertStmt.setString(3, grade);
                        insertStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Grade recorded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

        } catch (SQLException e) {
            showError("Error recording grade: " + e.getMessage());
        }
    }

    private boolean isInputInvalid(String input) {
        if (input == null || input.trim().isEmpty()) {
            showError("Input cannot be empty.");
            return true;
        }
        // Additional input validation can be added here if needed (e.g., valid grades)
        return false;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Conn connInstance = Conn.getInstance();
            Connection connection = connInstance.getConnection();
            if (connection != null) {
                new RecordGrades("S001", connection); // Replace with actual student ID
            } else {
                JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
