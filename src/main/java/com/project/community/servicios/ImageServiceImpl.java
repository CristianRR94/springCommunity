package com.project.community.servicios;

import java.io.IOException;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.community.enums.StorageFolder;
import com.project.community.security.SecurityMethods;
import com.project.community.storage.StorageException;
import com.project.community.storage.StorageFileNotFoundException;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService{
	
	//private final Path rootLocation = Paths.get("images");
	private final SecurityMethods securityMethods;
	

	@PostConstruct
	public void init() {
	    try {
	        Files.createDirectories(securityMethods.getRootLocation());
	    } catch (IOException e) {
	        throw new StorageException("No se pudo crear el directorio para imágenes", e);
	    }
	}
		
	//Obtener la extension
	//(movido a securityMethods)
	@Override
	public Resource getImage(String filename, String folder) {

		try {
			securityMethods.getValidacionExtension(filename);
			securityMethods.limpiarFile(filename);			
			Path imagePath = securityMethods.getRootLocation().resolve(filename).normalize();
			securityMethods.controlarDirectorio(imagePath);
			Resource resource = new UrlResource(imagePath.toUri());
			if(!resource.exists()) {
				throw new StorageFileNotFoundException("Archivo no encontrado");
			}
			return resource;
		}
		catch (IOException e) {
			throw new StorageException("Error al obtener la imagen", e);
		}	
	}

	@Override
	public Stream<Path> getImages() {
	
		return null;
	}

	@Override
	public String postImage(MultipartFile file, StorageFolder folder) {
		try {
			if(file.isEmpty()) {
				throw new RuntimeException("Archivo vacío");
			}
			if(folder == null) {
				throw new StorageException("Error en la carpeta de alamcenamiento");
			}
			String originalFileName = file.getOriginalFilename();
			securityMethods.limpiarFile(originalFileName);
			String extension = securityMethods.getValidacionExtension(file.getOriginalFilename());
			String nombreImagen =  UUID.randomUUID().toString() + extension;
			String nombreFolder = folder.getPath();
			Path folderPath = securityMethods.getRootLocation().resolve(nombreFolder);
			if(!Files.exists(folderPath)) {
				Files.createDirectories(folderPath);
			}
			Path destination = folderPath.resolve(nombreImagen).normalize().toAbsolutePath();
			securityMethods.controlarDirectorio(destination);
			try(InputStream  inputStream = file.getInputStream()){
				Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
			}
			//nombreFolder para tenerlo en minúsculas
			return nombreFolder + "/" +nombreImagen;
		}
		catch (IOException e) {
			throw new StorageException("Error al guardar el archivo", e);
		}
	}

	@Override
	public void deleteImage(String filename, String folder) {
		try {			
			securityMethods.limpiarFile(filename);			
			Path imagePath = securityMethods.getRootLocation().resolve(filename).normalize();
			securityMethods.controlarDirectorio(imagePath);
			Files.deleteIfExists(imagePath);	 
		}
		catch (IOException e) {
			throw new StorageException("Error al eliminar la imagen", e);
		}
		
	}

}
