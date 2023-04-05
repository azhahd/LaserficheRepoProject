/** *************************************
 * Project: Remote directory class
 * Programmer: Aris Ariawan
 * Date: April, 2023
 * Program: RepoDirectory.java
 ************************************** */
package com.mycompany.finalproj;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;

/**
 *
 * @author arisariawan
 */
public class RepoDirectory {
    
    //fields
    private String repoId;
    private int entryId;

    //constructor
    public RepoDirectory(String repoId, int entryId) {
        this.repoId = repoId;
        this.entryId = entryId;
    }

    //getters and setters
    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
    
    /**
     * getDirectoryName()
     * @return - String of directory name
     */
    public String getDirectoryName(){
        
        //connect to repository
        String servicePrincipalKey = "GvWi0AvTLiKfuM_o37OE";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiMGIyYTE1NWEtMjNlMC00ZDFjLWJlYzktY2NiNDM2Y2RmYTQ3IiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIlZkZ0tCR3Jrd3BfOHpUYTZXOFNncjF6MEdneUJRNWI0Q2FKcjJQYlo1X1EiLAoJCSJ4IjogIjlreE5hNE1vYXlkOTRFZTdUT2hfeXE0ZlZlMDJCNXFsYWJJeHBCOG1qX0UiLAoJCSJ5IjogIld3bjdLMDdhTmxhSU5nSGZ0VVRzbWxyMElCTmE0RFB1ZTIwVzNpcFFxLXMiLAoJCSJkIjogIkhQcjNfZm9YQ1pEX01hUHAwWVlwNDJwbTNEOXRmQk9HdmxOXzBsclB3WkUiLAoJCSJpYXQiOiAxNjc3Mjk3OTMzCgl9Cn0=";
	String repositoryId = repoId;
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        // Get information about the entry
        int rootEntryId = entryId;
        com.laserfiche.repository.api.clients.impl.model.Entry entry = client.getEntriesClient()
                .getEntry(repositoryId, rootEntryId, null).join();
            
        
        //close client
        client.close(); 
        
        
        //return name
        return entry.getName();
    }
    
    /**
     * getPath()
     * @return - String of directory path
     */
    public String getPath(){
        
        //connect to repository
        String servicePrincipalKey = "GvWi0AvTLiKfuM_o37OE";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiMGIyYTE1NWEtMjNlMC00ZDFjLWJlYzktY2NiNDM2Y2RmYTQ3IiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIlZkZ0tCR3Jrd3BfOHpUYTZXOFNncjF6MEdneUJRNWI0Q2FKcjJQYlo1X1EiLAoJCSJ4IjogIjlreE5hNE1vYXlkOTRFZTdUT2hfeXE0ZlZlMDJCNXFsYWJJeHBCOG1qX0UiLAoJCSJ5IjogIld3bjdLMDdhTmxhSU5nSGZ0VVRzbWxyMElCTmE0RFB1ZTIwVzNpcFFxLXMiLAoJCSJkIjogIkhQcjNfZm9YQ1pEX01hUHAwWVlwNDJwbTNEOXRmQk9HdmxOXzBsclB3WkUiLAoJCSJpYXQiOiAxNjc3Mjk3OTMzCgl9Cn0=";
	String repositoryId = repoId;
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        // Get information about the entry
        int rootEntryId = entryId;
        com.laserfiche.repository.api.clients.impl.model.Entry entry = client.getEntriesClient()
                .getEntry(repositoryId, rootEntryId, null).join();
            
        //close client
        client.close(); 
        
        //return path
        return entry.getFullPath();
    }

    @Override
    public String toString() {
        return "RepoDirectory{" + "repoId=" + repoId + ", entryId=" + entryId + ", Name:" + getDirectoryName() + '}';
    }
    
    
}
