package project.DataBridge.Services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;

@Service
public class DownloadingFile {
	@Value("${firebase.storage.bucket-name}")
    private String bucketName;
		
	@Autowired
	FireBaseConnector connection;
	
	String lastFileContentType="";
	
	public void setContentType(Blob b) {
		// make sure to call the readFile first
		lastFileContentType = b.getContentType();
	}
	
	public String getcontentType() {return lastFileContentType;}
	
	
	
	public Resource readFile(String name) throws IOException {
	    Storage storage = connection.getStorage();
	    String fileName = name;
	    BlobId blobId = BlobId.of(bucketName, fileName);

	    Blob blob = storage.get(blobId);
	    if (blob != null) {
	    	setContentType(blob);
	        byte[] content = blob.getContent();
//	        String contentString = new String(content, UTF_8)
	        System.out.println("Reading : "+name);
	        return new ByteArrayResource(content);
	    } else {
	       
			 BlobId blobId2 = BlobId.of(bucketName, "***LASTFILE***");
			 Blob blob2 = storage.get(blobId2);
			 setContentType(blob2);
			 byte[] content = blob2.getContent();
			 return new ByteArrayResource(content);
	    }
	}
	
	public String getLastFileName() {
		 Storage storage = connection.getStorage();
		 String fileName = "***LASTFILE***";
		 BlobId blobId = BlobId.of(bucketName, fileName);
		 Blob blob = storage.get(blobId);
		 byte[] content = blob.getContent();
	     String contentString = new String(content, UTF_8);
	     return contentString;
	}
	
	private String getName(String str) {
		int start=str.indexOf("name")+5;
		int last =str.indexOf(",",start);
		return str.substring(start, last);
		
	}
	
	private String getSize(String str) {
		int start=str.indexOf("size")+5;
		int last =str.indexOf(",",start);
		return str.substring(start, last);
		
	}
	
	private String getType(String str) {
		int start=str.indexOf("content-type")+13;
		int last =str.indexOf(",",start);
		return str.substring(start, last);
		
	}
	public List<List<String>> listOfBlob(){
		Storage storage = connection.getStorage();
		Iterable<Blob> allfile =storage.list(bucketName).iterateAll();
		List<List<String>> details = new ArrayList<>();
		for(Blob i:allfile) {
			List<String> file = new ArrayList<>();
			String x=i.toString();
			file.add(getName(x));
			file.add(getSize(x));
			file.add(getType(x));
			details.add(file);
		}
		int j=0;
		for(List<String> i :details) {
			if(i.get(0).equals("***LASTFILE***")) {
				break;
			}
			j++;
		}
		details.remove(j);
		
		return details;
		
	}

	public boolean doesFileExists(String fileName) {
		Storage storage = connection.getStorage();
		try {
		 Blob blob = storage.get(bucketName, fileName);
        return Optional.ofNullable(blob).isPresent();
		}catch (Exception e) {return false;}
        		
	}

	// delete method
	public boolean deleteFile(String fileName){
		if(doesFileExists(fileName)){
			Storage storage = connection.getStorage();
			try{
				BlobId id =BlobId.of(bucketName,fileName);
				storage.delete(id);
				return  true;
			}catch (Exception e) {return false;}
		}return  false;

	}
	
	
	
}
