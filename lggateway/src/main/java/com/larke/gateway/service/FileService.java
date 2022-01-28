package com.larke.gateway.service;



import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import com.larke.gateway.model.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ComponentScan(basePackages = "com.larke.gateway")
public interface FileService {

	public File viewFile(String fileId);

	public void deleteAFile(File file);
	
	public List<File> listAllFilesDAO();
	
	public Resource downloadFile(String filename);
	
	public File findByFileName(String fileName);
	
	public File store(MultipartFile file);
	
	public Stream<File> getAllFiles();
	
	public List<File> listAllFilesRepo();	
	
	public Optional<File> getImageById(String id);
	
	public void deleteFile(String id);
	
	public Stream<File> getLocalFile(String id);
	
	public File getFile(String id);

}
