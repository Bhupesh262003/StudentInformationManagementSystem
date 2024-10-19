package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DeleteStudent extends JFrame implements ActionListener {
    private Connection connection;
    private JTextField tfStudentID;
    private JButton deleteBtn, cancelBtn;

    public DeleteStudent(Connection connection) {
        this.connection = connection;
        setTitle("Delete Student");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupForm();
        setVisible(true);
    }

    private void setupForm() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel heading = new JLabel("DELETE STUDENT", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(heading, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        tfStudentID = new JTextField(20);
        add(tfStudentID, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(this);
        add(deleteBtn, gbc);

        gbc.gridx = 1;
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        add(cancelBtn, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteBtn) {
            deleteStudent();
        } else if (e.getSource() == cancelBtn) {
            dispose();
        }
    }

    private void deleteStudent() {
        String studentId = tfStudentID.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = Conn.getInstance().getConnection()) {
            String query = "DELETE FROM students WHERE student_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, studentId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with that ID.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteStudent(Conn.getInstance().getConnection()));
    }
}
