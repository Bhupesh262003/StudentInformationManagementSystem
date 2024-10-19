package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.String;
import java.sql.Connection;

public class Dashboard extends JFrame implements ActionListener {

    private JMenuBar mb;
    private JMenu fileMenu, detailsMenu;
    private JMenuItem feeMenuItem, attendanceMenuItem, educationalMenuItem, personalMenuItem, feedbackMenuItem, reportMenuItem;
    private JLabel background;

    public Dashboard(Connection connection) {
        // Set window size and title
        setTitle("Dashboard - Student Information Management System");
        setSize(1053, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Changed to BorderLayout for better layout management

        // Menu bar
        mb = new JMenuBar();
        setJMenuBar(mb);

        // File and Details menus
        fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Serif", Font.PLAIN, 15));

        detailsMenu = new JMenu("Details");
        detailsMenu.setFont(new Font("Serif", Font.PLAIN, 15));

        mb.add(fileMenu);
        mb.add(detailsMenu);

        // Add menu items
        feeMenuItem = new JMenuItem("Fee Management");
        feeMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        feeMenuItem.addActionListener(this);
        detailsMenu.add(feeMenuItem);

        attendanceMenuItem = new JMenuItem("Attendance Management");
        attendanceMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        attendanceMenuItem.addActionListener(this);
        detailsMenu.add(attendanceMenuItem);

        educationalMenuItem = new JMenuItem("Educational Details");
        educationalMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        educationalMenuItem.addActionListener(this);
        detailsMenu.add(educationalMenuItem);

        personalMenuItem = new JMenuItem("Personal Details");
        personalMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        personalMenuItem.addActionListener(this);
        detailsMenu.add(personalMenuItem);

        feedbackMenuItem = new JMenuItem("Feedback");
        feedbackMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        feedbackMenuItem.addActionListener(this);
        detailsMenu.add(feedbackMenuItem);

        reportMenuItem = new JMenuItem("Report Generation");
        reportMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        reportMenuItem.addActionListener(this);
        detailsMenu.add(reportMenuItem);

        // Background Image (Optional)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/background.jpg")); // Ensure the path is correct
            if (icon != null) {
                background = new JLabel(icon);
                add(background, BorderLayout.CENTER);
            } else {
                System.out.println("Background image not found.");
            }
        } catch (Exception e) {
            System.out.println("Error loading background image.");
            e.printStackTrace();
        }

        // Make the frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        Connection connection = Conn.getInstance().getConnection(); // Obtain the connection

        switch (command) {
            case "Fee Management":
                String feeStudentId = JOptionPane.showInputDialog(this, "Enter Student ID to manage fee details:");
                if (feeStudentId != null && !feeStudentId.trim().isEmpty()) {
                    new ViewFee(feeStudentId, connection);
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID cannot be empty.");
                }
                break;
            case "Attendance Management":
                String teacherId = "teacher123";
                new AttendanceManagement(connection,teacherId); // Updated to accept Connection
                break;
            case "Educational Details":
                String eduStudentId = JOptionPane.showInputDialog(this, "Enter Student ID to manage educational details:");
                if (eduStudentId != null && !eduStudentId.trim().isEmpty()) {
                    new EducationDetails(eduStudentId, connection); // Updated to accept Connection
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID cannot be empty.");
                }
                break;
            case "Personal Details":
                String personalStudentId = JOptionPane.showInputDialog(this, "Enter Student ID to manage personal details:");
                if (personalStudentId != null && !personalStudentId.trim().isEmpty()) {
                    new PersonalDetails(personalStudentId, connection); // Assuming PersonalDetails accepts Connection
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID cannot be empty.");
                }
                break;
            case "Feedback":
                String feedbackStudentId = JOptionPane.showInputDialog(this, "Enter Student ID to provide feedback:");
                if (feedbackStudentId != null && !feedbackStudentId.trim().isEmpty()) {
                    new FeedbackManagement(feedbackStudentId, connection); // Assuming FeedbackManagement accepts Connection
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID cannot be empty.");
                }
                break;
            case "Report Generation":
                new ReportGenerator(connection); // Assuming ReportGenerator accepts Connection
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        if (connection != null) {
            new Dashboard(connection);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
