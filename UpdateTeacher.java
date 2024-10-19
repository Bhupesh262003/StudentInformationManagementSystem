package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateTeacher extends JFrame implements ActionListener {
    private JTextField teacherIdField, nameField;
    private JComboBox<String> subjectField;
    private JButton updateButton;
    private Connection connection;

    public UpdateTeacher(Connection connection) {
        this.connection = connection; // Store the connection

        // Frame properties
        setTitle("Update Teacher");
        setSize(400, 400);
        setLocation(300, 150);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components

        // Initialize components
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Teacher ID:"), gbc);

        gbc.gridx = 1;
        teacherIdField = new JTextField(15);
        add(teacherIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Subject:"), gbc);

        gbc.gridx = 1;
        String[] subjects = {"DSA", "PCOM", "MATHS-3", "DBMS", "PCPF", "JAVA", "GRAPHICS", "DEVOPS"};
        subjectField = new JComboBox<>(subjects);
        add(subjectField, gbc);

        // Initialize update button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; // Span across both columns
        updateButton = new JButton("Update Teacher");
        updateButton.addActionListener(this);
        add(updateButton, gbc);

        // Frame settings
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateButton) {
            String teacherId = teacherIdField.getText().trim();
            String name = nameField.getText().trim();
            String subject = (String) subjectField.getSelectedItem();

            // Validate inputs
            if (teacherId.isEmpty() || name.isEmpty() || subject == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the teacher details
            try (PreparedStatement ps = connection.prepareStatement("UPDATE teachers SET name = ?, subject = ? WHERE teacher_id = ?")) {
                ps.setString(1, name);
                ps.setString(2, subject);
                ps.setString(3, teacherId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Clear input fields after successful update
                    teacherIdField.setText("");
                    nameField.setText("");
                    subjectField.setSelectedIndex(0);

                    // Show success message
                    JOptionPane.showMessageDialog(this, "Teacher Updated Successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "Teacher Not Found", "Update Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        Conn conn = Conn.getInstance();
        Connection connection = conn.getConnection();
        new UpdateTeacher(connection);
    }
}
