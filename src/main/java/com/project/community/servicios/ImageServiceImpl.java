package com.project.community.servicios;

import java.io.IOException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.community.security.SecurityMethods;

@Service
public class ImageServiceImpl implements ImageService{
	
	private final Path rootLocation = Paths.get("images");
	private final SecurityMethods securityMethods;

	public ImageServiceImpl(SecurityMethods securityMethods) {
		this.securityMethods = securityMethods;
	    try {
	        Files.createDirectories(rootLocation);
	    } catch (IOException e) {
	        throw new RuntimeException("No se pudo crear el directorio para imágenes", e);
	    }
	}
		
	//Obtener la extension
		public String getExtension(String filename) {
			if(filename == null || filename.isBlank()) {
				throw new RuntimeException("No se ha podido obtener la extension");
			}
			//obtenemos string despues de punto, si existe
			int ultimoPunto = filename.lastIndexOf(".");
			return (ultimoPunto >= 0) ? filename.substring(ultimoPunto).toLowerCase() : null;
		}
	@Override
	public Resource getImage(String filename) {
		
		
		try {
			securityMethods.getValidacionExtension(filename);
			securityMethods.limpiarFile(filename);			
			Path imagePath = rootLocation.resolve(filename).normalize();
			securityMethods.controlarDirectorio(imagePath);
			Resource resource = new UrlResource(imagePath.toUri());
			if(!resource.exists()) {
				throw new RuntimeException("Archivo no encontrado");
			}
			return resource;
		}
		catch (IOException e) {
			throw new RuntimeException("Error al obtener la imagen", e);
		}	
	}

	@Override
	public Stream<Path> getImages() {
	
		return null;
	}

	@Override
	public String postImage(MultipartFile file, Long eventoId) {
		try {
			if(file.isEmpty()) {
				throw new RuntimeException("Archivo vacío");
			}
			String extension = getExtension(file.getOriginalFilename());
			String nombreImagen = "evento_" + eventoId + extension;
			Path destination = rootLocation.resolve(
				Path.of(file.getOriginalFilename())).normalize().toAbsolutePath();
			if(!destination.startsWith(rootLocation.toAbsolutePath())) {
				throw new RuntimeException("No se puede guardar fuera del directorio permitido");
			}
			try(InputStream  inputStream = file.getInputStream()){
				Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
			}
			return nombreImagen;
		}
		catch (IOException e) {
			throw new RuntimeException("Error al guardar el archivo", e);
		}
	}

	@Override
	public void deleteImage(String filename) {
		try {			
			securityMethods.limpiarFile(filename);			
			Path imagePath = rootLocation.resolve(filename).normalize();
			securityMethods.controlarDirectorio(imagePath);
			Files.deleteIfExists(imagePath);	 
		}
		catch (IOException e) {
			throw new RuntimeException("Error al eliminar la imagen", e);
		}
		
	}
	
	

}
