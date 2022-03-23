package com.spring.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.spring.rest.service.FileStorageService;
import com.spring.rest.storageproperty.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class FileUploadStaticApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadStaticApplication.class, args);
	}

}
