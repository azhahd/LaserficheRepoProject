/***************************************
 * Project: 
 * Programmer: Aris Ariawan
 * Date: Month #, 2023
 * Program: ----.java
 ***************************************/
package com.mycompany.finalproj;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.json.*;


/**
 *
 * @author arisariawan
 */
public class FinalProj {

    public static void main(String[] args) throws FileNotFoundException, IOException {
 
        ArrayList<Object> entryArray = new ArrayList<Object>();
        
         // Read the JSON file into a JSON object
        JSONObject jsonData = new JSONObject(new JSONTokener(new FileReader("Test Scenario3.json")));
        

        JSONArray prossElements = jsonData.getJSONArray("processing_elements");
        
        JSONArray entries = prossElements.getJSONObject(0).getJSONArray("input_entries");
        
        JSONObject inEntry = entries.getJSONObject(0);
        
        
        String entryType = inEntry.getString("type");
        

        //if the entry is local, add the file to the entryArray
        if(entryType.equalsIgnoreCase("local")){
            
            String path = inEntry.getString("path");
            entryArray.add(new File(path));
        
        //if its remote
        }else if(entryType.equalsIgnoreCase("remte")){
            
            //get repoID and entry ID
            String repoID = inEntry.getString("repositoryId");
            int entryID = inEntry.getInt("entryId");

            //add either a File or RepoDirectory to the entry Array
            entryArray.add(repoFile(repoID, entryID));

  
        }
        
        for (int i = 0; i < prossElements.length(); i++) {
            
            JSONObject type = prossElements.getJSONObject(i);
            
            JSONArray paramArray = prossElements.getJSONObject(i).getJSONArray("parameters");
            
            String prossType = type.getString("type").toLowerCase();
            
            switch(prossType){
                
                case "namefilter":
                                        
                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("key")){
                            String key = parameters.getString("value");
                            //update entryArray
                            entryArray = nameFilter(entryArray, key);
                            
                        }
                        
                   }
                         
                    break;
                case "lengthfilter":
                    
                    long length = 0;
                    String operator = null;

                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("length")){
                            length = parameters.getLong("value");
                            System.out.println(length);
                        }
                        
