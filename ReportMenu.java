package student.information.management.system;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportMenu extends JFrame {
    private Conn conn;

    public ReportMenu() {
        conn = Conn.getInstance();
        setTitle("Report Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton generateFeeReportButton = new JButton("Generate Fee Report");
        generateFeeReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = JOptionPane.showInputDialog("Enter Student ID:");
                if (studentId != null && !studentId.trim().isEmpty()) {
                    FeeReportGenerator reportGenerator = new FeeReportGenerator(conn.getConnection());
                    reportGenerator.generateFeeReport(studentId);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Student ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(generateFeeReportButton);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportMenu());
    }
}
