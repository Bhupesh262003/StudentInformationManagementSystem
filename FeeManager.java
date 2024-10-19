package student.information.management.system;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeeManager {
    private Connection connection;

    public FeeManager(Connection connection) {
        this.connection = connection;
    }

    // Method to add fee details
    public void addFeeDetails(String studentID, double feeAmount, String paymentStatus, Date dueDate, double remainingFees) throws SQLException {
        String insertQuery = "INSERT INTO fees (student_id, fee_amount, payment_status, due_date, remaining_fees) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, studentID);
            statement.setDouble(2, feeAmount);
            statement.setString(3, paymentStatus);
            statement.setDate(4, dueDate);
            statement.setDouble(5, remainingFees); // Set remaining fees
            statement.executeUpdate();
        }
    }

    // Method to fetch fee details by student ID
    public FeeDetails getFeeDetails(String studentID) throws SQLException {
        String query = "SELECT fee_amount, remaining_fees, payment_status, due_date FROM fees WHERE student_id = ?";
        FeeDetails feeDetails = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, studentID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double feeAmount = resultSet.getDouble("fee_amount");
                double remainingFees = resultSet.getDouble("remaining_fees"); // Fetch remaining fees
                String paymentStatus = resultSet.getString("payment_status");
                Date dueDate = resultSet.getDate("due_date");

                // Create the FeeDetails object
                feeDetails = new FeeDetails(studentID, feeAmount, remainingFees, paymentStatus, dueDate);
            } else {
                throw new SQLException("No fee details found for Student ID: " + studentID);
            }
        }

        return feeDetails;
    }

    // Method to update fee details
    public void updateFeeDetails(String studentID, double feeAmount, double remainingFees, String paymentStatus, Date dueDate) throws SQLException {
        String updateQuery = "UPDATE fees SET fee_amount = ?, remaining_fees = ?, payment_status = ?, due_date = ? WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setDouble(1, feeAmount);
            statement.setDouble(2, remainingFees); // Update remaining fees
            statement.setString(3, paymentStatus);
            statement.setDate(4, dueDate);
            statement.setString(5, studentID);
            statement.executeUpdate();
        }
    }
}
