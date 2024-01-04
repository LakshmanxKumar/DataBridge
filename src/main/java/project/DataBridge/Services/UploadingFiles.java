package project.DataBridge.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;




@Service
public class UploadingFiles {
	@Value("${firebase.storage.bucket-name}")
    private String bucketName;

//	@Value("${DB_ADMIN_PASSWORD}")
//	String pass;
		
	@Autowired
	FireBaseConnector connection;

//	public String getMyPass(){return this.pass;}
	
	public void upload(MultipartFile file, String remoteFileName) throws IOException {
	    Storage storage = connection.getStorage();
	    BlobId blobId = BlobId.of(bucketName, remoteFileName);
	    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
	            .setContentType(file.getContentType())
	            .build();
	    storage.create(blobInfo, file.getBytes());
	    updateLastFileName(file.getOriginalFilename());
	    System.out.println("Uploaded : "+file.getOriginalFilename());
	}
	
	
	public void updateLastFileName(String name) {
		Storage storage=connection.getStorage();
		BlobId blobId = BlobId.of(bucketName, "***LASTFILE***");
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
		storage.create(blobInfo, name.getBytes(StandardCharsets.UTF_8));
	}
	
	@PostConstruct
	public void lastUploadCreation() throws IOException {
		Storage storage=connection.getStorage();
		BlobId blobId = BlobId.of(bucketName, "***LASTFILE***");
		try {
		storage.readAllBytes(blobId);
//		System.out.println(new String(content));
		}catch (Exception e) {
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
		storage.create(blobInfo, "No last upload Using this API".getBytes(StandardCharsets.UTF_8));}
	}
	
	
	
	}
