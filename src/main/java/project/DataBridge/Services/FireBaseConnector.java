package project.DataBridge.Services;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

	
@Service
public class FireBaseConnector {
	Storage storage=null;
	
	public Storage getStorage() {return storage;}
	
	@PostConstruct
	public void makeConnectionToFirebase() {
	try {
	FileInputStream serviceAccount =
			new FileInputStream("./serviceAccountKey.json");
	
		String projectId="data-bridge-a6b0a";
    
	try {
		storage=StorageOptions.newBuilder()
		.setCredentials(ServiceAccountCredentials.fromStream(serviceAccount))
		.setProjectId(projectId)
		.build().getService();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	}catch(Exception e) {
		System.out.println("Failed to established connection");
		e.printStackTrace();
	}
	}	
}