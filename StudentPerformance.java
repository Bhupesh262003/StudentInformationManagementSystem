package student.information.management.system;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

public class StudentPerformance extends JFrame implements ActionListener {
    JTextField studentIdField, subjectField, gradeField;
    JTextArea reportArea;
    JButton addGradeButton, generateReportButton;

    // Data structure to hold grades
    HashMap<String, HashMap<String, String>> studentGrades = new HashMap<>();

    public StudentPerformance() {
        setTitle("Student Performance");
        setSize(600, 600);
        setLayout(null);

        // Labels and text fields
        JLabel lblStudentId = new JLabel("Student ID:");
        lblStudentId.setBounds(30, 30, 100, 30);
        add(lblStudentId);

        studentIdField = new JTextField();
        studentIdField.setBounds(150, 30, 150, 30);
        add(studentIdField);

        JLabel lblSubject = new JLabel("Subject:");
        lblSubject.setBounds(30, 80, 100, 30);
        add(lblSubject);

        subjectField = new JTextField();
        subjectField.setBounds(150, 80, 150, 30);
        add(subjectField);

        JLabel lblGrade = new JLabel("Grade:");
        lblGrade.setBounds(30, 130, 100, 30);
        add(lblGrade);

        gradeField = new JTextField();
        gradeField.setBounds(150, 130, 150, 30);
        add(gradeField);

        addGradeButton = new JButton("Add Grade");
        addGradeButton.setBounds(50, 200, 120, 30);
        addGradeButton.addActionListener(this);
        add(addGradeButton);

        generateReportButton = new JButton("Generate Report");
        generateReportButton.setBounds(200, 200, 150, 30);
        generateReportButton.addActionListener(this);
        add(generateReportButton);

        reportArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBounds(30, 250, 500, 300);
        add(scrollPane);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addGradeButton) {
            String studentId = studentIdField.getText();
            String subject = subjectField.getText();
            String grade = gradeField.getText();

            studentGrades.putIfAbsent(studentId, new HashMap<>());
            studentGrades.get(studentId).put(subject, grade);

            JOptionPane.showMessageDialog(null, "Grade added successfully!");
        } else if (ae.getSource() == generateReportButton) {
            String studentId = studentIdField.getText();
            if (studentGrades.containsKey(studentId)) {
                StringBuilder report = new StringBuilder("Performance Report for Student ID: " + studentId + "\n");
                report.append("Subject\tGrade\n");

                HashMap<String, String> grades = studentGrades.get(studentId);
                for (String subject : grades.keySet()) {
                    report.append(subject).append("\t").append(grades.get(subject)).append("\n");
                }

                reportArea.setText(report.toString());
            } else {
                JOptionPane.showMessageDialog(null, "No grades found for the student ID.");
            }
        }
    }

    public static void main(String[] args) {
        new StudentPerformance();
    }
}
