package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class GenerateReports extends JFrame implements ActionListener {

    private JButton studentReportButton, attendanceReportButton, feeReportButton;
    private Connection connection; // Connection to the database

    public GenerateReports(Connection connection) {
        this.connection = connection; // Initialize the connection

        // Set JFrame properties
        setTitle("Generate Reports");
        setSize(400, 300);
        setLocation(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(245, 245, 245));

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        buttonPanel.setBackground(new Color(255, 255, 255)); // Panel background color

        // Initialize buttons
        studentReportButton = new JButton("Generate Student Report");
        attendanceReportButton = new JButton("Generate Attendance Report");
        feeReportButton = new JButton("Generate Fee Report");

        // Set button font and style
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        studentReportButton.setFont(buttonFont);
        attendanceReportButton.setFont(buttonFont);
        feeReportButton.setFont(buttonFont);

        // Add action listeners
        studentReportButton.addActionListener(this);
        attendanceReportButton.addActionListener(this);
        feeReportButton.addActionListener(this);

        // Add buttons to the panel
        buttonPanel.add(studentReportButton);
        buttonPanel.add(attendanceReportButton);
        buttonPanel.add(feeReportButton);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        switch (command) {
            case "Generate Student Report":
                new StudentReportGenerator(connection).generateStudentReport();
                break;
            case "Generate Attendance Report":
                new AttendanceReportGenerator(connection).generateAttendanceReport();
                break;
            case "Generate Fee Report":
                // Prompt user for student ID
                String studentID = JOptionPane.showInputDialog(this, "Enter Student ID:", "Generate Fee Report", JOptionPane.PLAIN_MESSAGE);
                if (studentID != null && !studentID.trim().isEmpty()) {
                    new FeeReportGenerator(connection).generateFeeReport(studentID);
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        new GenerateReports(connection);
    }
}
