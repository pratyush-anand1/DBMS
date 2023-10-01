import java.io.*;

public class Question5{
    public static void main(String[] args) throws Exception{
        try{
            File f1 = new File("C:\\Users\\Om Patil\\Desktop\\Programming\\CS241 Lab\\Assignment 1\\faculty.txt");
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            String str1= "";
            while ((str1 = br1.readLine()) != null){
                String[] words1 = str1.split(",");
                int deptId = Integer.parseInt(words1[2]);
                if(deptId == 20){
                    System.out.println(words1[1]);
                }
            }
            br1.close();
        }
        catch(Exception e){
            System.err.println("Error parsing files");
        }
    }
}