                        if(parameters.getString("name").equalsIgnoreCase("operator")){
                            operator = parameters.getString("value");
                            System.out.println(operator);
                        }
                        
                        
                    }
                    
                    //update entryArray
                    entryArray = lengthFilter(entryArray, length, operator);
                    
                    break;
                case "contentfilter":

                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("key")){
                            String key = parameters.getString("value");
                            
                            //update entryArray
                            entryArray = contentFilter(entryArray, key);                           
                        }
                        
                        
                    }                     
    
                    break;    
                case "countfilter":
                    
                    String key = null;
                    int min = 0;

                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("key")){
                            key = parameters.getString("value");
                        }
                        
                        if(parameters.getString("name").equalsIgnoreCase("min")){
                            min = parameters.getInt("value");
                        } 
                        
                    }       
                    
                    //update entryArray, call countFilter method
                    entryArray = countFilter(entryArray, key, min);                    
   
                    break;
                case "split":
                   
                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("lines")){
                            int lines = parameters.getInt("value");
                        }
                        
                        
                    }                        

                    break;    
                case"list":
                         
                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("max")){
                            int max = parameters.getInt("value");
                            
                            //update entryArray
                            entryArray = List(entryArray, max);                            
                        }
                        
                   }
      
                    break;
                case"rename":
                    
                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("suffix")){
                            String suffix = parameters.getString("value");
                        }
                        
                   }
                    
                    
                   
                    break;    
                case"print":
                    
                    for(Object entry: entries)
                    {
                        if (entry instanceof File file) 
                        {
                            file.getName();
                            file.length();
                            file.getPath();
                        }
                        
                        else if(entry instanceof RepoDirectory repoDirectory)
                        {
                            repoDirectory.getEntryId();
                            repoDirectory.getDirectoryName();
                            repoDirectory.getPath();
                        }
                    }
                    break;     
            }
        }
        
        System.out.println(entryArray);

    }


    public static Object repoFile(String repoID, int entryID){
    
        String servicePrincipalKey = "GvWi0AvTLiKfuM_o37OE";
       
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiMGIyYTE1NWEtMjNlMC00ZDFjLWJlYzktY2NiNDM2Y2RmYTQ3IiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIlZkZ0tCR3Jrd3BfOHpUYTZXOFNncjF6MEdneUJRNWI0Q2FKcjJQYlo1X1EiLAoJCSJ4IjogIjlreE5hNE1vYXlkOTRFZTdUT2hfeXE0ZlZlMDJCNXFsYWJJeHBCOG1qX0UiLAoJCSJ5IjogIld3bjdLMDdhTmxhSU5nSGZ0VVRzbWxyMElCTmE0RFB1ZTIwVzNpcFFxLXMiLAoJCSJkIjogIkhQcjNfZm9YQ1pEX01hUHAwWVlwNDJwbTNEOXRmQk9HdmxOXzBsclB3WkUiLAoJCSJpYXQiOiAxNjc3Mjk3OTMzCgl9Cn0=";
        
        String repositoryId = repoID;
       
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
            servicePrincipalKey, accessKey);
    
        
        Entry entryDownload = client.getEntriesClient()
                .getEntry(repositoryId, entryID, null).join();
        
        final String FILE_NAME = entryDownload.getName();

        Consumer<InputStream> consumer = inputStream -> {
            
            //download files to a folder called entries in program folder
            String filePath = "entries/" + FILE_NAME;
            File folder = new File("entries");
            folder.mkdirs();
            File exportedFile = new File(filePath);
            
            
            try (FileOutputStream outputStream = new FileOutputStream(exportedFile)) {
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //try to download file and return file
        try{
        client.getEntriesClient()
            .exportDocument(repositoryId, entryID, null, consumer)
            .join();
        
        
            client.close();
            return new File("entries/" + FILE_NAME);
        //catch if not file, return RepoDirectory object
        }catch(Exception E){
            
            client.close();
            return new RepoDirectory(repoID, entryID);
        }
        
              
}
    public static ArrayList List(ArrayList<Object> entries, int max) throws IOException{
        
        ArrayList<Object> newEntries = new ArrayList<>();
        
        for (int i = 0; i < entries.size(); i++) {
            
            int fileCount = 0;
            
            //if it is a local directory
            if(entries.get(i) instanceof File && 
                    ((File) entries.get(i)).isDirectory()){
                
                File directory = (File) entries.get(i);
                
                //put files in directory into array
                File[] files = directory.listFiles();
                
                for (int j = 0; j < files.length; j++) {
                    
                    //add max amount of files to newEntries arrayList
                    if(files[i].isFile() && fileCount <= max){
                        newEntries.add(files[i]);
                        fileCount++;
                    }
                    
                    //break when max number has been reached
                    if(fileCount > max ){
                        break;
                    }
                    
                }
             
            //if it is a repoDirectory
            }else if(entries.get(i) instanceof RepoDirectory repoDirect){
                
                
                ArrayList<File> repoFiles = dlFromRepoDirectory(repoDirect.getRepoId(), repoDirect.getEntryId(), max);
                newEntries.addAll(repoFiles);
            }
            
        }
        
        
        return newEntries;
        
    }
    

    
    public static ArrayList dlFromRepoDirectory(String repoID, int entryID, int max){
        
        ArrayList<File> filesFromRepoDirect = new ArrayList<>();
        
        String servicePrincipalKey = "GvWi0AvTLiKfuM_o37OE";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiMGIyYTE1NWEtMjNlMC00ZDFjLWJlYzktY2NiNDM2Y2RmYTQ3IiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIlZkZ0tCR3Jrd3BfOHpUYTZXOFNncjF6MEdneUJRNWI0Q2FKcjJQYlo1X1EiLAoJCSJ4IjogIjlreE5hNE1vYXlkOTRFZTdUT2hfeXE0ZlZlMDJCNXFsYWJJeHBCOG1qX0UiLAoJCSJ5IjogIld3bjdLMDdhTmxhSU5nSGZ0VVRzbWxyMElCTmE0RFB1ZTIwVzNpcFFxLXMiLAoJCSJkIjogIkhQcjNfZm9YQ1pEX01hUHAwWVlwNDJwbTNEOXRmQk9HdmxOXzBsclB3WkUiLAoJCSJpYXQiOiAxNjc3Mjk3OTMzCgl9Cn0=";
        String repositoryId = repoID;
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        // Get information about the child entries of the Root entry
        ODataValueContextOfIListOfEntry result = client
                .getEntriesClient()
                .getEntryListing(repositoryId, entryID, true, null, null, null, null, null, "name", null, null, null).join();
        List<Entry> entries = result.getValue();
        
        //create ArrayList to store all entryIDs in directory
        ArrayList<Integer> entryIDArray = new ArrayList<>();
        
        //add x amount entryIDs in arrayList
        for (int i = 0; i < max; i++) {
            
            //only add if i is less than entries size
            if(i<entries.size())
                entryIDArray.add(entries.get(i).getId());
            else
                break;
        }
        
        //download files and add them to new ArrayList  
        for (int i = 0; i < entryIDArray.size(); i++) {
            
            Object repoFileLF = repoFile(repoID, entryIDArray.get(i));
            
            if(repoFileLF instanceof File)
                filesFromRepoDirect.add((File)repoFileLF);
            
        }
        
        client.close();
        
        //return ArrayList of files in directory
        return filesFromRepoDirect;
   
    }
    
    /**
     * Aizah
     * @param entries
     * @param key
     * @return 
     */
    public static ArrayList<Object> nameFilter(ArrayList<Object> entries, String key) {
        
        //create arrayList to store sublist of entries
        ArrayList<Object> sublist = new ArrayList<>();
        
        //go through each entry
        for (Object entry : entries) {
            
            //if it is of type file (local)
            if (entry instanceof File) {
                File afile = (File) entry;
                String fileName = afile.getName();
                
                //compare file or directory name to key
                if (fileName.toLowerCase().contains(key.toLowerCase())) {
                    
                    //add file to sublist if it contains key
                    sublist.add(afile);
                }
            //if it is a repoDirectory
            }else if(entry instanceof RepoDirectory){
                
                RepoDirectory repoDirect = (RepoDirectory) entry;
                
                //connect to client and check name of folder, compare to key
                if(repoDirect.getDirectoryName().toLowerCase().contains(key.toLowerCase())){
                    
                    //add repoDirectory to list if it contains key
                    sublist.add(repoDirect);
                }

                
            }
                
        }
        
        //return sublist
        return sublist;
    }
    
    
    /**
     * Apoorva
     * @param entries
     * @param key
     * @param min
     * @return
     * @throws IOException 
     */
    public static ArrayList<Object> countFilter(ArrayList<Object> entries, String key, int min) throws IOException {
        ArrayList<Object> filteredEntries = new ArrayList<Object>();
        
        for (Object entry : entries) {
            if (entry instanceof File && ((File)entry).isFile()) {
                
                BufferedReader reader = new BufferedReader(new FileReader((File)entry));
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
    
    /**
     * Apoorva
     * @param str
     * @param substr
     * @return 
     */
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
    
    
    /**
     * Taya
     * @param entries
     * @param key
     * @return 
     */
    public static ArrayList<Object> contentFilter(ArrayList<Object> entries, String key){
        ArrayList<Object> sublist = new ArrayList<>();
        
        for(Object afile : entries){
            
            if(afile instanceof File && ((File) afile).isFile()){
            
                try(BufferedReader input = new BufferedReader(new FileReader((File) afile))){
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
        }   
        return sublist;
    }
    
    /**
     * Taya
     * @param entries
     * @param length
     * @param Operator
     * @return 
     */
    public static ArrayList<Object> lengthFilter(ArrayList<Object> entries, long length, String Operator){
        ArrayList<Object> sublist = new ArrayList<>();
        
        
                
        for(Object afile : entries){
            
            
            if(afile instanceof File && ((File) afile).isFile()){
                long file_len = ((File)afile).length();
                

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
        }
        
        return sublist;
       
    }
    
//    public ArrayList<Object> Print(ArrayList<Object> entries){
//        
//        for (Object entry: entries) {
//            
//            if(entry instanceof File){
//                
//                System.out.println("Name: " + ((File) entry).getName() + 
//                "Length: " +  (((File) entry).length()) + "Path: " + ((File) entry).getPath());
//                
//            }
//            
//            
//        }
//        
//        
//        return entries;
//    }
    
    


        
    }
    
 


    