package student.information.management.system;

public class Student {
    private String studentId;
    private String name;
    private String fatherName;
    private String dob;
    private String address;
    private String phone;
    private String email;
    private String gender;

    // Constructor
    public Student(String studentId, String name, String fatherName, String dob, String address, String phone, String email, String gender) {
        this.studentId = studentId;
        this.name = name;
        this.fatherName = fatherName;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getFatherName() { return fatherName; }
    public String getDob() { return dob; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }

    // Setters (optional, based on requirements)
    public void setName(String name) { this.name = name; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public void setDob(String dob) { this.dob = dob; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setGender(String gender) { this.gender = gender; }
}
