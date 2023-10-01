import java.util.*;
import java.sql.*;
import java.io.*;

public class Q3 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/university";
        String username = "root";
        String password = "root";

        String createStudentResearchTableQuery = "Create table if not exists student_research (snum int,paper_id varchar(20))";
        String insertTableQuery = "Insert into student_research values(?,?)";
        String createPapersQuery = "Create table if not exists papers(paper_id varchar(20),paper MEDIUMBLOB)";
        String insertPapersQuery = "Insert into papers values(?,?)";

        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, username, password);

            Statement stmt = conn.createStatement();
            stmt.execute(createStudentResearchTableQuery);
            stmt.execute(createPapersQuery);

            System.out.println("Press 1 to insert paper information else press any key to exit");
            String choice = sc.next();
            if (choice.equals("1")) {
                int num;
                String getsnumQuery = "Select snum from student";
                Statement getids = conn.createStatement();
                ResultSet gc = getids.executeQuery(getsnumQuery);
                ArrayList<Integer> ids = new ArrayList<Integer>();
                System.out.println("Enter student id:");
                num = sc.nextInt();
                while (gc.next()) {
                    int snum = gc.getInt(1);
                    ids.add(snum);
                }
                if (ids.contains(num)) {
                    String paperID;
                    System.out.println("Enter paper id");
                    paperID = sc.next();
                    PreparedStatement pstmnt = conn.prepareStatement(insertTableQuery);
                    pstmnt.setInt(1, num);
                    pstmnt.setString(2, paperID);
                    pstmnt.executeUpdate();
                    String path = paperID + ".pdf" ;
                    InputStream inp = new FileInputStream(new File(path));
                    PreparedStatement pstmt = conn.prepareStatement(insertPapersQuery);
                    pstmt.setString(1, paperID);
                    pstmt.setBlob(2, inp);
                    pstmt.execute();
                } else {
                    System.out.println("No student with the given student id");
                }
            } else {
                return;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        sc.close();
    }
}