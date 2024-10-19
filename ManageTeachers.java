package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class ManageTeachers extends JFrame implements ActionListener {

    private JButton AddTeacher, ViewTeacher, UpdateTeacher, DeleteTeacher;
    private Connection connection;

    public ManageTeachers(Connection connection) {
        this.connection = connection;
        setTitle("Manage Teachers");
        setSize(650, 400);
        setLocation(300, 100);
        setLayout(null);

        JLabel heading = new JLabel("MANAGE TEACHERS");
        heading.setBounds(150, 40, 400, 40);
        heading.setFont(new Font("Bodoni MT", Font.BOLD, 35));
        add(heading);

        AddTeacher = new JButton("Add Teacher");
        AddTeacher.setBounds(100, 150, 200, 50);
        AddTeacher.setFont(new Font("Tahoma", Font.PLAIN, 18));
        AddTeacher.addActionListener(this);
        add(AddTeacher);

        ViewTeacher = new JButton("View Teacher");
        ViewTeacher.setBounds(350, 150, 200, 50);
        ViewTeacher.setFont(new Font("Tahoma", Font.PLAIN, 18));
        ViewTeacher.addActionListener(this);
        add(ViewTeacher);

        UpdateTeacher = new JButton("Update Teacher");
        UpdateTeacher.setBounds(100, 250, 200, 50);
        UpdateTeacher.setFont(new Font("Tahoma", Font.PLAIN, 18));
        UpdateTeacher.addActionListener(this);
        add(UpdateTeacher);

        DeleteTeacher = new JButton("Delete Teacher");
        DeleteTeacher.setBounds(350, 250, 200, 50);
        DeleteTeacher.setFont(new Font("Tahoma", Font.PLAIN, 18));
        DeleteTeacher.addActionListener(this);
        add(DeleteTeacher);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == AddTeacher) {
            // Open Add Teacher form
            new AddTeacher(connection);
        } else if (ae.getSource() == ViewTeacher) {
            // Open View Teacher form
            new ViewTeacher(connection);
        } else if (ae.getSource() == UpdateTeacher) {
            // Open Update Teacher form
            new UpdateTeacher(connection);
        } else if (ae.getSource() == DeleteTeacher) {
            // Open Delete Teacher form
            new DeleteTeacher(connection);
        }
    }

    public static void main(String[] args) {
        Connection connection = Conn.getInstance().getConnection();
        new ManageTeachers(connection);
    }
}
