package com.larke.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationProperties("file")
@ComponentScan(basePackages = "com.larke.gateway")
public class FileProperties {
	    private String uploadDir;

	    public String getUploadDir() {
	        return uploadDir;
	    }

	    public void setUploadDir(String uploadDir) {
	        this.uploadDir = uploadDir;
	    }
	}
