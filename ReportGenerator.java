package student.information.management.system;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportGenerator {

    private Connection con;  // Database connection

    // Constructor that accepts a Connection object
    public ReportGenerator(Connection con) {
        this.con = con;  // Save the connection for later use
    }

    public void generateStudentReport() {
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT s.student_id, s.name, g.grade, c.course_name " +
                     "FROM students s " +
                     "JOIN grades g ON s.student_id = g.student_id " +
                     "JOIN courses c ON g.course_id = c.course_id")) {

            String dest = "StudentReport.pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Fetch data and construct report records
            while (rs.next()) {
                String record = String.format("Student ID: %s, Name: %s, Course: %s, Grade: %s",
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("course_name"),
                        rs.getString("grade"));
                document.add(new Paragraph(record));
            }

            document.close();
            System.out.println("Student Report generated as StudentReport.pdf");

        } catch (SQLException e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage with a Connection object
        Connection con = Conn.getInstance().getConnection();  // Get database connection
        ReportGenerator reportGenerator = new ReportGenerator(con);  // Pass the connection
        reportGenerator.generateStudentReport();  // Generate the report
    }
}
