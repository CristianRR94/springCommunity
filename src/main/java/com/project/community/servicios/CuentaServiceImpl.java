package com.project.community.servicios;

import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Usuario;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class CuentaServiceImpl implements CuentaService{
	private final UsuarioService usuarioService;
	private final ParticipanteService participanteService;
	private final TokenManagementService tokenManagementService;
	private final JwtProviderService jwtProviderService;
	private final AuthDataService authDataService;
	
	
	@Override
	@Transactional
	public TokenResponse createUsuarioParticipante(Usuario usuario) {
		Usuario nuevoUsuario = usuarioService.postUsuario(usuario);
		Participante participante = participanteService.crearParticipanteNombreUsuario(nuevoUsuario.getNombre(), nuevoUsuario);
		participanteService.postParticipante(participante);	
		String jwtToken = jwtProviderService.generateToken(nuevoUsuario);
		String refreshToken = jwtProviderService.generateRefreshToken(nuevoUsuario);
		tokenManagementService.saveUsuarioToken(nuevoUsuario, jwtToken);
		return new TokenResponse(jwtToken, refreshToken);		
	}

	@Override
	@Transactional
	public void deleteUsuarioParticipante(Long id){
		Participante participante = authDataService.obtenerParticipanteAutenticado();
		if(participante !=null) {
			participanteService.deleteParticipante(participante);
		}
		usuarioService.deleteUsuario(id);
	}
	
	@Override
	@Transactional
	public Usuario modNombreUsuarioParticipante(String nuevoNombre) {
		Usuario usuario = authDataService.obtenerUsuarioAutenticado();
		if(nuevoNombre == null || nuevoNombre.isBlank()) {
			throw new IllegalArgumentException("Introduce un nombre válido");
		}
		if(usuario == null) {
			throw new IllegalArgumentException("No se ha encontrado ningún usuario");
		}
		String nombreViejo = usuario.getNombre();
		if(nombreViejo == null || nombreViejo.isBlank()) {
			throw new IllegalArgumentException("No se ha encontrado ningún nombre");
		}
		if(nombreViejo.equals(nuevoNombre)) {
			throw new IllegalArgumentException("Introduce un nombre distinto al antiguo");
		}
		
		if(usuarioService.existeNombre(nuevoNombre)) {
			throw new IllegalArgumentException("Nombre ya en uso");
		}
		participanteService.cambiarParticipanteNombre(nuevoNombre);
		usuario.cambiarNombre(nuevoNombre);
		usuarioService.putUsuario(usuario);
		return usuario;
		}

	
	
	
}
