/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.finalproj;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Taya
 */
public class Length_content {

    /**
     * @param entries
     * @param args the command line arguments
     */
    
    public static ArrayList<File> content_filter(ArrayList<File> entries, String key){
        ArrayList<File> sublist = new ArrayList<>();
        
        for(File afile : entries){
            try(BufferedReader input = new BufferedReader(new FileReader(afile))){
                while (true){
                    String line = input.readLine();
                    if (line == null){
                        break;
                    }
                    else if(line.contains(key)){
                        sublist.add(afile);
                        break;
                    }
                }
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }   
        return sublist;
    }
    
    public static ArrayList<File> length_filter(ArrayList<File> entries, long length, String Operator){
        ArrayList<File> sublist = new ArrayList<>();
        
        for(File afile : entries){
            long file_len = afile.length();
            
            switch (Operator) {
                case "EQ" -> {
                    if(file_len == length){
                        sublist.add(afile);
                    }
                }
                case "NEQ" -> {
                    if(file_len != length){
                        sublist.add(afile);
                    }
                }
                case "GT" -> {
                    if(file_len > length){
                        sublist.add(afile);
                    }
                }
                case "GTE" -> {
                    if(file_len >= length){
                        sublist.add(afile);
                    }
                }
                case "LT" -> {
                    if(file_len < length){
                        sublist.add(afile);
                    }
                }
                case "LTE" -> {
                    if(file_len <= length){
                        sublist.add(afile);
                    }
                }
            }
        }
        
        return sublist;
       
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<File> inputs = new ArrayList<>();
        inputs.add(new File("C:/Users/Taya/Downloads/Countries.txt"));
        inputs.add(new File("C:/Users/Taya/Documents/GitHub/FinalProject/finalProj23/DownloadedFile.txt"));
        inputs.add(new File("C:/Users/Taya/Documents/justin.txt"));
        
        /*for (File afile : inputs){
            System.out.println(afile.length());
        }*/
        
        //ArrayList<File> sublist = content_filter(inputs, "Turkey");
        ArrayList<File> sublist = length_filter(inputs, 10, "GT");
        
        for(File afile: sublist){
            System.out.println(afile.getPath());
        }
    }
    
}
