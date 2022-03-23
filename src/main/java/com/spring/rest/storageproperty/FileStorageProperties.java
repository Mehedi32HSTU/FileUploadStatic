package com.spring.rest.storageproperty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="file")
public class FileStorageProperties {
	private String uploadDir;
	
//	Getter
	public String getUploadDir() {
		return uploadDir;
	}
	
//	Setter
	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
	

}
