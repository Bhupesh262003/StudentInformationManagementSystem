package student.information.management.system;

public class DriverTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver successfully loaded.");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC Driver.");
            e.printStackTrace();
        }
    }
}
