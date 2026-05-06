package com.project.community.security;

import java.nio.file.Path;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.project.community.storage.StorageProperties;

@Component
public class SecurityMethods {
	
	//metodos para imagen
	//***************************************************************************************
	//cambiar para producción:
	//public /*final*/Path rootLocation = Path.of("images").toAbsolutePath().normalize();
	private final Path rootLocation;
	private static final Set<String> EXTENSIONES_IMAGEN = Set.of(".jpg", ".jpeg", ".png", ".gif");
	
	public SecurityMethods(StorageProperties properties) {
		// Convertimos el String "images" de las propiedades en un Path real
        this.rootLocation = Path.of(properties.getLocation()).toAbsolutePath().normalize();
	}
	public Path getRootLocation() {
		return rootLocation;
	}
	//evitar directory traversal
	public void limpiarFile (String filename){
		String cleanFilename = StringUtils.cleanPath(filename);
        if (cleanFilename.contains("..")) {
            throw new RuntimeException("Nombre de archivo inválido");
        }
	}
	//controlar que sea en el directorio corecto
	public void controlarDirectorio(Path imagePath) {
		if (!imagePath.toAbsolutePath().startsWith(rootLocation.toAbsolutePath())) { //posible fallo?
            throw new RuntimeException("Acceso fuera del directorio permitido");
        }
	}
	
	//Obtener la extension
	public String getValidacionExtension(String filename) {
		if(filename == null || filename.isBlank()) {
			throw new RuntimeException("No se ha podido obtener la extension");
		}
		//obtenemos string despues de punto, si existe
		int ultimoPunto = filename.lastIndexOf(".");
		String extension = (ultimoPunto >= 0) ? filename.substring(ultimoPunto).toLowerCase() : null;
		//validamos
		validarExtension(extension);
		return extension;
	}
	//validar la extension
	public void validarExtension(String extension) {
		if(!EXTENSIONES_IMAGEN.contains(extension)) {
			throw new RuntimeException("Extension no permitida: " + extension);
		}
	}
	//****************************************************************************************
}
