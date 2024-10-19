package student.information.management.system;

import javax.swing.*;

public class Attendance extends JFrame {

    public Attendance() {
        setTitle("Attendance Records");
        setSize(600, 400);
        setLocation(400, 200);

        // Add components to display attendance records
        JLabel attendanceLabel = new JLabel("Display attendance records here.");
        add(attendanceLabel);

        setVisible(true);
    }
}
