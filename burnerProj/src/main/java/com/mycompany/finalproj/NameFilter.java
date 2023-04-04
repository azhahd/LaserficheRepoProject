/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sampleproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author aizah
 */
public class NameFilter {
    
    public static ArrayList<File> nameFilter (ArrayList<File> entries, String key)
    {
        ArrayList<File> sublist = new ArrayList<>();
        
        for(File afile : entries)
        {
            try(BufferedReader input = new BufferedReader(new FileReader(afile)))
            {
                while(true)
                {
                    String line = input.readLine();
                
                    if (line == null)
                    break;
                
                    if (line.contains(key)) 
                    {
                        String filename = afile.getName();
                        String newname = filename + key;
                        File renamedFile = new File(afile.getParent(), newname);
                        boolean success = afile.renameTo(renamedFile);
                    
                        if (success) 
                        {
                            System.out.println("Renamed file: " + renamedFile.getName());
                            sublist.add(renamedFile);
                        }
                    
                        break;
                    }  
                }
            }
            
            catch(IOException e)
            {
                System.out.println("Handling exception");
            }
        }
        
        return sublist;
    }


public static void main(String[] args){
    
    ArrayList<File> inputs = new ArrayList<>();
        inputs.add(new File("/Users/aizah/Downloads/Course Work/Prog/Final project files/Countries.txtCanada"));
        //inputs.add(new File("/Users/aizah/Downloads/Course Work/Prog/Final project files/SampleCityNamesInCanada_copy_copy_copy_copy_copy_copy_copy.txt"));
    
    nameFilter(inputs,"Canada");


}

}