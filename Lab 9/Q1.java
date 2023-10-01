import java.util.*;
import java.sql.*;

public class Q1 {
    public static void main(String[] args) 
    {
        String url = "jdbc:mysql://localhost:3306/university";
        String username = "root";
        String password = "root";

        String createStudent1TableQuery = "Create table if not exists Student1 (snum int,sname varchar(20),major varchar(50),level varchar(20),age int)";
        String createNewEnrolledTableQuery = "Create table if not exists NewEnrolled (snum int,cname varchar(40))";
        String insertTableQuery = "Insert into Student1 values(?,?,?,?,?)";

        Scanner sc = new Scanner(System.in);

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, username, password);

            Statement stmt = conn.createStatement();
            stmt.execute(createStudent1TableQuery);
            stmt.execute(createNewEnrolledTableQuery);

            System.out.println("Press 1 to insert student information else press any key to exit");
            String choice = sc.next();
            if(choice.equals("1"))
            {
                int num;
                System.out.println("How many students ?");
                num = sc.nextInt();

                int i=1;

                while(i <= num)
                {
                    System.out.println("Student " + i + " information:");
                    System.out.println("Enter student id:");
                    int snum = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter name of the student:");
                    String sname = sc.nextLine();
                    System.out.println("Enter major of student:");
                    String major = sc.nextLine();
                    System.out.println("Enter level:");
                    String level = sc.nextLine();
                    System.out.println("Enter age of the student");
                    int age = sc.nextInt();
                    PreparedStatement pstmnt = conn.prepareStatement(insertTableQuery);
                    pstmnt.setInt(1, snum);
                    pstmnt.setString(2, sname);
                    pstmnt.setString(3, major);
                    pstmnt.setString(4, level);
                    pstmnt.setInt(5, age);

                    pstmnt.executeUpdate();
                    i++;
                }
                System.out.println("Student information updated succesfully!");
                sc.close();
            }
            else
            {
                return;
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}