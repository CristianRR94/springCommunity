package com.project.community.servicios;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.project.community.enums.StorageFolder;

public interface ImageService {
	
	public Resource getImage(String filename);
		
	public Stream<Path> getImages();
	
	public String postImage(MultipartFile file, StorageFolder folder);
	
	public void deleteImage(String filename);
	
}
