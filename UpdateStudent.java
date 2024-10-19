package student.information.management.system;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateStudent extends JFrame implements ActionListener {
    private Connection connection;
    private JTextField studentIdField, nameField, fatherNameField, phoneField, emailField, addressField, bloodGroupField, departmentField;
    private JPasswordField passwordField;
    private JDateChooser dobChooser;
    private JComboBox<String> genderComboBox;
    private JButton updateButton, searchButton, cancelButton;

    public UpdateStudent(Connection connection) {
        this.connection = connection;
        setTitle("Update Student");
        setSize(500, 600);
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

        String[] labels = {
                "Student ID:", "Name:", "Father's Name:", "Date of Birth:", "Phone No:",
                "Email:", "Address:", "Gender:", "Blood Group:", "Department:", "Password:"
        };

        // Add labels
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            add(new JLabel(labels[i]), gbc);
        }

        // Initialize text fields and components
        studentIdField = new JTextField(20);
        nameField = new JTextField(20);
        fatherNameField = new JTextField(20);
        dobChooser = new JDateChooser();
        dobChooser.setDateFormatString("yyyy-MM-dd");
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressField = new JTextField(20);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        bloodGroupField = new JTextField(5); // Blood Group Field
        departmentField = new JTextField(20); // Department Field
        passwordField = new JPasswordField(20); // Password Field

        // Array of text fields for adding to the layout
        JTextField[] textFields = {
                studentIdField, nameField, fatherNameField, phoneField, emailField,
                addressField, bloodGroupField, departmentField
        };

        // Add text fields
        for (int i = 0; i < textFields.length; i++) {
            gbc.gridx = 1;
            gbc.gridy = i;
            add(textFields[i], gbc);
        }

        // Adding the JDateChooser and JComboBox for gender
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(dobChooser, gbc);
        gbc.gridy = 7;
        add(genderComboBox, gbc);

        // Add password field
        gbc.gridx = 1;
        gbc.gridy = 4; // Password field is at the 10th position
        add(phoneField, gbc);

        // Add password field
        gbc.gridx = 1;
        gbc.gridy = 5; // Password field is at the 10th position
        add(emailField, gbc);

        // Add password field
        gbc.gridx = 1;
        gbc.gridy = 6; // Password field is at the 10th position
        add(addressField, gbc);

        // Add password field
        gbc.gridx = 1;
        gbc.gridy = 8; // Password field is at the 10th position
        add(bloodGroupField, gbc);

        // Add password field
        gbc.gridx = 1;
        gbc.gridy = 9; // Password field is at the 10th position
        add(departmentField, gbc);

        // Add password field
        gbc.gridx = 1;
        gbc.gridy = 10; // Password field is at the 10th position
        add(passwordField, gbc);

        // Buttons for Search, Update, and Cancel
        gbc.gridx = 0;
        gbc.gridy = 11; // Search button position
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        add(searchButton, gbc);

        gbc.gridx = 1;
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        add(updateButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 12; // Cancel button position
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        add(cancelButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == searchButton) {
            searchStudent();
        } else if (ae.getSource() == updateButton) {
            updateStudent();
        } else if (ae.getSource() == cancelButton) {
            dispose();
        }
    }

    private void searchStudent() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "SELECT * FROM students WHERE student_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, studentId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    fatherNameField.setText(rs.getString("father_name"));
                    dobChooser.setDate(rs.getDate("dob"));
                    phoneField.setText(rs.getString("phone_no"));
                    emailField.setText(rs.getString("email"));
                    addressField.setText(rs.getString("address"));
                    genderComboBox.setSelectedItem(rs.getString("gender"));
                    bloodGroupField.setText(rs.getString("blood_group"));
                    departmentField.setText(rs.getString("department"));
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with that ID.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        String studentId = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String fatherName = fatherNameField.getText().trim();
        String dob = ((JTextField) dobChooser.getDateEditor().getUiComponent()).getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String bloodGroup = bloodGroupField.getText().trim();
        String department = departmentField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (studentId.isEmpty() || name.isEmpty() || fatherName.isEmpty() || dob.isEmpty() || phone.isEmpty() || email.isEmpty()
                || address.isEmpty() || bloodGroup.isEmpty() || department.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "UPDATE students SET name = ?, father_name = ?, dob = ?, address = ?, phone_no = ?, email = ?, gender = ?, blood_group = ?, department = ? WHERE student_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setString(2, fatherName);
                pstmt.setString(3, dob);
                pstmt.setString(4, address);
                pstmt.setString(5, phone);
                pstmt.setString(6, email);
                pstmt.setString(7, gender);
                pstmt.setString(8, bloodGroup);
                pstmt.setString(9, department);
                pstmt.setString(10, studentId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No student found with that ID.", "Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        return email.matches(emailRegex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateStudent(Conn.getInstance().getConnection()));
    }
}
