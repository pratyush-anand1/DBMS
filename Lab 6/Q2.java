import java.sql.*;
import java.util.*;

public class Q2 {
    public static void main(String args[]) {

        String url = "jdbc:mysql://localhost:3306/university";
        String username = "root";
        String password = "root";

        try {

            Scanner sc = new Scanner(System.in);

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            /*
             * Statement stmt = null;
             * System.out.
             * println("Enter name of the table to be viewed.The following tables exists in the database:"
             * );
             * ResultSet rs = null;
             * stmt = con.createStatement();
             * rs = stmt.executeQuery("show tables");
             */
            while (true) {
                try {
                    System.out.println("Enter the option number:");
                    System.out.println("1. View classes deatils of the desired room.");
                    System.out.println("2. View timing details and venue for a course.");
                    System.out.println("3. View courses taught by a faculty memebr.");
                    System.out.println("4. Exit");
                    System.out.print("Option Number:");
                    int a = sc.nextInt();

                    if (a == 1) {
                        System.out.println("Enter room number:");
                        sc.nextLine();
                        //System.out.println("h");
                        String room = sc.nextLine();
                        //System.out.println(room);
                        PreparedStatement pstmnt = con.prepareStatement("Select * from class where room = ?");
                        pstmnt.setString(1, room);
                        ResultSet rs = pstmnt.executeQuery();
                        if (!rs.next()) {
                            System.out.println("Invalid room number");
                            continue;
                        }
                        System.out.println("DETAILS OF COURSES IN ROOM NO. " + room);
                        System.out.println("-------------------------------------------");
                        System.out.println("NAME_OF_COURSE |  TIMINGS |  FACULTY ID");
                        System.out.println("--------------------------------------------");
                        while (rs.next()) {
                            System.out.println(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getInt(4));
                            System.out.println("-------------------------------------------");
                        }
                        continue;
                    }

                    else if (a == 2) {
                        System.out.println("Enter name of the course:");
                        sc.nextLine();
                        String course = sc.nextLine();
                        Statement pstmnt1 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        //pstmnt1.setString(1, course);
                       // Statement pstmnt1 = null;
                        ResultSet rs1 = pstmnt1.executeQuery("Select room,meets_at from class where name ='"+course+"'");
                        //System.out.println(rs1.next());
                        if (!rs1.next()) {
                            System.out.println("No room number and timings found for the given course");
                            continue;
                        }
                       // System.out.println(rs1.next());
                        System.out.println("ROOM AND TIMING OF COURSE " + course);
                        System.out.println("-------------------------------------------");
                        System.out.println("ROOM NO. |  TIMINGS");
                        System.out.println("--------------------------------------------");
                        //System.out.println(rs1.next());
                        rs1.beforeFirst();
                        while (rs1.next()) {
                            System.out.println(rs1.getString(1) + " | " + rs1.getString(2));
                            System.out.println("-------------------------------------------");
                        }
                        continue;
                    }

                    else if (a == 3) {
                        System.out.println("Enter name of the teacher:");
                        sc.nextLine();
                        String fname = sc.nextLine();
                        Statement pstmnt1 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        //pstmnt1.setString(1, course);
                       // Statement pstmnt1 = null;
                        ResultSet rs1 = pstmnt1.executeQuery("select name from class where fid in (select fid from faculty where fname ='"+fname+"')");
                        //System.out.println(rs1.next());
                        if (!rs1.next()) {
                            System.out.println("Either the faculty name is invalid or the faculty does not teach any course.");
                            continue;
                        }
                       // System.out.println(rs1.next());
                        System.out.println("NAME OF COURSES TAUGHT BY " + fname);
                        System.out.println("-------------------------------------------");
                        System.out.println("COURSE NAME");
                        System.out.println("--------------------------------------------");
                        //System.out.println(rs1.next());
                        rs1.beforeFirst();
                        while (rs1.next()) {
                            System.out.println(rs1.getString(1));
                            System.out.println("-------------------------------------------");
                        }
                        continue;
                    }

                    else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Invalid operation performed. Terminating....");
                    break;
                }
            }
            sc.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}