package student.information.management.system;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentLogin extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn;

    StudentLogin() {
        setTitle("Student Login");
        setSize(400, 300);
        setLocation(400, 150);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on exit

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 50, 150, 30);
        add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 30);
        add(usernameField);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 100, 150, 30);
        add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 30);
        add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 150, 100, 30);
        loginBtn.addActionListener(this);
        add(loginBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Conn c = Conn.getInstance(); // Use singleton instance

            // Use try-with-resources to manage database resources
            try (Connection con = c.getConnection();
                 PreparedStatement pstmt = con.prepareStatement("SELECT * FROM student WHERE username = ? AND password = ?")) {

                pstmt.setString(1, username);
                pstmt.setString(2, password); // Hash password before checking

                // Execute the query and get results
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Redirect to teacher dashboard (implement this)
                    // e.g., new TeacherDashboard().setVisible(true);
                    dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid login", "Login Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentLogin()); // Ensure thread safety
    }
}
