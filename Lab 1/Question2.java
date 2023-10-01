import java.io.*;

public class Question2{
    public static void main(String[] args) throws Exception{
        try{
            File f1 = new File("C:\\Users\\Om Patil\\Desktop\\Programming\\CS241 Lab\\Assignment 1\\student.txt");
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            String str1= "";
            while((str1 = br1.readLine())!=  null){
                String[] words1 = str1.split(",");
                int age = Integer.parseInt(words1[4]);
                if(age > 18){
                    for (String st : words1) {
                        System.out.print(st + " ");
                    }
                    System.out.println(" ");
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
