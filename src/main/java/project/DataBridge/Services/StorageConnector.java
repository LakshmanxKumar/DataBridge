package project.DataBridge.Services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class StorageConnector {
	FileInputStream serviceAccount = null;
	
	Storage storage=null;
	
	StorageConnector(){
	
	try {
		serviceAccount = new FileInputStream("./serviceAccountKey.json");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String projectId="data-bridge-a6b0a";
    
	try {
		storage=StorageOptions.newBuilder()
		.setCredentials(ServiceAccountCredentials.fromStream(serviceAccount))
		.setProjectId(projectId)
		.build().getService();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
