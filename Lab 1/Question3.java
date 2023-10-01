import java.io.*;

public class Question3{
    public static void main(String[] args) throws Exception{
        try{
            File f1 = new File("C:\\Users\\Om Patil\\Desktop\\Programming\\CS241 Lab\\Assignment 1\\enrolled.txt");
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            String str1= "";
            while((str1 = br1.readLine())!=  null){
                String[] words1 = str1.split(",");
                if(words1[1].equals("Database Systems")){
                    System.out.println(words1[0]);
                }
                else{
                    continue;
                }
            }
            br1.close();
        }
        catch(Exception e){
            System.err.println("Error parsing files");
        }
    }
}
