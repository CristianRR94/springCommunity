package com.project.community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.project.community.enums.StorageFolder;
import com.project.community.security.SecurityMethods;
import com.project.community.servicios.ImageServiceImpl;
import com.project.community.storage.StorageException;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {
	
	@Mock
	private SecurityMethods securityMethods;
	
	@InjectMocks
	private ImageServiceImpl imageService;
	
	//Negative path
	
	@Test
	void postImage_ShouldThrowStorageException_WhenFileIsNullorEmpty() {
		//arrange
		MultipartFile fileNull = null;
		
		//act & assert
		StorageException ex = assertThrows(StorageException.class, ()->{
			imageService.postImage(fileNull, StorageFolder.USUARIOS);
		});
		
		assertEquals("Archivo vacío", ex.getMessage());
	}
	
	@Test
	void postImage_ShouldThrowStorageException_WhenFolderIsNullOrEmpty() {
		//arrange 
		MultipartFile mockFile = new MockMultipartFile("file", "test.png", "image/png", "contenido".getBytes());
		
		// Act & Assert
        StorageException exception = assertThrows(StorageException.class, () -> {
            imageService.postImage(mockFile, null);
        });

        assertEquals("Error en la carpeta de almacenamiento", exception.getMessage());
    
	}
	
	// Happy Path
	
	@Test
	void postImage_ShouldSaveImage_WhenIsCorrect() throws IOException{
		//arrange
		String originalName = "foto.png";
		byte[] content = "false_image".getBytes();
		MultipartFile mockFile = new MockMultipartFile("image", originalName, "image/png", content);
		// Simulamos el comportamiento de las dependencias que inyectamos
        // Cuando en el servicio se llame a getValidacionExtension, queremos que devuelva ".png"
        when(securityMethods.getValidacionExtension(originalName)).thenReturn(".png");
        
        // Como getRootLocation() devuelve un Path, podemos usar uno temporal o mockearlo si es complejo.
        // Para este ejemplo simplificado, asumamos que securityMethods devuelve un path temporal del sistema
        java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("storage_test");
        when(securityMethods.getRootLocation()).thenReturn(tempDir);

        // Act
        String resultadoPath = imageService.postImage(mockFile, StorageFolder.USUARIOS);

        // Assert
        assertNotNull(resultadoPath);
        assertTrue(resultadoPath.startsWith("usuarios/"));
        assertTrue(resultadoPath.endsWith(".png"));

        // Verificaciones de comportamiento: Asegurarnos de que el "guardián" de seguridad realmente se ejecutó
        verify(securityMethods, times(1)).limpiarFile(originalName);
        verify(securityMethods, times(1)).getValidacionExtension(originalName);
    
	}

}
