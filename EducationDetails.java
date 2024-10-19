package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EducationDetails extends JFrame implements ActionListener {

    private JTextField tfStudentID, tfDegree, tfInstitution, tfYear;
    private JButton submitBtn, cancelBtn;
    private Connection con;  // Connection object passed via constructor

    // Updated constructor to accept both studentID and Connection
    public EducationDetails(String studentID, Connection con) {
        this.con = con;  // Store the connection for later use
        setTitle("Education Details");
        setSize(600, 400);
        setLocation(300, 100);
        setLayout(null);

        // Adding heading
        JLabel heading = new JLabel("EDUCATION DETAILS");
        heading.setBounds(160, 30, 300, 40);
        heading.setFont(new Font("Bodoni MT", Font.BOLD, 30));
        add(heading);

        // Adding labels and text fields
        createLabelAndTextField("Student ID:", 40, 100, tfStudentID = new JTextField(studentID));
        createLabelAndTextField("Degree:", 40, 150, tfDegree = new JTextField());
        createLabelAndTextField("Institution:", 40, 200, tfInstitution = new JTextField());
        createLabelAndTextField("Year of Graduation:", 40, 250, tfYear = new JTextField());

        // Adding buttons
        submitBtn = new JButton("Submit");
        submitBtn.setBounds(180, 300, 120, 40);
        submitBtn.addActionListener(this);
        add(submitBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(350, 300, 120, 40);
        cancelBtn.addActionListener(this);
        add(cancelBtn);

        // Set fields read-only where applicable
        tfStudentID.setEditable(false);

        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void createLabelAndTextField(String labelText, int labelX, int labelY, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setBounds(labelX, labelY, 150, 30);
        add(label);
        textField.setBounds(labelX + 160, labelY, 150, 30);
        add(textField);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            submitEducationDetails();
        } else if (e.getSource() == cancelBtn) {
            dispose(); // Close the window
        }
    }

    private void submitEducationDetails() {
        String studentID = tfStudentID.getText().trim();
        String degree = tfDegree.getText().trim();
        String institution = tfInstitution.getText().trim();
        String year = tfYear.getText().trim();

        if (degree.isEmpty() || institution.isEmpty() || year.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // Insert into the database
        try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO education (student_id, degree, institution, year_of_graduation) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, studentID);
            pstmt.setString(2, degree);
            pstmt.setString(3, institution);
            pstmt.setString(4, year);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Education details added successfully.");
                dispose(); // Close the window after submission
            } else {
                JOptionPane.showMessageDialog(this, "Error adding education details.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example usage with a Connection object (you need to manage this part)
        Connection con = Conn.getInstance().getConnection();  // Get the connection from your Conn class
        new EducationDetails("123", con);  // Pass both the studentID and connection
    }
}
