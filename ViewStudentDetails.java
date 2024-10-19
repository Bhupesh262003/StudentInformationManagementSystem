package student.information.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ViewStudentDetails extends JFrame {
    private String studentId;
    private JTable studentDetailsTable;
    private Connection connection;

    public ViewStudentDetails(String studentId, Connection connection) {
        this.studentId = studentId;
        this.connection = connection;

        setTitle("View Student Details");
        setSize(800, 500);
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Student Details", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table to display student details
        studentDetailsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentDetailsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton btnExport = new JButton("Export to Excel");
        buttonPanel.add(btnExport);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listener for Export button
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToExcel();
            }
        });

        // Fetch and display student details
        loadStudentDetails();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadStudentDetails() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Student ID");
        model.addColumn("Name");
        model.addColumn("Father's Name");
        model.addColumn("DOB");
        model.addColumn("Address");
        model.addColumn("Phone No");
        model.addColumn("Email");
        model.addColumn("Gender");
        model.addColumn("Blood Group");
        model.addColumn("Department");

        try {
            String studentQuery = "SELECT student_id,student_name, father_name, dob, address, phone_no, email, gender, blood_group, department FROM students WHERE student_id = ?";
            PreparedStatement studentStmt = connection.prepareStatement(studentQuery);
            studentStmt.setString(1, studentId);
            ResultSet studentRs = studentStmt.executeQuery();

            if (studentRs.next()) {

                String studentID = studentRs.getString("student_id");
                String name = studentRs.getString("student_name");
                String fatherName = studentRs.getString("father_name");
                Date dob = studentRs.getDate("dob");
                String address = studentRs.getString("address");
                String phoneNo = studentRs.getString("phone_no");
                String email = studentRs.getString("email");
                String gender = studentRs.getString("gender");
                String bloodGroup = studentRs.getString("blood_group");
                String department = studentRs.getString("department");

                model.addRow(new Object[]{studentID, name, fatherName, dob, address, phoneNo, email, gender, bloodGroup, department});
            } else {
                JOptionPane.showMessageDialog(this, "No student found with ID: " + studentId, "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            studentDetailsTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading student details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Details");
        Row headerRow = sheet.createRow(0);

        // Create header row
        for (int i = 0; i < studentDetailsTable.getColumnCount(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(studentDetailsTable.getColumnName(i));
        }

        // Create data rows
        for (int i = 0; i < studentDetailsTable.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < studentDetailsTable.getColumnCount(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(studentDetailsTable.getValueAt(i, j).toString());
            }
        }

        // Write to Excel file
        try (FileOutputStream fileOut = new FileOutputStream("student_details.xlsx")) {
            workbook.write(fileOut);
            JOptionPane.showMessageDialog(this, "Data exported to Excel successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Closing workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
