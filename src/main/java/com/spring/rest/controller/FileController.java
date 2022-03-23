package com.spring.rest.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.rest.model.FileDbModel;
import com.spring.rest.payload.UploadFileResponse;
import com.spring.rest.repository.StaticFileDbRepository;
import com.spring.rest.service.FileStorageService;
import com.spring.rest.storageproperty.FileStorageProperties;

//import com.sun.org.slf4j.internal.Logger;

@RestController
@RequestMapping("Files")
public class FileController {
	private static final Logger logger=LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private StaticFileDbRepository staticFileDbRepository;
	
	@PostMapping("/uploadfile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file)
	{
		String fileName = fileStorageService.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/downloadFile/")
				.path(fileName)
				.toUriString();
//		have to save file into db here
		System.out.println(fileName+" "+file.getContentType()+" "+file.getSize()+" "+ fileDownloadUri);
		FileDbModel fileDbModel = new FileDbModel(fileName,file.getContentType(),file.getSize(), fileDownloadUri);
		staticFileDbRepository.save(fileDbModel);
		
		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}
	@PostMapping("uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files){
		
		return Arrays.asList(files).stream().map(file->uploadFile(file)).collect(Collectors.toList());
	}
	
	@GetMapping("/download/{fineName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request)
	{
		Resource resource =fileStorageService.loadFileAsResource(fileName); 
		String contentType=null;
		try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
		
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
}
