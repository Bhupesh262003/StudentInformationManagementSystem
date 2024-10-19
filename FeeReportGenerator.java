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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeeReportGenerator {

    private Connection connection;

    public FeeReportGenerator(Connection connection) {
        this.connection = connection;
    }

    public void generateFeeReport(String studentID) {
        // SQL query to retrieve fee details, including remaining fees
        String sql = "SELECT f.student_id, s.student_name, f.fee_amount, " +
                "IFNULL(f.fee_amount - COALESCE(p.amount_paid, 0), f.fee_amount) AS remaining_fees, " +
                "f.due_date, f.payment_status, f.payment_date " +
                "FROM fees f " +
                "JOIN students s ON f.student_id = s.student_id " +
                "LEFT JOIN (SELECT student_id, SUM(fee_amount) AS amount_paid FROM fees GROUP BY student_id) p ON f.student_id = p.student_id " +
                (studentID != null && !studentID.isEmpty() ? " WHERE f.student_id = ?" : "");

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set student ID if provided
            if (studentID != null && !studentID.isEmpty()) {
                pstmt.setString(1, studentID);
            }

            ResultSet rs = pstmt.executeQuery();

            // Generate unique PDF file name
            String dest = "FeeReport" + (studentID != null && !studentID.isEmpty() ? "_" + studentID : "") + ".pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Fee Report" + (studentID != null && !studentID.isEmpty() ? " for Student ID: " + studentID : ""))
                    .setBold().setFontSize(20).setMarginBottom(20));

            // Create a table with 7 columns
            Table table = new Table(7);
            DeviceRgb lightGray = new DeviceRgb(211, 211, 211); // Light gray color

            // Add table headers
            table.addHeaderCell(new Cell().add(new Paragraph("Student ID")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Name")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Remaining Fees")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Due Date")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Status")).setBackgroundColor(lightGray));
            table.addHeaderCell(new Cell().add(new Paragraph("Payment Date")).setBackgroundColor(lightGray));

            // Fill table with data
            while (rs.next()) {
                String paymentDate = rs.getDate("payment_date") != null ? rs.getDate("payment_date").toString() : "N/A";
                table.addCell(new Paragraph(rs.getString("student_id")));
                table.addCell(new Paragraph(rs.getString("student_name")));
                table.addCell(new Paragraph(String.format("%.2f", rs.getDouble("fee_amount"))));
                table.addCell(new Paragraph(String.format("%.2f", rs.getDouble("remaining_fees")))); // Changed to remaining_fees
                table.addCell(new Paragraph(rs.getDate("due_date") != null ? rs.getDate("due_date").toString() : "N/A"));
                table.addCell(new Paragraph(rs.getString("payment_status")));
                table.addCell(new Paragraph(paymentDate));
            }

            document.add(table); // Add the table to the document
            document.close();
            System.out.println("Fee Report generated as " + dest);

        } catch (SQLException e) {
            System.err.println("Error generating fee report: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error creating PDF document: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
