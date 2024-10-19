package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AdminDashboard extends JFrame implements ActionListener {

    private JMenuBar mb;
    private JMenu userManagement;
    private JMenu reports;
    private JMenu feeManagement;
    private JMenuItem manageTeachersMenuItem, manageStudentsMenuItem, generateReportsMenuItem, viewFeeMenuItem;
    private Connection connection; // Connection to the database

    public AdminDashboard(Connection connection) {
        this.connection = connection; // Initialize the connection
        setTitle("Admin Dashboard");
        setSize(1053, 700);
        setLocation(150, 25);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load the background image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/icons1.jpg"));
            if (icon != null) {
                JLabel iconLabel = new JLabel(icon);
                add(iconLabel, BorderLayout.CENTER);
            } else {
                System.out.println("Image not found, loading default interface.");
            }
        } catch (Exception e) {
            System.out.println("Error loading image");
            e.printStackTrace();
        }

        // Menu Bar
        mb = new JMenuBar();
        setJMenuBar(mb);

        // User Management Menu
        userManagement = new JMenu("User Management");
        userManagement.setFont(new Font("Serif", Font.PLAIN, 15));
        mb.add(userManagement);

        // Add menu items to User Management
        manageTeachersMenuItem = new JMenuItem("Manage Teachers");
        manageTeachersMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        manageTeachersMenuItem.addActionListener(this);
        userManagement.add(manageTeachersMenuItem);

        manageStudentsMenuItem = new JMenuItem("Manage Students");
        manageStudentsMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        manageStudentsMenuItem.addActionListener(this);
        userManagement.add(manageStudentsMenuItem);

        // Reports Menu
        reports = new JMenu("Reports");
        reports.setFont(new Font("Serif", Font.PLAIN, 15));
        mb.add(reports);

        generateReportsMenuItem = new JMenuItem("Generate Reports");
        generateReportsMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        generateReportsMenuItem.addActionListener(this);
        reports.add(generateReportsMenuItem);

        // Fee Management Menu
        feeManagement = new JMenu("Fee Management");
        feeManagement.setFont(new Font("Serif", Font.PLAIN, 15));
        mb.add(feeManagement);

        viewFeeMenuItem = new JMenuItem("View Fee");
        viewFeeMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        viewFeeMenuItem.addActionListener(this);
        feeManagement.add(viewFeeMenuItem);

        // Make frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        switch (command) {
            case "Manage Teachers":
                new ManageTeachers(connection);
                break;
            case "Manage Students":
                new ManageStudents(connection);
                break;
            case "Generate Reports":
                new GenerateReports(connection);
                break;
            case "View Fee":
                String studentId = JOptionPane.showInputDialog(this, "Enter Student ID to view fee details:");
                if (studentId != null && !studentId.trim().isEmpty()) {
                    new ViewFee(studentId, connection);
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID cannot be empty.");
                }
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        if (connection != null) {
            new AdminDashboard(connection);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
