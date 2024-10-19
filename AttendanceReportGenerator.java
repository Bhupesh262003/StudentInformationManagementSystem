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

public class AttendanceReportGenerator {

    private Connection connection;

    public AttendanceReportGenerator(Connection connection) {
        this.connection = connection;
    }

    public void generateAttendanceReport() {
        String sql = "SELECT a.student_id, s.student_name, a.attendance_date, a.status, c.course_name " +
                "FROM attendance a " +
                "JOIN students s ON a.student_id = s.student_id " +
                "JOIN courses c ON a.course_id = c.course_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            String dest = "AttendanceReport.pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Attendance Report").setBold().setFontSize(20));

            // Create a table with 5 columns
            Table table = new Table(5);
            DeviceRgb lightGray = new DeviceRgb(211, 211, 211); // Light gray color

            table.addHeaderCell(new Cell().add(new Paragraph("Student ID")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Name")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Date")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Status")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Course")).setBackgroundColor(lightGray));

            while (rs.next()) {
                table.addCell(new Paragraph(rs.getString("student_id")));
                table.addCell(new Paragraph(rs.getString("student_name")));
                table.addCell(new Paragraph(rs.getDate("attendance_date").toString()));
                table.addCell(new Paragraph(rs.getString("status")));
                table.addCell(new Paragraph(rs.getString("course_name")));
            }

            document.add(table); // Add the table to the document
            document.close();
            System.out.println("Attendance Report generated as " + dest);

        } catch (SQLException e) {
            System.err.println("Error generating attendance report: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error creating PDF document: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
