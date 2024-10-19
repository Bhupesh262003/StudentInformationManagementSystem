package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherLogin extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn, cancelBtn;

    TeacherLogin() {
        // Set frame properties
        setTitle("Teacher Login");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add Username label and field
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(50, 50, 150, 30);
        add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 30);
        add(usernameField);

        // Add Password label and field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 100, 150, 30);
        add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 30);
        add(passwordField);

        // Add Login button
        loginBtn = new JButton("Login");
        loginBtn.setBounds(80, 180, 100, 30);
        loginBtn.addActionListener(this);
        add(loginBtn);

        // Add Cancel button
        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(220, 180, 100, 30);
        cancelBtn.addActionListener(e -> dispose()); // Close the login window
        add(cancelBtn);

        // Set frame visibility
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
                 PreparedStatement pstmt = con.prepareStatement("SELECT * FROM teacher WHERE teacherID = ? AND password = ?")) {

                pstmt.setString(1, username);
                pstmt.setString(2, password);

                // Execute the query and get results
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Redirect to Teacher Dashboard (implement this)
                    new TeacherDashboard(rs.getString("id"), con).setVisible(true); // Pass connection here
                    dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid login credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TeacherLogin::new); // Ensure thread safety
    }
}
