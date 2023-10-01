import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Q4 {
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "root");
        
        System.out.println(">> RETRIEVE PAPERS OF A STUDENT");
        System.out.println(">> Enter StudentId:");
        int sid = scan.nextInt();
        
        PreparedStatement st = conn.prepareStatement("SELECT paper_id,paper FROM student_research natural join papers WHERE snum = ?");
        st.setInt(1, sid);
        ResultSet rs = st.executeQuery();

        while(rs.next()) {
            String paper_id = rs.getString("paper_id");
            InputStream inputStream = rs.getBinaryStream("paper");
            FileOutputStream outputStream = new FileOutputStream("retfiles/" + paper_id + ".pdf");
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            System.out.println("Paper " + paper_id + " retrieved.");
            outputStream.close();
        }

        scan.close();
        conn.close();
    }
}