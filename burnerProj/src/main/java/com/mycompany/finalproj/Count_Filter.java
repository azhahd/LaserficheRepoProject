package com.mycompany.finalproj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Count_Filter {
    
    public static ArrayList<File> countFilter(ArrayList<File> entries, String key, int min) throws IOException {
        ArrayList<File> filteredEntries = new ArrayList<File>();
        
        for (File entry : entries) {
            if (entry.isFile()) {
                BufferedReader reader = new BufferedReader(new FileReader(entry));
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    count += countSubstring(line, key);
                }
                reader.close();
                
                if (count >= min) {
                    filteredEntries.add(entry);
                }
            }
        }
        
        return filteredEntries;
    }
    
    private static int countSubstring(String str, String substr) {
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(substr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += substr.length();
            }
        }
        return count;
    }
    
    public static void main(String[] args) throws IOException {
        // Set up input files and parameters
        ArrayList<File> entries = new ArrayList<File>();
        entries.add(new File("athirumu1.txt"));
        entries.add(new File("athirumu2.txt"));

        String key = "key";
        int min = 2;
        
        // Run count filter and print results
        ArrayList<File> filteredEntries = countFilter(entries, key, min);
        System.out.println("Filtered entries:");
        for (File entry : filteredEntries) {
            System.out.println(entry.getName());
        }
    }
}