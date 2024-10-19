package student.information.management.system;

import java.sql.Date;

public class FeeDetails {
    private String studentID;
    private double feeAmount;
    private double remainingFees;
    private String paymentStatus;
    private Date dueDate;

    public FeeDetails(String studentID, double feeAmount, double remainingFees, String paymentStatus, Date dueDate) {
        this.studentID = studentID;
        this.feeAmount = feeAmount;
        this.remainingFees = remainingFees;
        this.paymentStatus = paymentStatus;
        this.dueDate = dueDate;
    }

    // Getters
    public String getStudentID() {
        return studentID;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public double getRemainingFees() {
        return remainingFees;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Date getDueDate() {
        return dueDate;
    }
}
