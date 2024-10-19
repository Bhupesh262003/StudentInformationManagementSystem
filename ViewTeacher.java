package student.information.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewTeacher extends JFrame {
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private Connection connection;

    public ViewTeacher(Connection connection) {
        this.connection = connection; // Store the connection
        // Frame settings
        setTitle("View Teachers");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table setup
        teacherTable = new JTable();
        tableModel = new DefaultTableModel();

        // Adding columns to the table
        tableModel.addColumn("Teacher ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone No");
        tableModel.addColumn("DOB");
        tableModel.addColumn("Department");
        tableModel.addColumn("Subject");
        tableModel.addColumn("Father's Name");

        // Set model to table
        teacherTable.setModel(tableModel);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load data from database
        loadTeacherData();

        setVisible(true);
    }

    // Method to load teacher data from database
    private void loadTeacherData() {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // Ensure the connection is not null
            if (connection != null) {
                // Create a statement
                stmt = connection.createStatement();
                // Updated SQL query to fetch the necessary fields
                String sql = "SELECT teacher_id, teacher_name, email, phone_no, dob, department, subject, father_name FROM teachers";
                rs = stmt.executeQuery(sql);

                // Add rows to the table
                while (rs.next()) {
                    Object[] row = new Object[8]; // Adjust size for the columns
                    row[0] = rs.getString("teacher_id");
                    row[1] = rs.getString("teacher_name");
                    row[2] = rs.getString("email");
                    row[3] = rs.getString("phone_no");
                    row[4] = rs.getDate("dob");
                    row[5] = rs.getString("department");
                    row[6] = rs.getString("subject");  // Fetch subject
                    row[7] = rs.getString("father_name");  // Fetch father_name

                    tableModel.addRow(row);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading teacher data.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Conn conn = Conn.getInstance();
        Connection connection = conn.getConnection();
        new ViewTeacher(connection);
    }
}
