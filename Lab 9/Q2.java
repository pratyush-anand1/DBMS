import java.util.*;
import java.sql.*;

public class Q2 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/university";
        String username = "root";
        String password = "root";

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, username, password);

            while (true) {
                System.out.println("Enter student id/Press 1 to exit");
                int snum = sc.nextInt();
                if (snum != 1) {
                    String studentQuery = "Select * from Student1 where snum = ?";
                    PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
                    studentStmt.setInt(1, snum);
                    ResultSet studentResult = studentStmt.executeQuery();
                    if (!studentResult.next()) {
                        System.out.println("No such student id exists!");
                    } else {
                        System.out.println("Details of enrolled courses:");
                        String accessCoursesQuery = "Select * from Student1 natural join Enrolled where snum = ?";
                        PreparedStatement aCQ = conn.prepareStatement(accessCoursesQuery);
                        aCQ.setInt(1, snum);
                        ResultSet rs = aCQ.executeQuery();
                        System.out.println("SNUM | SNAME | MAJOR | LEVEL | AGE | CNAME | GRADE ");
                        if (rs.next()) {
                            while (rs.next()) {
                                System.out.println(
                                        "-----------------------------------------------------------------------------------------------------------------");
                                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getString(3)
                                        + " | " + rs.getString(4) + " | " + rs.getInt(5) + " | " + rs.getString(6)
                                        + " | " + rs.getString(7));
                            }
                        }
                        else
                        {
                            System.out.println("Not currently enrolled in any course!");
                            System.out.println("From the available course option enter the course name to enroll yourself in a course.");
                            String getCoursesQuery = "Select name from class";
                            Statement getCourses = conn.createStatement();
                            ResultSet gc = getCourses.executeQuery(getCoursesQuery);
                            ArrayList<String> courses = new ArrayList<String>();
                            while(gc.next())
                            {
                                String c = gc.getString(1);
                                System.out.println(c);
                                courses.add(c);
                            }
                            System.out.print("Enter course name here:");
                            sc.nextLine();
                            String choiceCourse = sc.nextLine();
                            if(courses.contains(choiceCourse))
                            {
                                String insertNewEnrolledQuery = "Insert into NewEnrolled values(?,?)";
                                PreparedStatement newEnrolled = conn.prepareStatement(insertNewEnrolledQuery);
                                newEnrolled.setInt(1, snum);
                                newEnrolled.setString(2, choiceCourse);
                                newEnrolled.execute();
                                System.out.println("You have enrolled yourself for " + choiceCourse + " course.");
                            }
                            else
                            {
                                System.out.println("Invalid course name.Terminating....");
                            }
                        }
                    }
                } else {
                    return;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        sc.close();
    }
}