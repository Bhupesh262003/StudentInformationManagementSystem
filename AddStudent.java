package student.information.management.system;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddStudent extends JFrame implements ActionListener {
    private Connection connection;
    private JTextField studentIdField, nameField, fatherNameField, phoneField, emailField, addressField, bloodGroupField, departmentField;
    private JPasswordField passwordField;
    private JDateChooser dobChooser;
    private JComboBox<String> genderComboBox;
    private JButton addButton, cancelButton;
    private String hashedPassword;

    public AddStudent(Connection connection) {
        this.connection = connection;
        setTitle("Add Student");
        setSize(400, 600);
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
                "Student ID:", "Name:", "Father's Name:", "Date of Birth:",
                "Phone No:", "Email:", "Address:", "Gender:",
                "Blood Group:", "Department:", "Password:"
        };

        // Add labels to the form
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.LINE_END;
            add(new JLabel(labels[i]), gbc);
        }

        // Text fields
        studentIdField = new JTextField(20);
        nameField = new JTextField(20);
        fatherNameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressField = new JTextField(20);
        bloodGroupField = new JTextField(20);
        departmentField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Date picker for DOB
        dobChooser = new JDateChooser();
        dobChooser.setDateFormatString("yyyy-MM-dd");

        // Combo box for gender
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        // Add input fields to the form
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(studentIdField, gbc);
        gbc.gridy = 1;
        add(nameField, gbc);
        gbc.gridy = 2;
        add(fatherNameField, gbc);
        gbc.gridy = 3;
        add(dobChooser, gbc);
        gbc.gridy = 4;
        add(phoneField, gbc);
        gbc.gridy = 5;
        add(emailField, gbc);
        gbc.gridy = 6;
        add(addressField, gbc);
        gbc.gridy = 7;
        add(genderComboBox, gbc);
        gbc.gridy = 8;
        add(bloodGroupField, gbc);
        gbc.gridy = 9;
        add(departmentField, gbc);
        gbc.gridy = 10;
        add(passwordField, gbc);

        // Add buttons
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addButton = new JButton("Add Student");
        cancelButton = new JButton("Cancel");
        add(addButton, gbc);
        gbc.gridy = 12;
        add(cancelButton, gbc);

        addButton.addActionListener(this);
        cancelButton.addActionListener(e -> dispose());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String studentId = studentIdField.getText().trim();
            String name = nameField.getText().trim();
            String fatherName = fatherNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String bloodGroup = bloodGroupField.getText().trim();
            String department = departmentField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();
            java.util.Date dob = dobChooser.getDate();
            String password = new String(passwordField.getPassword()).trim();

            // Validation
            if (studentId.isEmpty() || name.isEmpty() || fatherName.isEmpty() || phone.isEmpty() ||
                    email.isEmpty() || address.isEmpty() || bloodGroup.isEmpty() || department.isEmpty() ||
                    gender.isEmpty() || dob == null || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Password hashing
            String salt = PasswordUtil.generateSalt(); // Ensure this method exists in your PasswordUtil
            hashedPassword = PasswordUtil.hashPasswordWithSalt(password, salt); // Use the class variable

            String sql = "INSERT INTO students (student_id, student_name, father_name, dob, phone_no, email, address, gender, " +
                    "blood_group, department, hashedpassword, salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, studentId);
            ps.setString(2, name);
            ps.setString(3, fatherName);
            ps.setDate(4, new java.sql.Date(dob.getTime()));
            ps.setString(5, phone);
            ps.setString(6, email);
            ps.setString(7, address);
            ps.setString(8, gender);
            ps.setString(9, bloodGroup);
            ps.setString(10, department);
            ps.setString(11, hashedPassword); // Use the class variable
            ps.setString(12, salt); // Use the newly created salt

            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        nameField.setText("");
        fatherNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        bloodGroupField.setText("");
        departmentField.setText("");
        passwordField.setText("");
        dobChooser.setDate(null);
        genderComboBox.setSelectedIndex(0);
    }
}

