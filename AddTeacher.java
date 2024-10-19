package student.information.management.system;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddTeacher extends JFrame implements ActionListener {
    JTextField teacherIdField, nameField, fatherNameField, phoneField, emailField, addressField, departmentField, bloodGroupField, subjectField;
    JPasswordField passwordField;
    JDateChooser dobChooser;
    JComboBox<String> genderComboBox;
    JButton addButton, cancelButton;

    public AddTeacher(Connection connection) {
        setTitle("Add Teacher");

        // Initialize input fields
        JLabel lblTeacherId = new JLabel("Teacher ID:");
        teacherIdField = new JTextField(15);

        JLabel lblName = new JLabel("Name:");
        nameField = new JTextField(15);

        JLabel lblFatherName = new JLabel("Father's Name:");
        fatherNameField = new JTextField(15);

        JLabel lblDob = new JLabel("Date of Birth:");
        dobChooser = new JDateChooser();
        dobChooser.setDateFormatString("yyyy-MM-dd");

        JLabel lblPhone = new JLabel("Phone No:");
        phoneField = new JTextField(15);

        JLabel lblEmail = new JLabel("Email:");
        emailField = new JTextField(15);

        JLabel lblAddress = new JLabel("Address:");
        addressField = new JTextField(15);

        JLabel lblGender = new JLabel("Gender:");
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);

        JLabel lblBloodGroup = new JLabel("Blood Group:");
        bloodGroupField = new JTextField(5);

        JLabel lblDepartment = new JLabel("Department:");
        departmentField = new JTextField(15);

        JLabel lblSubject = new JLabel("Subject:");
        subjectField = new JTextField(15);  // New subject field

        JLabel lblPassword = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        // Buttons
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");

        // Adding Action Listeners
        addButton.addActionListener(e -> addTeacher(connection));
        cancelButton.addActionListener(e -> this.dispose());

        // Adding components to the frame
        setLayout(new GridLayout(14, 2, 10, 10));
        add(lblTeacherId);
        add(teacherIdField);
        add(lblName);
        add(nameField);
        add(lblFatherName);
        add(fatherNameField);
        add(lblDob);
        add(dobChooser);
        add(lblPhone);
        add(phoneField);
        add(lblEmail);
        add(emailField);
        add(lblAddress);
        add(addressField);
        add(lblGender);
        add(genderComboBox);
        add(lblBloodGroup);
        add(bloodGroupField);
        add(lblDepartment);
        add(departmentField);
        add(lblSubject);  // Add subject label
        add(subjectField);  // Add subject field
        add(lblPassword);
        add(passwordField);
        add(addButton);
        add(cancelButton);

        setSize(400, 550);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addTeacher(Connection connection) {
        String teacherId = teacherIdField.getText();
        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String dob = ((JTextField) dobChooser.getDateEditor().getUiComponent()).getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String bloodGroup = bloodGroupField.getText();
        String department = departmentField.getText();
        String subject = subjectField.getText();  // Get the subject
        String password = new String(passwordField.getPassword());

        try {
            // Generate a random salt
            String salt = PasswordUtil.generateSalt();

            // Hash the password with the salt
            String hashedPassword = PasswordUtil.hashPasswordWithSalt(password, salt);

            // Insert teacher data
            String query = "INSERT INTO teachers (teacher_id, teacher_name, father_name, dob, address, phone_no, email, gender, blood_group, department, subject, salt, password_hash) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, teacherId);
                pstmt.setString(2, name);
                pstmt.setString(3, fatherName);
                pstmt.setString(4, dob);
                pstmt.setString(5, address);
                pstmt.setString(6, phone);
                pstmt.setString(7, email);
                pstmt.setString(8, gender);
                pstmt.setString(9, bloodGroup);
                pstmt.setString(10, department);
                pstmt.setString(11, subject);  // Set the subject
                pstmt.setString(12, salt);
                pstmt.setString(13, hashedPassword);

                pstmt.executeUpdate();
            }

            // Insert into log table
            String loginQuery = "INSERT INTO log (username, password_hash, salt, role) VALUES (?, ?, ?, 'teacher')";
            try (PreparedStatement loginPstmt = connection.prepareStatement(loginQuery)) {
                loginPstmt.setString(1, teacherId);
                loginPstmt.setString(2, hashedPassword);
                loginPstmt.setString(3, salt);

                loginPstmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Teacher added successfully!");
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Conn conn = Conn.getInstance();
        Connection connection = conn.getConnection();
        new AddTeacher(connection);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
