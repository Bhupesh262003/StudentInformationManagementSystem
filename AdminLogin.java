package student.information.management.system;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLogin extends JFrame implements ActionListener {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn;

    AdminLogin() {
        setTitle("Admin Login");
        setSize(400, 300);
        setLocation(400, 150);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on exit

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
            JOptionPane.showMessageDialog(null, "Username and Password cannot be empty.");
            return;
        }

        // Using try-with-resources for Connection and PreparedStatement
        try {
            Conn c = Conn.getInstance(); // Use singleton instance
            try (Connection con = c.getConnection();
                 PreparedStatement pstmt = con.prepareStatement("SELECT * FROM admin WHERE username = ? AND password = ?")) {

                pstmt.setString(1, username);
                pstmt.setString(2, password); // Hash the password here if needed

                // Execute the query and get results
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login successful");
                        // Redirect to admin dashboard (implement this)
                        // e.g., new AdminDashboard().setVisible(true);
                        dispose(); // Close the login window
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid login");
                    }
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AdminLogin();
    }
}
