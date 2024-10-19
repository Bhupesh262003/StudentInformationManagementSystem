package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * TeacherDashboard provides the main interface for teachers to manage attendance, grades, and view student details.
 */
public class TeacherDashboard extends JFrame implements ActionListener {

  private JMenuBar menuBar;
  private JMenu manageAttendanceMenu, manageGradesMenu, viewDetailsMenu, logoutMenu;
  private JMenuItem viewAttendanceMenuItem, recordGradesMenuItem, viewStudentDetailsMenuItem, logoutMenuItem;
  private String teacherID;
  private Connection connection;

  /**
    * Constructs the TeacherDashboard.
    *
    * @param teacherID The ID of the logged-in teacher.
    * @param connection The database connection.
    */
          public TeacherDashboard(String teacherID, Connection connection) {
   this.teacherID = teacherID;
   this.connection = connection;

   // Setting up the frame
   setTitle("Teacher Dashboard - " + teacherID);
   setSize(1050, 700);
  setLayout(new BorderLayout());
   setLocationRelativeTo(null); // Center the window on screen

   // Create the menu bar
   setupMenuBar();

   // Welcome message
   JLabel welcomeLabel = new JLabel("Welcome, Teacher ID: " + teacherID, SwingConstants.CENTER);
   welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
   welcomeLabel.setForeground(new Color(0, 102, 204)); // Change font color
   add(welcomeLabel, BorderLayout.NORTH);

   // Main content area
   JPanel mainPanel = new JPanel();
   mainPanel.setLayout(new GridBagLayout());
   mainPanel.setBackground(new Color(220, 220, 255)); // Light blue background
   add(mainPanel, BorderLayout.CENTER);

   // Optional: Add a logo or image (if any)
  // JLabel logoLabel = new JLabel(new ImageIcon("path/to/logo.png"));
 // mainPanel.add(logoLabel, new GridBagConstraints());

   // Set the menu bar
   setJMenuBar(menuBar);
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   setVisible(true);
          }

 /**
  * Initializes and sets up the menu bar with all necessary menus and menu items.
  */
          private void setupMenuBar() {
   menuBar = new JMenuBar();

   // Manage Attendance menu
   manageAttendanceMenu = new JMenu("Manage Attendance");
  viewAttendanceMenuItem = new JMenuItem("View Attendance");
              viewAttendanceMenuItem.addActionListener(this);
   manageAttendanceMenu.add(viewAttendanceMenuItem);
   menuBar.add(manageAttendanceMenu);

   // Manage Grades menu
   manageGradesMenu = new JMenu("Manage Grades");
   recordGradesMenuItem = new JMenuItem("Record Grades");
   recordGradesMenuItem.addActionListener(this);
   manageGradesMenu.add(recordGradesMenuItem);
   menuBar.add(manageGradesMenu);

   // View Details menu
   viewDetailsMenu = new JMenu("View Details");
   viewStudentDetailsMenuItem = new JMenuItem("View Student Details");
   viewStudentDetailsMenuItem.addActionListener(this);
  viewDetailsMenu.add(viewStudentDetailsMenuItem);
  menuBar.add(viewDetailsMenu);

  // Logout menu
 logoutMenuItem = new JMenuItem("Logout");
  logoutMenuItem.addActionListener(this);
  menuBar.add(logoutMenuItem);
 }

 /**
 * Handles menu item actions based on user interactions.
 *
  * @param ae The ActionEvent triggered by user interaction.
  */

 @Override
 public void actionPerformed(ActionEvent ae) {
  String command = ae.getActionCommand();

  switch (command) {
case "View Attendance":
 handleViewAttendance();
 break;
case "Record Grades":
 handleRecordGrades();
 break;
case "View Student Details":
 handleViewStudentDetails();
  break;
 case "Logout":
 handleLogout();
  break;
 default:
  break;
   }
 }

 private void handleViewAttendance() {
   String attendanceStudentId = JOptionPane.showInputDialog(this, "Enter Student ID to view attendance:");
   if (attendanceStudentId != null && !attendanceStudentId.trim().isEmpty()) {
 new ViewAttendancePanel(attendanceStudentId, connection);
   } else {
 JOptionPane.showMessageDialog(this, "Student ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
   }
 }

  private void handleRecordGrades() {
   String gradesStudentId = JOptionPane.showInputDialog(this, "Enter Student ID to record grades:");
   if (gradesStudentId != null && !gradesStudentId.trim().isEmpty()) {
 new RecordGrades(gradesStudentId, connection); // Updated constructor call
   } else {
 JOptionPane.showMessageDialog(this, "Student ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
   }
  }

 private void handleViewStudentDetails() {
   String detailsStudentId = JOptionPane.showInputDialog(this, "Enter Student ID to view details:");
   if (detailsStudentId != null && !detailsStudentId.trim().isEmpty()) {
 new ViewStudentDetails(detailsStudentId, connection); // Updated constructor call
   } else {
 JOptionPane.showMessageDialog(this, "Student ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
   }
         }

  private void handleLogout() {
  int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
   if (confirmed == JOptionPane.YES_OPTION) {
 dispose(); // Close the dashboard
 // Closing the connection before logging out
 try {
  if (connection != null && !connection.isClosed()) {
   connection.close();
  }
 } catch (Exception e) {
                e.printStackTrace();
            }
            new Login(); // Redirect to the login page
        }
    }

    /**
     * Main method for testing purposes. Initializes the TeacherDashboard with a sample teacher ID.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        if (connection != null) {
            SwingUtilities.invokeLater(() -> new TeacherDashboard("T001", connection)); // Example teacher ID
        } else {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}