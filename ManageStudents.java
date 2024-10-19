package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ManageStudents extends JFrame implements ActionListener {

    private JButton addStudent, viewStudents, updateStudent, deleteStudent;
    private Connection connection;

    public ManageStudents(Connection connection) {
        this.connection = connection;
        setTitle("Manage Students");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window when exiting
        setLayout(null);

        // Heading label
        JLabel heading = new JLabel("Manage Students");
        heading.setBounds(250, 30, 300, 40);
        heading.setFont(new Font("Tahoma", Font.BOLD, 30));
        add(heading);

        // Add Student button
        addStudent = new JButton("Add Student");
        addStudent.setBounds(100, 150, 200, 50);
        addStudent.setFont(new Font("Tahoma", Font.PLAIN, 18));
        addStudent.addActionListener(this);
        add(addStudent);

        // View Students button
        viewStudents = new JButton("View Students");
        viewStudents.setBounds(400, 150, 200, 50);
        viewStudents.setFont(new Font("Tahoma", Font.PLAIN, 18));
        viewStudents.addActionListener(this);
        add(viewStudents);

        // Update Student button
        updateStudent = new JButton("Update Student");
        updateStudent.setBounds(100, 250, 200, 50);
        updateStudent.setFont(new Font("Tahoma", Font.PLAIN, 18));
        updateStudent.addActionListener(this);
        add(updateStudent);

        // Delete Student button
        deleteStudent = new JButton("Delete Student");
        deleteStudent.setBounds(400, 250, 200, 50);
        deleteStudent.setFont(new Font("Tahoma", Font.PLAIN, 18));
        deleteStudent.addActionListener(this);
        add(deleteStudent);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addStudent) {
            // Open Add Student form
            new AddStudent(connection);
        } else if (ae.getSource() == viewStudents) {
            // Open View Students form
            new ViewStudents(connection);
        } else if (ae.getSource() == updateStudent) {
            // Open Update Student form
            new UpdateStudent(connection);
        } else if (ae.getSource() == deleteStudent) {
            // Open Delete Student form
            new DeleteStudent(connection);
        }
    }

    public static void main(String[] args) {
        // Establishing connection
        Connection connection = Conn.getInstance().getConnection();
        new ManageStudents(connection);
    }
}
