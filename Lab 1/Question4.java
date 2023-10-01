import java.io.*;
import java.util.*;

public class Question4{
    public static void main(String[] args) throws Exception{
        try{
            File f1 = new File("C:\\Users\\Om Patil\\Desktop\\Programming\\CS241 Lab\\Assignment 1\\enrolled.txt");
            File f2 = new File("C:\\Users\\Om Patil\\Desktop\\Programming\\CS241 Lab\\Assignment 1\\student.txt");
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            String str1= "";String str2= "";
            ArrayList<String> ar = new ArrayList<>();
            while (((str1 = br1.readLine()) != null)){
                String[] words1 = str1.split(",");
                if(words1[1].equals("Database Systems")){
                    String snum = words1[0];
                    ar.add(snum);
                }
            }
            while (((str2 = br2.readLine()) != null)){
                String[] words2 = str2.split(",");
                for(String obj : ar){
                    if(obj.equals(words2[0])){
                        System.out.println(words2[1]);
                    }
                }
            }
            br1.close();
            br2.close();
        }
        catch(Exception e){
            System.err.println("Error parsing files");
        }
    }
}
