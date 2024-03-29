package project.DataBridge.Controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import project.DataBridge.Services.DownloadingFile;
import project.DataBridge.Services.Timer;
import project.DataBridge.Services.UploadingFiles;

@RestController
public class Controller {

	@Value("${DB_ADMIN_PASSWORD}")
	String pass;
	@Autowired
	private DownloadingFile downloader;

	@Autowired
	private UploadingFiles uploader;

	public static Boolean isAllowed=false;
	private int requests=0;

	@GetMapping("/calls")
	public int getNoOfRequests(){
		return this.requests;}

	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		requests++;
		return ResponseEntity.ok("Api is alive");
	}

	@GetMapping("/view")
	public ResponseEntity<Resource> view() throws IOException {
		requests++;
		if(isAllowed) {
			String last = downloader.getLastFileName();
			Resource res = downloader.readFile(last);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + last);
			String type = downloader.getcontentType();
			headers.setContentType(MediaType.valueOf(type));
			return ResponseEntity.ok()
					.headers(headers)
					.body(res);
		}else{return  null;}
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> download() throws IOException {
		requests++;
		if(isAllowed) {
			String last = downloader.getLastFileName();
			if (last.equals("No last upload Using this API")) {
				return view();
			} else {
				Resource res = downloader.readFile(last);
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + last);
				String type = downloader.getcontentType();
				headers.setContentType(MediaType.valueOf(type));
				return ResponseEntity.ok()
						.headers(headers)
						.body(res);
			}
		}else{return null;}
	}

	@GetMapping("/delete/{filename}")
	public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
		requests++;
		if(isAllowed){
			if(downloader.deleteFile(filename)){
				return ResponseEntity.ok().build();
			}else{
				return ResponseEntity.badRequest().build();
			}

		}else{return  null;}
	}

	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadfile(@PathVariable String filename) {
		requests++;
		if(isAllowed){
		Resource res;
		if(downloader.doesFileExists(filename)) {
			try {
				res = downloader.readFile(filename);
			} catch (IOException e) {
				return null;
			}
		HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename);
	    String type=downloader.getcontentType();
	    headers.setContentType(MediaType.valueOf(type));
		 return ResponseEntity.ok()
		            .headers(headers)
		            .body(res);
		} else{
			return null;
		}}else{return  null;}
	}

	public static void setIsAllowed(boolean val){
		isAllowed=val;
	}

	Timer time= new Timer();
	@GetMapping("/{password}/stop")
	public String blockAll(@PathVariable String password){
		requests++;
		if(password.equals(this.pass)){
			time.setThreadPermission(false);
//			setIsAllowed(false);
			return "All Permissions Revoked!";}else
		{return "Wrong Password";}
	}

	@GetMapping("/{password}/{timeMin}")
	public String allowEveryone(@PathVariable String password, @PathVariable int timeMin){
		requests++;
		time.setThreadPermission(false);
		try{
		Thread.sleep(1100);}catch (Exception e){System.out.println("Controller thread sleep failed!");}
		time.setThreadPermission(true);
		if(password.equals(this.pass)){
			time.setMins(timeMin);
			time.setThreadPermission(true);
			Thread th= new Thread(time);
			th.start();
			setIsAllowed(true);
			return "Permissions Granted!";}else
		{return "Wrong Password";}
	}
	

	@PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile file)throws IOException {
		requests++;
		if(isAllowed){
        System.out.println("Received file: " + file.getOriginalFilename());
			uploader.upload(file, file.getOriginalFilename());
			return ResponseEntity.ok("File uploaded successfully");}else{return null;}
    }
	
	
	@GetMapping("/list")
	public List<List<String>> getFileList() {
		requests++;
		if(isAllowed){
		return downloader.listOfBlob();}else{return  null;}
	}
	
}
