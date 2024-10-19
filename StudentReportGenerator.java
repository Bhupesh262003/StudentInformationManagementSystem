package student.information.management.system;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.colors.DeviceRgb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentReportGenerator {

    private Connection connection;

    public StudentReportGenerator(Connection connection) {
        this.connection = connection;
    }

    public void generateStudentReport() {
        // Update the SQL query based on the actual column names in your database
        String sql = "SELECT s.student_id, s.student_name AS name, g.grade, c.course_name " +
                "FROM students s " +
                "JOIN grades g ON s.student_id = g.student_id " +
                "JOIN courses c ON g.course_id = c.course_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            String dest = "StudentReport.pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Student Report").setBold().setFontSize(20));

            // Create a table with 4 columns
            Table table = new Table(4);
            DeviceRgb lightGray = new DeviceRgb(211, 211, 211); // Light gray color

            table.addHeaderCell(new Cell().add(new Paragraph("Student ID")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Name")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Course")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Grade")).setBackgroundColor(lightGray));

            while (rs.next()) {
                table.addCell(new Paragraph(rs.getString("student_id")));
                table.addCell(new Paragraph(rs.getString("name")));
                table.addCell(new Paragraph(rs.getString("course_name")));
                table.addCell(new Paragraph(rs.getString("grade")));
            }

            document.add(table); // Add the table to the document
            document.close();
            System.out.println("Student Report generated as " + dest);

        } catch (SQLException e) {
            System.err.println("Error generating student report for SQL: " + sql);
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error creating PDF document: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
