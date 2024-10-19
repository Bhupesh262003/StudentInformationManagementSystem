package student.information.management.system;

public class Teacher {
    private String teacherId;
    private String name;
    private String fatherName;
    private String dob;
    private String address;
    private String phone;
    private String email;
    private String gender;
    private String bloodGroup;
    private String department;
    private String subject;

    public Teacher(String teacherId, String name, String fatherName, String dob, String address, String phone, String email, String gender, String bloodGroup, String department, String subject) {
        this.teacherId = teacherId;
        this.name = name;
        this.fatherName = fatherName;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.department = department;
        this.subject = subject;
    }

    // Getters and setters (if needed)
    public String getTeacherId() { return teacherId; }
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public String getDob() { return dob; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getBloodGroup() { return bloodGroup; }
    public String getDepartment() { return department; }
    public String getSubject() { return subject; }
}
