package student.information.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class ViewFee extends JFrame {
    private JTextField tfFeeAmount;
    private JTextField tfRemainingFees; // New JTextField for remaining fees
    private JComboBox<String> paymentStatusComboBox;
    private JDateChooser dueDateChooser;
    private FeeManager feeManager;
    private String studentID;

    public ViewFee(String studentID, Connection connection) {
        this.studentID = studentID;
        feeManager = new FeeManager(connection);

        setTitle("View and Update Fee Details for Student ID: " + studentID);
        setSize(400, 350); // Adjusted size to accommodate new field
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblStudentID = new JLabel("Student ID: " + studentID);
        lblStudentID.setBounds(20, 20, 200, 25);
        add(lblStudentID);

        JLabel lblFeeAmount = new JLabel("Fee Amount:");
        lblFeeAmount.setBounds(20, 60, 100, 25);
        add(lblFeeAmount);

        tfFeeAmount = new JTextField();
        tfFeeAmount.setBounds(120, 60, 200, 25);
        add(tfFeeAmount);

        JLabel lblRemainingFees = new JLabel("Remaining Fees:"); // New label for remaining fees
        lblRemainingFees.setBounds(20, 100, 100, 25);
        add(lblRemainingFees);

        tfRemainingFees = new JTextField(); // New text field for remaining fees
        tfRemainingFees.setBounds(120, 100, 200, 25);
        add(tfRemainingFees);

        JLabel lblPaymentStatus = new JLabel("Payment Status:");
        lblPaymentStatus.setBounds(20, 140, 100, 25);
        add(lblPaymentStatus);

        paymentStatusComboBox = new JComboBox<>(new String[]{"Paid", "Unpaid", "Pending", "Overdue"});
        paymentStatusComboBox.setBounds(120, 140, 200, 25);
        add(paymentStatusComboBox);

        JLabel lblDueDate = new JLabel("Due Date:");
        lblDueDate.setBounds(20, 180, 100, 25);
        add(lblDueDate);

        dueDateChooser = new JDateChooser();
        dueDateChooser.setBounds(120, 180, 200, 25);
        add(dueDateChooser);

        JButton btnUpdate = new JButton("Update Fee Details");
        btnUpdate.setBounds(120, 220, 150, 30);
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFeeDetails();
            }
        });
        add(btnUpdate);

        // Load fee details for the studentID when the form is opened
        loadFeeDetails();
        setVisible(true);
    }

    private void loadFeeDetails() {
        // Load the fee details from the database using the feeManager
        try {
            FeeDetails feeDetails = feeManager.getFeeDetails(studentID);
            if (feeDetails != null) {
                tfFeeAmount.setText(String.valueOf(feeDetails.getFeeAmount()));
                tfRemainingFees.setText(String.valueOf(feeDetails.getRemainingFees())); // Load remaining fees
                paymentStatusComboBox.setSelectedItem(feeDetails.getPaymentStatus());
                dueDateChooser.setDate(feeDetails.getDueDate());
            } else {
                JOptionPane.showMessageDialog(this, "No fee details found for Student ID: " + studentID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading fee details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFeeDetails() {
        double feeAmount;
        double remainingFees; // New variable for remaining fees
        try {
            feeAmount = Double.parseDouble(tfFeeAmount.getText().trim());
            remainingFees = Double.parseDouble(tfRemainingFees.getText().trim()); // Get remaining fees
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid amounts.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String paymentStatus = (String) paymentStatusComboBox.getSelectedItem();
        Date dueDate = new Date(dueDateChooser.getDate().getTime());

        try {
            feeManager.updateFeeDetails(studentID, feeAmount, remainingFees, paymentStatus, dueDate); // Pass remainingFees
            JOptionPane.showMessageDialog(this, "Fee details updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating fee details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        SwingUtilities.invokeLater(() -> {
            new ViewFee("12345", connection).setVisible(true); // Example student ID
        });
    }
}
