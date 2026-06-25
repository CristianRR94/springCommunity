package com.project.community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.community.entidades.Usuario;
import com.project.community.servicios.AuthDataService;
import com.project.community.servicios.CuentaServiceImpl;
import com.project.community.servicios.JwtProviderService;
import com.project.community.servicios.ParticipanteService;
import com.project.community.servicios.TokenManagementService;
import com.project.community.servicios.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceImplTest {

	@Mock
	private UsuarioService usuarioService;
	@Mock
	private ParticipanteService participanteService;
	@Mock
	private TokenManagementService tokenManagementService;
	@Mock
	private JwtProviderService jwtProviderService;
	@Mock
	private AuthDataService authDataService;
	
	@InjectMocks
	private CuentaServiceImpl cuentaServiceImpl;
	
	@ParameterizedTest
	@CsvSource({
		"'', 'Introduce un nombre válido'", //vacio
		"'  ', 'Introduce un nombre válido'", //espacios
		"'nombre-antiguo', 'Introduce un nombre distinto al antiguo'"
	})
	void modNombreUsuarioParticipante_ShouldThrowExceptions_WhenWrongName(String nuevoNombre, String mensajeEsperado) {
		//Arrange
		Usuario usuario = new Usuario();
		usuario.setNombre("nombre-antiguo");
		
		when(authDataService.obtenerUsuarioAutenticado()).thenReturn(usuario);
		
		//Act & Assert
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			cuentaServiceImpl.modNombreUsuarioParticipante(nuevoNombre);
		});
		
		assertEquals(mensajeEsperado, ex.getMessage());
	}
	
	@Test
	void modNombreUsuarioParticipante_ShouldThrowException_WhenNameInUse() {
		//Arrange
		Usuario usuario = new Usuario();
		usuario.setNombre("Nombre");
		
		when(authDataService.obtenerUsuarioAutenticado()).thenReturn(usuario);
		when(usuarioService.existeNombre("NombreExistente")).thenReturn(true);
		
		//Act & Assert
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
			cuentaServiceImpl.modNombreUsuarioParticipante("NombreExistente");
		});
		
		assertEquals("Nombre ya en uso", ex.getMessage());
	}
	
	//Happy path
	@Test
	void modNombreUsuarioParticipante_ShouldChangeNameInDatabase_WhenIsCorrect() {
		//Arrange
		Usuario usuario = new Usuario();
		usuario.setNombre("Nombre");
		
		when(authDataService.obtenerUsuarioAutenticado()).thenReturn(usuario);
		when(usuarioService.putUsuario(usuario)).thenReturn(usuario);
		//Act
		cuentaServiceImpl.modNombreUsuarioParticipante("NombreMod");
		
		//Assert
		assertEquals("NombreMod", usuario.getNombre());
		verify(participanteService, times(1)).cambiarParticipanteNombre("NombreMod");
		verify(usuarioService, times(1)).putUsuario(usuario);
	}
}