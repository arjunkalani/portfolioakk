package com.larke.gateway.dao;

import java.util.List;
import org.springframework.context.annotation.ComponentScan;

import com.larke.gateway.model.File;
import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
public interface FileDao {

	public void updateDbWithFileInfo(String id, String name, String url, Long size, long owner);
	
	public List<File> listAllFilesDAO();
	
	public File findByFileName(String fileName);

}
