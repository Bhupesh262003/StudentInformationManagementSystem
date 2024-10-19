package student.information.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewStudents extends JFrame {
    private Connection connection;
    private JTable table;

    public ViewStudents(Connection connection) {
        this.connection = connection;
        setTitle("View Students");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupTable();
        setVisible(true);
    }

    private void setupTable() {
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        loadStudents();
    }

    private void loadStudents() {
        String query = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel model = new DefaultTableModel(new String[]{"Student ID", "Name", "Father's Name", "DOB", "Phone No", "Email", "Address", "Gender", "Blood Group", "Department"}, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("father_name"),
                        rs.getDate("dob"),
                        rs.getString("phone_no"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getString("blood_group"),
                        rs.getString("department")
                });
            }
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewStudents(Conn.getInstance().getConnection()));
    }
}
