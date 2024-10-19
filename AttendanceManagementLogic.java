package student.information.management.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * AttendanceManagementLogic handles the logic for attendance management.
 */
public class AttendanceManagementLogic {

    private Connection connection;

    public AttendanceManagementLogic(Connection connection) {
        this.connection = connection;
    }

    /**
     * Marks attendance for a student.
     *
     * @param studentId The ID of the student.
     * @param status    The attendance status (Present/Absent).
     * @param teacherId The ID of the teacher.
     * @param attendanceDate The date for which attendance is being marked.
     * @return true if attendance was marked successfully, false otherwise.
     */
    public boolean markAttendance(String studentId, String status, String teacherId, java.sql.Date attendanceDate) {
        // Check if attendance already exists for the student today
        if (attendanceExists(studentId, attendanceDate, teacherId)) {
            System.out.println("Attendance already marked for student " + studentId + " today.");
            return false; // Prevent duplicate entry
        }

        // SQL query to insert attendance record
        String query = "INSERT INTO attendance (student_id, status, teacher_id, attendance_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, status);
            stmt.setString(3, teacherId);
            stmt.setDate(4, attendanceDate); // Set the provided date

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if attendance was marked successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an error
        }
    }

    /**
     * Checks if attendance has already been marked for a student today.
     *
     * @param studentId The ID of the student.
     * @param attendanceDate The date to check for attendance.
     * @param teacherId The ID of the teacher.
     * @return true if attendance already exists, false otherwise.
     */
    private boolean attendanceExists(String studentId, java.sql.Date attendanceDate, String teacherId) {
        String query = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND attendance_date = ? AND teacher_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setDate(2, attendanceDate); // Check for the specified date
            stmt.setString(3, teacherId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, attendance already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Attendance does not exist
    }


    /**
     * Checks if a student exists in the database.
     *
     * @param studentId The ID of the student.
     * @return true if the student exists, false otherwise.
     */
    public boolean isStudentExists(String studentId) {
        String query = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, the student exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Student does not exist
    }

    // Additional methods can be added here for more functionality if needed

}
