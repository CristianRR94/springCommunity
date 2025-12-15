package com.project.community.servicios;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.project.community.controladores.TokenResponse;
import com.project.community.entidades.Participante;
import com.project.community.entidades.Token;
import com.project.community.entidades.Usuario;
import com.project.community.repositorios.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioParticipanteServiceImpl implements UsuarioParticipanteService{
	private final UsuarioService usuarioService;
	private final ParticipanteService participanteService;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	
	
	@Override
	@Transactional
	public TokenResponse createUsuarioParticipante(Usuario usuario) {
		Usuario nuevoUsuario = usuarioService.postUsuario(usuario);
		Participante participante = participanteService.crearParticipanteNombreUsuario(nuevoUsuario.getNombre(), nuevoUsuario);
		participanteService.postParticipante(participante);	
		String jwtToken = jwtService.generateToken(nuevoUsuario);
		String refreshToken = jwtService.generateRefreshToken(nuevoUsuario);
		guardarUsuarioToken(nuevoUsuario, jwtToken);
		return new TokenResponse(jwtToken, refreshToken);		
	}

	@Override
	@Transactional
	public void deleteUsuarioParticipante(Long id){
		Participante participante = participanteService.findParticipanteByUsuario(id);
		if(participante !=null) {
			participanteService.deleteParticipante(participante);
		}
		usuarioService.deleteUsuario(id);
	}
	
	@Override
	@Transactional
	public Usuario modNombreUsuarioParticipante(Long id, String nuevoNombre) {
		Usuario usuario = usuarioService.getUsuario(id);
		if(nuevoNombre == null || nuevoNombre.isBlank()) {
			throw new IllegalArgumentException("Intoduce un nombre válido");
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
		Usuario otroUsuario = usuarioService.encontrarNombre(nuevoNombre);
		if(otroUsuario.getNombre() != null) {
			throw new IllegalArgumentException("Nombre ya en uso");
		}
		participanteService.cambiarParticipanteNombre(nuevoNombre, id);
		usuario.cambiarNombre(nuevoNombre);
		usuarioService.putUsuario(usuario);
		return usuario;
		}

	@Override
	public void guardarUsuarioToken(Usuario usuario, String jwtToken) {
		var token = Token.builder()
				.usuario(usuario)
				.token(jwtToken)
				.tokenType(Token.TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepository.save(token);
	}
	
	public TokenResponse refreshToken(final String authHeader) {
		return null;
	}
	
	
}
