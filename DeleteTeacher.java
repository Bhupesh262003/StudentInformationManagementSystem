package student.information.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteTeacher extends JFrame implements ActionListener {
    JTextField tfTeacherID;
    JButton deleteBtn, cancelBtn;
    private Connection connection;

    public DeleteTeacher(Connection connection) {
        this.connection = connection; // Store the connection
        setTitle("Delete Teacher");
        setSize(400, 300);
        setLocation(300, 150);
        setLayout(null);

        JLabel heading = new JLabel("Delete Teacher Record");
        heading.setBounds(100, 30, 200, 30);
        heading.setFont(new Font("Bodoni MT", Font.BOLD, 18));
        add(heading);

        JLabel lblTeacherID = new JLabel("Teacher ID:");
        lblTeacherID.setBounds(50, 100, 100, 30);
        add(lblTeacherID);

        tfTeacherID = new JTextField();
        tfTeacherID.setBounds(150, 100, 150, 30);
        add(tfTeacherID);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(50, 180, 100, 30);
        deleteBtn.addActionListener(this);
        add(deleteBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(200, 180, 100, 30);
        cancelBtn.addActionListener(this);
        add(cancelBtn);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteBtn) {
            String teacherID = tfTeacherID.getText().trim();

            if (teacherID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter Teacher ID.");
                return;
            }

            int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM teachers WHERE teacher_id = ?")) {
                    pstmt.setString(1, teacherID);
                    int result = pstmt.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Teacher record deleted successfully.");
                        tfTeacherID.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Teacher ID not found.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }
            }
        } else if (e.getSource() == cancelBtn) {
            dispose();
        }
    }

    public static void main(String[] args) {
        Conn conn = Conn.getInstance();
        Connection connection = conn.getConnection();
        new DeleteTeacher(connection);
    }
}
