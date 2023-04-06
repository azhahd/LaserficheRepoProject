/***************************************
 * Project: File Processing System
 * Programmer: Aris, Taya, Daniel, Aizah, Apoorva
 * Date: April, 2023
 * Program: FinalProj.java
 ***************************************/
package com.mycompany.finalproj;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import com.laserfiche.repository.api.clients.impl.model.Entry;
import com.laserfiche.repository.api.clients.impl.model.ODataValueContextOfIListOfEntry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import java.io.FileReader;
import java.io.FileWriter;
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
    
    /**
     * @author Aris
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
 
        //create ArrayList to store input entries
        ArrayList<Object> entryArray = new ArrayList<>();
        
        //read JSON file into JSON object
        JSONObject jsonData = new JSONObject(new JSONTokener(new FileReader("example.json")));
        
        //get processing element body from JSON object
        JSONArray prossElements = jsonData.getJSONArray("processing_elements");
        
        //get input entries from the processing element
        JSONArray entries = prossElements.getJSONObject(0).getJSONArray("input_entries");
        
        //get body of input entries
        JSONObject inEntry = entries.getJSONObject(0);
        
        //get type of input entry
        String entryType = inEntry.getString("type");
        

        //if the entry is local, add the File to the entryArray
        if(entryType.equalsIgnoreCase("local")){
            
            String path = inEntry.getString("path");
            entryArray.add(new File(path));
        
        //if its remote
        }else if(entryType.equalsIgnoreCase("remte")){
            
            //get repoID and entry ID
            String repoID = inEntry.getString("repositoryId");
            int entryID = inEntry.getInt("entryId");

            //add either a RemoteFile or RepoDirectory to the entry Array
            entryArray.add(repoFile(repoID, entryID));

        }
        
        //loop through processing elements
        for (int i = 0; i < prossElements.length(); i++) {
            
            //get current processing element
            JSONObject type = prossElements.getJSONObject(i);
            
            //store parameters in JSONArray
            JSONArray paramArray = prossElements.getJSONObject(i).getJSONArray("parameters");
            
            //get type of processing element
            String prossType = type.getString("type").toLowerCase();
           
            //swtich type of processing elements
            switch(prossType){
                
                case "namefilter":
                    
                    //loop through parameters
                    for (int j = 0; j < paramArray.length(); j++) {                      
                        
                        JSONObject parameters = paramArray.getJSONObject(j);    
                        if(parameters.getString("name").equalsIgnoreCase("key")){
                            
                            String key = parameters.getString("value");
                           
                            //update entryArray by filtering based on name
                            entryArray = nameFilter(entryArray, key);
                            
                        } 
                   }           
                    break;
                    
                case "lengthfilter":
                    
                    long length = 0;
                    String operator = null;

                    //loop through parameters
                    for (int j = 0; j < paramArray.length(); j++) {
                          
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("length")){
                            length = parameters.getLong("value");                          
                        }
 
                        if(parameters.getString("name").equalsIgnoreCase("operator")){
                            operator = parameters.getString("value");                       
                        }
 
                    }
                    
                    //update entryArray by filtering based on length
                    entryArray = lengthFilter(entryArray, length, operator);
                    break;
                    
                case "contentfilter":
                    
                    //loop through parameters
                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("key")){
                            String key = parameters.getString("value");
                            
                            //update entryArray by filtering based on content
                            entryArray = contentFilter(entryArray, key);                           
                        }
                        
                    }                     
                    break;   
                    
                case "countfilter":
                    
                    String key = null;
                    int min = 0;
                    
                    //loop through parameters
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
                   
                    //loop through parameters
                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("lines")){
                            int lines = parameters.getInt("value");
                            
                            //update entryArray, call split
                            entryArray = split(lines, entryArray);
                        }
                                      
                    }                        

                    break;    
                case"list":
                    
                    //loop through parameters
                    for (int j = 0; j < paramArray.length(); j++) {
                        
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("max")){
                            int max = parameters.getInt("value");
                            
                            //update entryArray, call list
                            entryArray = List(entryArray, max);                            
                        }
                        
                   }
      
                    break;
                case"rename":
                    
                    //loop through parameters
                    for (int j = 0; j < paramArray.length(); j++) {
                                          
                        JSONObject parameters = paramArray.getJSONObject(j);
                        
                        if(parameters.getString("name").equalsIgnoreCase("suffix")){
                            String suffix = parameters.getString("value");
                            
                            //update entryArray, call rename
                            entryArray = rename(suffix, entryArray);
                        }
                        
                   }

                    break;    
                case"print":
                    
                    //call print method
                    entryArray = Print(entryArray);
                    break;     
            }          
        }    
        
    }//end of main method


    /**
     * @author - Aris
     * @param repoID - repositoryID
     * @param entryID - entryID
     * @return - RemoteFile or RepoDirectory
     */
    public static Object repoFile(String repoID, int entryID){
    
        //connect to laserFiche repository
        String servicePrincipalKey = "GvWi0AvTLiKfuM_o37OE";
       
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiMGIyYTE1NWEtMjNlMC00ZDFjLWJlYzktY2NiNDM2Y2RmYTQ3IiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIlZkZ0tCR3Jrd3BfOHpUYTZXOFNncjF6MEdneUJRNWI0Q2FKcjJQYlo1X1EiLAoJCSJ4IjogIjlreE5hNE1vYXlkOTRFZTdUT2hfeXE0ZlZlMDJCNXFsYWJJeHBCOG1qX0UiLAoJCSJ5IjogIld3bjdLMDdhTmxhSU5nSGZ0VVRzbWxyMElCTmE0RFB1ZTIwVzNpcFFxLXMiLAoJCSJkIjogIkhQcjNfZm9YQ1pEX01hUHAwWVlwNDJwbTNEOXRmQk9HdmxOXzBsclB3WkUiLAoJCSJpYXQiOiAxNjc3Mjk3OTMzCgl9Cn0=";
        
        String repositoryId = repoID;
       
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
            servicePrincipalKey, accessKey);
    
        //get entry to download
        Entry entryDownload = client.getEntriesClient()
                .getEntry(repositoryId, entryID, null).join();
        
        //get name of the file/folder
        final String FILE_NAME = entryDownload.getName();

        Consumer<InputStream> consumer = inputStream -> {
            
            //create a directory for original entries
            String filePath = "entries/" + FILE_NAME;
            File folder = new File("entries");
            folder.mkdirs();
            
            //create file object for downloaded file
            File exportedFile = new File(filePath);
            
            //try to write the file
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

        //try to download file and return Remotefile
        try{
        client.getEntriesClient()
            .exportDocument(repositoryId, entryID, null, consumer)
            .join();
        
            //close client
            client.close();
            
            //return remoteFile
            RemoteFile remFile = new RemoteFile(new File("entries/" + FILE_NAME), repoID, entryID);
            return remFile;
        //catch if not file, return RepoDirectory object
        }catch(Exception E){
            
            //close client
            client.close();
            
            //return repoDirectory
            return new RepoDirectory(repoID, entryID);
        }
        
    }//end of repoFile
    
    /**
     * @author Aris
     * @param entries - ArrayList of entries
     * @param max - max number of files to download from directory
     * @return - ArrayList of entries
     * @throws IOException 
     */
    public static ArrayList List(ArrayList<Object> entries, int max) throws IOException{
        
        //create ArrayList to store new Entries
        ArrayList<Object> newEntries = new ArrayList<>();
        
        //loop through entries
        for (int i = 0; i < entries.size(); i++) {
            
            int fileCount = 0;
            
            //if it is a local directory
            if(entries.get(i) instanceof File && 
                    ((File) entries.get(i)).isDirectory()){
                
                //store path into File object
                File directory = (File) entries.get(i);              
                
                //put files in directory into array
                File[] files = directory.listFiles();
                
                //loop through files in directory
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
                
                //call dlFromRepoDirectory
                ArrayList<File> repoFiles = dlFromRepoDirectory(repoDirect.getRepoId(), repoDirect.getEntryId(), max);
                
                //add files downloaded to newEntries
                newEntries.addAll(repoFiles);
            }
            
        }
        
        //return newEntries
        return newEntries;
        
    }//end of List
    

    /**
     * @author Aris
     * @param repoID - repository ID
     * @param entryID - Entry ID
     * @param max - max number of entries to download from directory
     * @return - arrayList of files from directory
     */
    public static ArrayList dlFromRepoDirectory(String repoID, int entryID, int max){
        
        //create ArrayList to store files downloaded from repo
        ArrayList<Object> filesFromRepoDirect = new ArrayList<>();
        
        //connect to LaserFiche repository
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
        
        //add max amount entryIDs in arrayList
        for (int i = 0; i < max; i++) {
            
            //only add if i is less than entries size
            if(i<entries.size())    
                entryIDArray.add(entries.get(i).getId());
            else
                break;
        }
        
        //download files and add them to new ArrayList  
        for (int i = 0; i < entryIDArray.size(); i++) {
            
            //call repoFile method to make RemoteFile object
            Object repoFileLF = repoFile(repoID, entryIDArray.get(i));
            
            //add Remotefile to array
            filesFromRepoDirect.add(repoFileLF);
            
            
        }
        
        //close client
        client.close();
        
        //return ArrayList of files in directory
        return filesFromRepoDirect;
   
    }//end of dlFromRepoDirectory
    
    /**
     * Aizah
     * @param entries
     * @param key
     * @return 
     */
    public static ArrayList<Object> nameFilter(ArrayList<Object> entries, String key) {
        
        //create arrayList to store sublist of entries
        ArrayList<Object> sublist = new ArrayList<>(); //create array list for filtered 
        
        //go through each entry
        for (Object entry : entries) { //start for loop and iterate through each entry
            
            //if it is of type file
            if (entry instanceof File) { //if entry is a local file
                File afile = (File) entry;
                String fileName = afile.getName(); //get name
                
                //compare file or directory name to key
                if (fileName.toLowerCase().contains(key.toLowerCase())) { //change filename to lower case so it doesn't matter and scan for the key
                    
                    //add file to sublist if it contains key
                    sublist.add(afile); //add file to filtered array
                }
            //if it is a repoDirectory
            }else if(entry instanceof RemoteFile){ //but if it's a remote file
                
                if(((RemoteFile) entry).getName().contains(key.toLowerCase())){ //change case of file name find key
                    
                    //add file to sublist if it contains key
                    sublist.add(entry); // add remote file to sorted array list
                }
                     
            }else if(entry instanceof RepoDirectory){ //if its a directory 
                
                RepoDirectory repoDirect = (RepoDirectory) entry; //make it an entry
                
                //connect to client and check name of folder, compare to key
                if(repoDirect.getDirectoryName().toLowerCase().contains(key.toLowerCase())){ //switch case and check for key
                    
                    //add repoDirectory to list if it contains key
                    sublist.add(repoDirect); //add to filtered array
                }

            }
                
        }
        
        //return sublist
        return sublist; //return the whole array
    }
    
    
    /**
     * Apoorva
     * @param entries
     * @param key
     * @param min
     * @return
     * @throws IOException 
     */
    public static ArrayList<Object> countFilter(ArrayList<Object> entries, String key, int min) throws IOException 
    {
        /*countFilter takes parameters: arraylist of file objects, a string key, a integer min 
        value greater than 0*/
        
        //A new arraylist of file objects is initialised and anmes filteredEntries
        ArrayList<Object> filteredEntries = new ArrayList<>();
        
        BufferedReader reader = null;
        
        //for loop iterates over each file object in the entries list
        for (Object entry : entries) 
        {
            //checks if entry object is an instance of the file class and if the file is not a directory
            //also checks if it is an instance of the Remote file class
            if ((entry instanceof File && ((File)entry).isFile()) ||
                    entry instanceof RemoteFile) 
            {
                
                //checks if entry object is an instance of the file class
                if(entry instanceof File file)
                {
                    reader = new BufferedReader(new FileReader(file));//initialised to a new file reader
                }
                else
                {
                    //initialised to a new FileReader object created using the File object retrieved from the remoteFile object
                    reader = new BufferedReader(new FileReader(((RemoteFile)entry).getFileobj()));
                }
               
                String line; //used to read the lines of the text
                
                int count = 0; //count set to 0
                
                //while loop to read each line of the file -- using the readLine() method
                while ((line = reader.readLine()) != null) //checks if the line is not null
                {
                    /*as the line isn't NULL --> the countSubstring() function counts the 
                    occurences of the key string in that line --> and this value is added 
                    to the count variable */
                    count += countSubstring(line.toLowerCase(), key.toLowerCase());
                }
                
                reader.close(); //once the file is completely read, the Buffered reader object is closed
                
                if (count >= min) //checks if count variable is greater than the min 
                {
                    filteredEntries.add(entry); //because it is, the entry object is added to the filtered list array list
                }
            }
        }
        
        return filteredEntries; //returning the arraylist
    }
    
    /**
     * Apoorva
     * @param str
     * @param substr
     * @return 
     */
    
    //count substring takes 2 parameters and it returns an integer value, count
    private static int countSubstring(String str, String substr) 
    {
        //counts the occurences of the substring
        int count = 0; //count is set to 0
        
        int lastIndex = 0;//keeps track of the last position in str where ever the match is found
        
        //while loop continues until there are no more matches of substr in str
        while (lastIndex != -1) 
        {
            //checks if there is a match of substr in str starting from lastIndex
            //if there is a match --> index of the first occurence is returned and stored in lastIndex
            lastIndex = str.indexOf(substr, lastIndex);
            //if no match then -1 is returned
            
            //if loop - checks if match is found
            if (lastIndex != -1) 
            {
                count++; //count var incremented by 1
                lastIndex += substr.length(); //updates the lastIndex var to the position after current match
            }
        }
        //while loop happens until there are no more matches of substr in str
        
        //the num of times substr appears in str
        return count;//count is returned
    }
    
    
    /**
     * @author Taya
     * @param entries
     * @param key
     * @return - ArrayList of entries
     */
    public static ArrayList<Object> contentFilter(ArrayList<Object> entries, String key) throws FileNotFoundException{
        //ArrayList to hold the files that contain the key
        ArrayList<Object> sublist = new ArrayList<>();
        BufferedReader inFile = null;
        
        //Search through the files in the list of entries
        for(Object afile : entries){
            //detect whether the current object is a file 
            if((afile instanceof File && ((File) afile).isFile()) ||
                    afile instanceof RemoteFile){
                //make file readers
                if(afile instanceof File){
                    inFile = new BufferedReader(new FileReader((File) afile));
                }else if(afile instanceof RemoteFile){
                    inFile = new BufferedReader(new FileReader(((RemoteFile)afile).getFileobj()));
                }
                
                try(BufferedReader input = inFile){
                    //go through the file line by line 
                    while (true){
                        String line = input.readLine();
                        //break loop when end of file
                        if (line == null){
                            break;
                        }
                        //check if the line contains the key, and add the file to the ArrayList if it does and break out of the loop
                        else if(line.toLowerCase().contains(key.toLowerCase())){
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
     * @author - Taya
     * @param entries
     * @param length
     * @param Operator
     * @return ArrayList of entries
     */
    public static ArrayList<Object> lengthFilter(ArrayList<Object> entries, long length, String Operator){
        ArrayList<Object> sublist = new ArrayList<>();
        
        long file_len = 0;
        
                
        for(Object afile : entries){
            // go through each instance of the list of entries, check if the object is a file and get the length of the file
            if((afile instanceof File && ((File) afile).isFile()) || 
                    (afile instanceof RemoteFile)){
                
                if(afile instanceof File){                  
                    file_len = ((File)afile).length();
                }
                else if(afile instanceof RemoteFile){
                    file_len = ((RemoteFile) afile).getLength();
                }
                
                switch (Operator) {
                    //check if the length of the file is equal to the value of length parameter, if yes then add the file to the sublist
                    case "EQ" -> {
                        if(file_len == length){
                            sublist.add(afile);
                        }
                    }
                    //check if the length of the file is not equal to the value of length parameter, if yes then add the file to the sublist
                    case "NEQ" -> {
                        if(file_len != length){
                            sublist.add(afile);
                        }
                    }
                    //check if the length of the file is greater than the value of length parameter, if yes then add the file to the sublist
                    case "GT" -> {
                        if(file_len > length){
                            sublist.add(afile);
                        }
                    }
                    //check if the length of the file is greater than or equal to the value of length parameter, if yes then add the file to the sublist
                    case "GTE" -> {
                        if(file_len >= length){
                            sublist.add(afile);
                        }
                    }
                    //check if the length of the file is less than the value of length parameter, if yes then add the file to the sublist
                    case "LT" -> {
                        if(file_len < length){
                            sublist.add(afile);
                        }
                    }
                    //check if the length of the file is less than or equal to the value of length parameter, if yes then add the file to the sublist
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
    
    /**
     * Daniel
     * @param n
     * @param entries
     * @param outputFilePrefix
     * @return
     * @throws IOException 
     */
    public static ArrayList split(int n, ArrayList<Object> entries) throws IOException {//input #lines as n && ArrayList of etries//returns new arraylist 
        n = Math.abs(n);// As long as n>0
        ArrayList<Object> newEntries = new ArrayList<>();//creates new arraylist
        
        File folder = new File("Split Files");//this creates Split downloads in the 
        folder.mkdirs();//creates the directory if doesnt exist
        
        File readFile = null;//intialize
   
        for (int i = 0; i < entries.size(); i++) {//for each entry
            int fileCount = 1;// original file
            int lineCount = 0;// count lines
           
        //check is remote && local file
        if ((entries.get(i) instanceof File && ((File)entries.get(i)).isFile()) ||
                entries.get(i) instanceof RemoteFile) {
         
         
         if(entries.get(i) instanceof File){
             readFile = (File) entries.get(i);
         }else{
             readFile = ((RemoteFile) entries.get(i)).getFileobj();
         }
                   
        try (BufferedReader reader = new BufferedReader(new FileReader(readFile))) {// reads the designated file
            String Line;// this will be string of each line
            BufferedWriter writer = new BufferedWriter(new FileWriter("Split Files/" + readFile.getName() + ".part" + fileCount + ".txt"));// writes in the new designated file using prefix

            while ((Line = reader.readLine()) != null) {// in a constant loop
                writer.write(Line);// writes the string line in the loop in a new file
                writer.newLine();// once done printing string go to new line
                lineCount++;// increase the line count to control how many lines are written to each file

                if (lineCount == n) {// if line is equivalent to the max lines in a file
                    writer.close();// close the file writing in
                    lineCount = 0;// reset line count to 0

                    newEntries.add(new File("Split Files/" + readFile.getName() + ".part" + fileCount + ".txt"));
                    fileCount++;// increase the file count
                    writer = new BufferedWriter(new FileWriter("Split Files/" + readFile.getName() + ".part" + fileCount + ".txt"));// switch the value of writer to a new so you write in diff file

                }
            }
            writer.close();//closes the writer
            
        } catch (IOException e) {
            System.out.println("An error occurred while splitting the file: " + e.getMessage());//error message if try does not work
        }
        }else{
            newEntries.add(entries.get(i));
        }
        
        }
        
        return newEntries;//return the array of new entries
    }
    
    
    
    /**
     * Daniel
     * @param suffix
     * @param entries
     * @return
     * @throws IOException 
     */
    public static ArrayList<Object> rename(String suffix, ArrayList<Object> entries) throws IOException {//input string SUFFIX and arraylist of entries//outputs newArraylist
        ArrayList<Object> changedFiles = new ArrayList<>();//new ArrayList
        
        //creates new folder RENAMED FILES and creates the directory if doesnt exist
        File folder = new File("Renamed Files");
        folder.mkdirs();
        
        File file = null;//initialize make sure theres something there
    
        for (int i = 0; i < entries.size(); i++) { //check all entries
            
            
            if ((entries.get(i) instanceof File && ((File)entries.get(i)).isFile() ||
                    entries.get(i) instanceof RemoteFile)) {//if file leads to file or leads to remote file
                
                if(entries.get(i) instanceof File){//what type of file is it? this local file
                    file = (File) entries.get(i);
                }else{//must be remote
                    file = ((RemoteFile) entries.get(i)).getFileobj();//calls the remoteFile class gets file from repo
                }   
                    
                String prefix = file.getName();//calls remoteFile class to get the base assign to string
                String filepath = "Renamed Files/" + prefix + suffix;//creates string for file path with the updated named by including the base and the inputed suffix
                File renamedFile = new File(filepath);//now that we created string now we have to make the file

            try (FileInputStream fis = new FileInputStream(file);//initialize the file output and file input
                 FileOutputStream fos = new FileOutputStream(renamedFile)) {

                byte[] buffer = new byte[1024];//initializes array called buffer size of array
                int bytesRead;//
                while ((bytesRead = fis.read(buffer)) != -1) {//reads data from input stream 
                    fos.write(buffer, 0, bytesRead);//writes to file output stream of array 
                }
                System.out.println("File copied successfully to: " + renamedFile.getAbsolutePath());
                changedFiles.add(renamedFile);
            } catch (IOException e) {
                System.out.println("Failed to copy the file: " + e.getMessage());
            }

        }else{
                changedFiles.add(entries.get(i));
            }
    }

    return changedFiles;//return the new arrayList
}
    
    /**
     * @author Aizah
     * @param entries - ArrayList of entries
     * @return - list of entries
     */
    public static ArrayList<Object> Print(ArrayList<Object> entries){
        
        for(Object entry: entries)
        {
            //if entry is local, print info
            if (entry instanceof File file) 
            {     
                System.out.print("Name: " + file.getName());
                System.out.print("; Length: " + file.length());
                System.out.print("; Path: " + file.getPath());
                System.out.println();
            }
            
            //if entry is a remote directory
            else if(entry instanceof RepoDirectory repoDirectory)
            {
                System.out.print("Name: " + repoDirectory.getDirectoryName());
                System.out.print("; Entry ID: " + repoDirectory.getEntryId());
                System.out.print("; Path: " + repoDirectory.getPath());
                System.out.println();
            }
            
            //if entry is a remote file
            else if(entry instanceof RemoteFile remoteFile){
                
                System.out.print("Name: "  + remoteFile.getName());
                System.out.print("; Entry ID: "  + remoteFile.getEntryId());
                System.out.print("; Repo ID: "  + remoteFile.getRepoId());
                System.out.print("; Path: "  + remoteFile.getPath());
                System.out.print("; Length: " + remoteFile.getLength());
                System.out.println();
 
            }
        }
        
        //return same entries as input
        return entries;
    
    }//end of Print
    
      
}//end of class
    
 


    