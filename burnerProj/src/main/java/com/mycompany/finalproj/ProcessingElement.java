/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.finalproj;

import java.io.*;
import java.util.ArrayList;

public class ProcessingElement {

    public ArrayList split(int n, ArrayList<Object> entries, String outputFilePrefix) throws IOException {
        n = Math.abs(n);// As long as n>0
        int fileCount = 1;// original file
        int lineCount = 0;// count lines
        ArrayList<Object> newEntries = new ArrayList<Object>();

        //File inputFile = new File("Countries_copy_copy copy.txt");// tried it with just a sample file
        //String outputFilePrefix = "Countries_copy_copy copy";
        
        File folder = new File("Split Files");
        folder.mkdirs();//create directory
        //String filepath = folder + outputFilePrefix + fileCount + ".txt";
        
        for (int i = 0; i < entries.size(); i++) {
         
            
        if ((entries.get(i) instanceof File && ((File)entries.get(i)).isFile())) {
         //check is entry is a file and path directory leads to file
        
   
            
            
        try (BufferedReader reader = new BufferedReader(new FileReader((File)entries.get(i)))) {// reads the designated file
            String Line;// this will be string of each line
            BufferedWriter writer = new BufferedWriter(new FileWriter("Split Files/" + outputFilePrefix + fileCount + ".txt"));// writes in the new designated file using prefix

            while ((Line = reader.readLine()) != null) {// in a constant loop
                writer.write(Line);// writes the string line in the loop in a new file
                writer.newLine();// once done printing string go to new line
                lineCount++;// increase the line count to control how many lines are written to each file

                if (lineCount == n) {// if line is equivalent to the max lines in a file
                    writer.close();// close the file writing in
                    fileCount++;// increase the file count
                    lineCount = 0;// reset line count to 0
                    writer = new BufferedWriter(new FileWriter(outputFilePrefix + fileCount + ".txt"));// switch the value of writer to a new so you write in diff file
                }
            }
            writer.close();//closes the writer
            
           newEntries.add(new File("Split Files/" + outputFilePrefix + fileCount + ".txt"));
            
          

        } catch (IOException e) {
            System.out.println("An error occurred while splitting the file: " + e.getMessage());
        }
        }
        
        }
        
        return newEntries;
    }

    
    
//    public ArrayList<File> rename(String suffix, ArrayList<Object> entries){
//       
//       ArrayList<File> ChangedFile = new ArrayList<>();
//       
//        for (int i = 0; i < entries.size(); i++) { 
//            if (entries.get(i) instanceof File && ((File)entries.get(i)).isFile()) {
//            
//                File file = (File) entries.get(i);
//                String prefix = file.getName();
//                String filepath = "Renamed Files/" + prefix + suffix;
//                //File folder = new File("Renamed Files");
//                //File folder = new File("Renamed Files");
//                //folder.mkdirs();//make directory if doesnt exist
//                File renamedFile = new File(filepath);
//                ChangedFile.add(renamedFile);
//               System.out.println(ChangedFile);
//                
//            }
//        }
//        
//        return ChangedFile;
//    }
    
    public ArrayList<File> rename(String suffix, ArrayList<Object> entries) throws IOException{
    ArrayList<File> changedFiles = new ArrayList<>();
    File folder = new File("Renamed Files");
    folder.mkdirs();
    
    for (int i = 0; i < entries.size(); i++) { 
        if (entries.get(i) instanceof File && ((File)entries.get(i)).isFile()) {
            File file = (File) entries.get(i);
            String prefix = file.getName();
            String filepath = "Renamed Files/" + prefix + suffix;
            File renamedFile = new File(filepath);

            
            // Copy the contents of the original file to a new file with the desired name
            try (InputStream is = new FileInputStream(file);
                 OutputStream os = new FileOutputStream(renamedFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                System.out.println("File downloaded successfully to: " + "Renamed Files/" + prefix + suffix);
            } catch (IOException e) {
                System.out.println("Failed to download the file: " + e.getMessage());
            }
            

            
            changedFiles.add(renamedFile);
            System.out.println(changedFiles);
        }
    }
    return changedFiles;
}

    
    
    
    public static void main(String[] args) throws IOException {
         
        ProcessingElement exp = new ProcessingElement();
        int maxLine=10;
        String suffix="_CHANGED";
        ArrayList<Object> entries = new ArrayList<>();
        entries.add(new File("Countries_copy_copy copy.txt"));
        exp.rename(suffix,entries);
        exp.split(maxLine, entries,"Countries_copy_copy copy");
        

        
    }
    
}




/*
 File myfile = new File("Countries_copy_copy copy.txt");
        try {
            Scanner readFile = new Scanner(myfile);
            while (readFile.hasNext()) {
                String words = readFile.nextLine();
                System.out.println(words);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/




