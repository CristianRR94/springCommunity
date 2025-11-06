package com.project.community.servicios;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService{
	
	private final Path rootLocation = Paths.get("images");

	public ImageServiceImpl() {
	    try {
	        Files.createDirectories(rootLocation);
	    } catch (IOException e) {
	        throw new RuntimeException("No se pudo crear el directorio para imágenes", e);
	    }
	}
	@Override
	public Resource getImage(String filename) {
	
		return null;
	}

	@Override
	public Stream<Path> getImages() {
	
		return null;
	}

	@Override
	public String postImage(MultipartFile file) {
		try {
			if(file.isEmpty()) {
				throw new RuntimeException("Archivo vacío");
			}
			Path destination = rootLocation.resolve(
					Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
			if(!destination.getParent().equals(rootLocation.toAbsolutePath())) {
				throw new RuntimeException("No se puede guardar fuera del directorio permitido");
			}
			
			try(InputStream  inputStream = file.getInputStream()){
				Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
			}
			return destination.toString();
		}
		catch (IOException e) {
			throw new RuntimeException("Error al guardar el archivo", e);
		}
	}

	@Override
	public void deleteImage(String filename) {

		
	}

}
