import java.sql.*;
import java.util.*;

public class Q1 {
    public static void main(String args[]) {

        String url = "jdbc:mysql://localhost:3306/university";
        String username = "root";
        String password = "root";

        try {

            Scanner sc = new Scanner(System.in);

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = null;
            System.out.println("Enter name of the table to be viewed.The following tables exists in the database:");
            ResultSet rs = null;
            stmt = con.createStatement();
            rs = stmt.executeQuery("show tables");

            while (rs.next())
            {
                System.out.println(rs.getString(1));
            }

            System.out.println("Enter table name here:");
            String table = sc.next();
            Statement stmt1 = con.createStatement();
            ResultSet rs1 = stmt1.executeQuery("Select * from "+table);
            
            if(table.toLowerCase().equals("student"))
            {
                while(rs1.next())
                {
                    System.out.println("--------------------------------------------------------------------------");
                    System.out.println(rs1.getInt(1) + " | " + rs1.getString(2) + " | " + rs1.getString(3) + " | " + rs1.getString(4) + " | " + rs1.getInt(5));
                }
            }
            else if(table.toLowerCase().equals("faculty"))
            {
                while(rs1.next())
                {
                    System.out.println("--------------------------------------------------------------------------");
                    System.out.println(rs1.getInt(1) + " | " +  rs1.getString(2) + " | " + rs1.getInt(3));
                }
            }
            else if(table.toLowerCase().equals("class"))
            {
                while(rs1.next())
                {
                    System.out.println("--------------------------------------------------------------------------");
                    System.out.println(rs1.getString(1) + " | " + rs1.getString(2) + " | " + rs1.getString(3) + " | " + rs1.getInt(4));
                }
            }
            else if(table.toLowerCase().equals("enrolled"))
            {
                while(rs1.next())
                {
                    System.out.println("--------------------------------------------------------------------------");
                    System.out.println(rs1.getInt(1) + " | " + rs1.getString(2));
                }
            }
            else
            {
                System.out.println("Table doesn't exist.Please enter a valid table name");
            }

            sc.close();
            con.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}