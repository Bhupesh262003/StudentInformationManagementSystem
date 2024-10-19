package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JComboBox<String> roleComboBox;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login - Student Information Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel headerLabel = new JLabel("Student Information Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2), "User Login",
                0, 0, new Font("Arial", Font.BOLD, 16), new Color(0, 102, 204))
        );
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role selection
        roleComboBox = new JComboBox<>(new String[]{"Select Role", "Student", "Admin", "Teacher"});
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(roleComboBox, gbc);

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setToolTipText("Enter your username");
        mainPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setToolTipText("Enter your password");
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        // Customizing buttons
        loginButton.setBackground(new Color(0, 153, 76)); // Green color
        loginButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(204, 0, 0)); // Red color
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        // Add mainPanel and buttonPanel to the frame
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        JLabel footerLabel = new JLabel("© 2024 SIMS - All Rights Reserved", JLabel.CENTER);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.PAGE_END);

        // Action listener for Login Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Action listener for Cancel Button
        cancelButton.addActionListener(e -> clearFields());

        // Center the frame on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin() {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        String username = usernameField.getText().trim();

        String password = new String(passwordField.getPassword()).trim();

        // Validate input
        if ("Select Role".equals(selectedRole)) {
            JOptionPane.showMessageDialog(this, "Please select a role.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call login logic based on role
        boolean loginSuccess = false;
        switch (selectedRole) {
            case "Student":
                loginSuccess = handleStudentLogin(username, password);
                break;
            case "Admin":
                loginSuccess = handleAdminLogin(username, password);
                break;
            case "Teacher":
                loginSuccess = handleTeacherLogin(username, password);
                break;
        }

        // Handle login success or failure
        if (loginSuccess) {
            handleSuccessfulLogin(selectedRole, username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSuccessfulLogin(String role, String username) {
        // Obtain a Connection instance
        Connection connection = Conn.getInstance().getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Failed to establish a database connection.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Redirect to the appropriate dashboard
        switch (role) {
            case "Student":
                new StudentDashboard(username, connection); // Ensure this constructor exists
                break;
            case "Admin":
                new AdminDashboard(connection);
                break;
            case "Teacher":
                new TeacherDashboard(username, connection);
                break;
        }
        dispose(); // Close the login window
    }

    private boolean handleStudentLogin(String username, String password) {
        try {
            Connection connection = Conn.getInstance().getConnection();
            System.out.println(username);

            // Update the query to include salt and hashedpassword
            String query = "SELECT student_id, salt, hashedpassword FROM students WHERE student_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Now you can retrieve salt and hashedpassword safely
                String salt = rs.getString("salt"); // This will now work
                String hashedPassword = PasswordUtil.hashPasswordWithSalt(password, salt);
                String studentId = rs.getString("student_id");
                System.out.println("Student ID: " + studentId);
                String dbPass = rs.getString("hashedpassword");
                System.out.println("DBPASS: " + dbPass);
                System.out.println("HASHPASS: " + hashedPassword);

                // Verify the hashed password
                return hashedPassword.equals(dbPass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean handleAdminLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }

    private boolean handleTeacherLogin(String username, String password) {
        return username.equals("teacher") && password.equals("teacher123");
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        JOptionPane.showMessageDialog(this, "Input cleared. You can start over.", "Input Cleared", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}

