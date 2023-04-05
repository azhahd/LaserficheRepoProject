/** *************************************
 * Project:
 * Programmer: Aris Ariawan
 * Date: Month #, 2023
 * Program: ----.java
 ************************************** */
package com.mycompany.finalproj;

import com.laserfiche.api.client.model.AccessKey;
import com.laserfiche.repository.api.RepositoryApiClient;
import com.laserfiche.repository.api.RepositoryApiClientImpl;
import java.io.File;

/**
 *
 * @author arisariawan
 */
public class RemoteFile{
    
    private File fileobj;
    private String repoId;
    private int entryId;

    public RemoteFile(File fileobj, String repoId, int entryId) {
        this.fileobj = fileobj;
        this.repoId = repoId;
        this.entryId = entryId;
    }

    public File getFileobj() {
        return fileobj;
    }

    public void setFileobj(File fileobj) {
        this.fileobj = fileobj;
    }

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
    
    public String getName(){
        return fileobj.getName();
    }
        
    public long getLength(){
        return fileobj.length();
    }

    @Override
    public String toString() {
        return "RemoteFile{" + "fileobj=" + fileobj + ", repoId=" + repoId + ", entryId=" + entryId + '}';
    }
    
    
    public String getPath(){

        String servicePrincipalKey = "GvWi0AvTLiKfuM_o37OE";
        String accessKeyBase64 = "ewoJImN1c3RvbWVySWQiOiAiMTQwMTM1OTIzOCIsCgkiY2xpZW50SWQiOiAiMGIyYTE1NWEtMjNlMC00ZDFjLWJlYzktY2NiNDM2Y2RmYTQ3IiwKCSJkb21haW4iOiAibGFzZXJmaWNoZS5jYSIsCgkiandrIjogewoJCSJrdHkiOiAiRUMiLAoJCSJjcnYiOiAiUC0yNTYiLAoJCSJ1c2UiOiAic2lnIiwKCQkia2lkIjogIlZkZ0tCR3Jrd3BfOHpUYTZXOFNncjF6MEdneUJRNWI0Q2FKcjJQYlo1X1EiLAoJCSJ4IjogIjlreE5hNE1vYXlkOTRFZTdUT2hfeXE0ZlZlMDJCNXFsYWJJeHBCOG1qX0UiLAoJCSJ5IjogIld3bjdLMDdhTmxhSU5nSGZ0VVRzbWxyMElCTmE0RFB1ZTIwVzNpcFFxLXMiLAoJCSJkIjogIkhQcjNfZm9YQ1pEX01hUHAwWVlwNDJwbTNEOXRmQk9HdmxOXzBsclB3WkUiLAoJCSJpYXQiOiAxNjc3Mjk3OTMzCgl9Cn0=";
	String repositoryId = repoId;
        AccessKey accessKey = AccessKey.createFromBase64EncodedAccessKey(accessKeyBase64);

        RepositoryApiClient client = RepositoryApiClientImpl.createFromAccessKey(
                servicePrincipalKey, accessKey);

        // Get information about the ROOT entry, i.e. entry with ID=1
        int rootEntryId = entryId;
        com.laserfiche.repository.api.clients.impl.model.Entry entry = client.getEntriesClient()
                .getEntry(repositoryId, rootEntryId, null).join();
            
        
        client.close(); 
        
  
        return entry.getFullPath();
    }
    
    
    
}
